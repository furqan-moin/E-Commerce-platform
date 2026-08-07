package com.furqan.ecommerce.security;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtPropertyVerifier {
    private final JwtProperties jwtProperties;

    @PostConstruct
    public void init() {
        System.out.println(jwtProperties.getExpirationMs());
    }
}
