package com.sku.queue.service.serviceImpl;

import com.sku.common.exception.CustomException;
import com.sku.common.util.ErrorCode;
import com.sku.queue.dto.QueueJoinResponseDto;
import com.sku.queue.dto.QueueStatusResponseDto;
import com.sku.queue.service.QueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String QUEUE_COUNTER_KEY = "queue:counter";           // 대기 번호 카운터
    private static final String QUEUE_WAITING_ZSET_KEY = "queue:waiting";      // 대기열 ZSET
    private static final String QUEUE_TOKEN_PREFIX = "queue:token:";           // 토큰 → 번호 매핑
    private static final String QUEUE_ACTIVE_SET_KEY = "queue:active";         // 활성 토큰 집합

    // 동시에 "입장 허용"할 최대 인원
    private static final long MAX_ACTIVE_USERS = 100L;

    // 1명당 처리에 걸리는 평균 시간(추정, 초 단위) – 예상 대기시간 계산용
    private static final long AVG_PROCESS_SECONDS_PER_USER = 5L;

    // 토큰 TTL
    private static final Duration TOKEN_TTL = Duration.ofHours(1);

    /**
     * Step 1. 대기열 진입 및 순번 발급
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public QueueJoinResponseDto joinQueue() {

        String token = generateToken();

        Long queueNumber = stringRedisTemplate.opsForValue().increment(QUEUE_COUNTER_KEY);
        if (queueNumber == null) {
            log.error("Redis queue counter increment failed");
            throw new CustomException(ErrorCode.QUEUE_SERVICE_UNAVAILABLE);
        }

        String tokenKey = QUEUE_TOKEN_PREFIX + token;
        stringRedisTemplate.opsForValue().set(tokenKey, String.valueOf(queueNumber), TOKEN_TTL);

        stringRedisTemplate.opsForZSet()
                .add(QUEUE_WAITING_ZSET_KEY, token, queueNumber.doubleValue());

        Long rank = stringRedisTemplate.opsForZSet()
                .rank(QUEUE_WAITING_ZSET_KEY, token);
        long position = (rank == null ? queueNumber : rank + 1);

        QueueJoinResponseDto dto = new QueueJoinResponseDto();
        dto.setQueueToken(token);
        dto.setQueueNumber(queueNumber);
        dto.setPosition(position);

        log.info("대기열 진입 - token={}, queueNumber={}, position={}",
                token, queueNumber, position);

        return dto;
    }

    /**
     * Step 2. 대기열 상태 조회
     */
    @Override
    @Transactional(readOnly = true)
    public QueueStatusResponseDto getStatus(String queueToken) {

        if (queueToken == null || queueToken.isBlank()) {
            throw new CustomException(ErrorCode.QUEUE_TOKEN_INVALID);
        }

        String tokenKey = QUEUE_TOKEN_PREFIX + queueToken;
        String queueNumberStr = stringRedisTemplate.opsForValue().get(tokenKey);

        // 1. 활성 토큰인지 먼저 확인
        boolean active = Boolean.TRUE.equals(
                stringRedisTemplate.opsForSet().isMember(QUEUE_ACTIVE_SET_KEY, queueToken)
        );

        // 토큰이 활성 상태이면 -> 이미 입장 허용, position=0
        if (active) {
            QueueStatusResponseDto dto = new QueueStatusResponseDto();
            dto.setQueueToken(queueToken);
            dto.setQueueNumber(queueNumberStr != null ? Long.valueOf(queueNumberStr) : null);
            dto.setPosition(0L);
            dto.setActive(true);
            dto.setEstimatedWaitSeconds(0L);
            return dto;
        }

        // 활성도 아닌데, queueNumber 정보도 없으면 토큰 만료/잘못된 토큰
        if (queueNumberStr == null) {
            throw new CustomException(ErrorCode.QUEUE_TOKEN_NOT_FOUND);
        }

        Long queueNumber = Long.valueOf(queueNumberStr);

        // ZSET에서 현재 순번(랭크) 조회
        Long rank = stringRedisTemplate.opsForZSet()
                .rank(QUEUE_WAITING_ZSET_KEY, queueToken);

        if (rank == null) {
            throw new CustomException(ErrorCode.QUEUE_TOKEN_NOT_FOUND);
        }

        long position = rank + 1; // 0-based -> 1-based
        long estimatedWaitSeconds = Math.max(0, (position - 1) * AVG_PROCESS_SECONDS_PER_USER);

        QueueStatusResponseDto dto = new QueueStatusResponseDto();
        dto.setQueueToken(queueToken);
        dto.setQueueNumber(queueNumber);
        dto.setPosition(position);
        dto.setActive(false);
        dto.setEstimatedWaitSeconds(estimatedWaitSeconds);

        return dto;
    }

    /**
     * 민감 API(수강신청 등) 진입 전에 호출해서
     * 해당 토큰이 "활성 상태"인지 검증
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void validateActiveToken(String queueToken) {

        if (queueToken == null || queueToken.isBlank()) {
            throw new CustomException(ErrorCode.QUEUE_TOKEN_INVALID);
        }

        // 이미 active인 경우 → 통과
        Boolean isActive = stringRedisTemplate.opsForSet()
                .isMember(QUEUE_ACTIVE_SET_KEY, queueToken);

        if (Boolean.TRUE.equals(isActive)) {
            return;
        }

        // 아직 active가 아니라면, 상위 N명까지는 자동 승급 로직 (선택)
        promoteIfInFront(queueToken);

        // 다시 확인
        isActive = stringRedisTemplate.opsForSet()
                .isMember(QUEUE_ACTIVE_SET_KEY, queueToken);

        if (!Boolean.TRUE.equals(isActive)) {
            throw new CustomException(ErrorCode.QUEUE_NOT_ACTIVE);
        }
    }

    /**
     * 현재 활성 사용자 수와 내 순번을 보고,
     * - active 인원이 MAX_ACTIVE_USERS보다 적고
     * - 내가 waiting ZSET 상위 MAX_ACTIVE_USERS 안에 들어와 있으면
     *   → active 세트에 승급시키고 waiting ZSET에서 제거
     */
    private void promoteIfInFront(String queueToken) {

        Long activeCount = stringRedisTemplate.opsForSet()
                .size(QUEUE_ACTIVE_SET_KEY);
        if (activeCount == null) {
            throw new CustomException(ErrorCode.QUEUE_SERVICE_UNAVAILABLE);
        }

        if (activeCount >= MAX_ACTIVE_USERS) {
            return;
        }

        Long rank = stringRedisTemplate.opsForZSet()
                .rank(QUEUE_WAITING_ZSET_KEY, queueToken);

        if (rank == null) {
            return;
        }

        // 내 위치가 허용 범위 안이면 active 승급
        if (rank < MAX_ACTIVE_USERS) {
            stringRedisTemplate.opsForSet().add(QUEUE_ACTIVE_SET_KEY, queueToken);
            stringRedisTemplate.opsForZSet().remove(QUEUE_WAITING_ZSET_KEY, queueToken);
            log.info("대기열 승급 - token={}, rank={}, activeCount(before)={}",
                    queueToken, rank, activeCount);
        }
    }

    private String generateToken() {
        long timestamp = System.currentTimeMillis();
        String random = UUID.randomUUID().toString().replace("-", "");
        return timestamp + "-" + random;
    }
}
