package com.sku.member.service;

import com.sku.member.dto.MemberSignUpRequestDto;

import java.util.Map;

public interface MemberService {

    /*
    *** 회원가입
     */
    Long signUp(MemberSignUpRequestDto requestDto);


}