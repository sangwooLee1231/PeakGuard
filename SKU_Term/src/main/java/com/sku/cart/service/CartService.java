package com.sku.cart.service;

import com.sku.cart.dto.CartEnrollResultDto;
import com.sku.cart.dto.CartItemResponseDto;

import java.util.List;

public interface CartService {

    // 장바구니 담기
    void addToCart(String studentNumber, Long lectureId);

    // 장바구니 삭제
    void removeFromCart(String studentNumber, Long lectureId);

    // 장바구니 목록 조회
    List<CartItemResponseDto> getMyCart(String studentNumber);

    // 장바구니 → 수강신청
    List<CartEnrollResultDto> enrollFromCart(String studentNumber, List<Long> lectureIds);
}
