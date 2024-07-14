package com.example.demo.constants;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

public class Constants {
    public static final SecretKey JwtSecretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
}
