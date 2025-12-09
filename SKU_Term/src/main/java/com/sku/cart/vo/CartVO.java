package com.sku.cart.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartVO {

    private Long cartId;         // CART_ID
    private Long cartStudentId;  // CART_STUDENT_ID
    private Long cartLectureId;  // CART_LECTURE_ID
}