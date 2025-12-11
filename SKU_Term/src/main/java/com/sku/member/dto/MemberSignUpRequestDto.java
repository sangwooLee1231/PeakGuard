package com.sku.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원가입 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class MemberSignUpRequestDto {

    @NotBlank(message = "학번은 필수 값입니다.")
    private String studentNumber;

    @NotBlank(message = "비밀번호는 필수 값입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "이름은 필수 값입니다.")
    private String name;

    private String department;

    private Integer grade;
}
