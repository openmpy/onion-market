package com.openmpy.backend.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;

public class SecretKeyGenerator {

    public static void main(String[] args) {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String secretKey = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("secretKey = " + secretKey);
    }
}
