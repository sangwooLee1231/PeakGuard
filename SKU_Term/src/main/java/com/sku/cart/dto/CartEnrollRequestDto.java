package com.sku.cart.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CartEnrollRequestDto {

    @NotEmpty(message = "신청할 강의 ID 목록은 비어 있을 수 없습니다.")
    private List<Long> lectureIds;
}
