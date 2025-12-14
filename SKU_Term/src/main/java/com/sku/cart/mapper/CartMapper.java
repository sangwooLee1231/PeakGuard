package com.sku.cart.mapper;

import com.sku.cart.dto.CartItemResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {

    // 이미 장바구니에 담긴 강의인지 확인
    int existsCart(@Param("studentId") Long studentId,
                   @Param("lectureId") Long lectureId);

    // 장바구니 담기
    int insertCart(@Param("studentId") Long studentId,
                   @Param("lectureId") Long lectureId);

    // 장바구니에서 제거
    int deleteCart(@Param("studentId") Long studentId,
                   @Param("lectureId") Long lectureId);

    // 장바구니 목록 조회
    List<CartItemResponseDto> findCartItems(@Param("studentId") Long studentId);
}
