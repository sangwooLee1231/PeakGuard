package com.sku.cart.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
public class Cart {

    private Long id;         // CART_ID
    private Long studentId;  // CART_STUDENT_ID
    private Long lectureId;  // CART_LECTURE_ID
}