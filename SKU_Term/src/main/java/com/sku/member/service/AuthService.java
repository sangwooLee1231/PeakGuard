package com.sku.member.service;

import java.util.Map;

public interface AuthService {
    Map<String, String> login(String email, String password);
}