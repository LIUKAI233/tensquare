package com.tensquare.test;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class CreateJWT {
    public static void main(String[] args) {
        JwtBuilder claim = Jwts.builder().setId("666")
                .setSubject("abc")
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 60000))
                .signWith(SignatureAlgorithm.HS256, "LK9527")
                .claim("roles", "admin");

        System.out.println(claim.compact());
    }
}
