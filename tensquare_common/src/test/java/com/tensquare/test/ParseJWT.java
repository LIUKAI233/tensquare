package com.tensquare.test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ParseJWT {
    public static void main(String[] args) {
        String key = "LK9527";
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NjYiLCJzdWIiOiJhYmMiLCJpYXQiOjE1NzMxOTYyNjQsImV4cCI6MTU3MzE5NjMyNCwicm9sZXMiOiJhZG1pbiJ9.w_Rx7ds_WGm4yesTH4BugKwSgdgBwW3MahrehElCxos";
        Claims body = Jwts.parser().setSigningKey("LK9527").parseClaimsJws(token).getBody();
        System.out.println("用户id："+body.getId());
        System.out.println("用户名："+body.getSubject());
        System.out.println("创建时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(body.getIssuedAt()));
        System.out.println("过期时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(body.getExpiration()));
        System.out.println("当前时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        System.out.println("用户类别："+body.get("roles"));
    }
}
