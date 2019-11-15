package com.tensquare.friend.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JWTUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtIntetceptor implements HandlerInterceptor {

    @Autowired
    private JWTUtil jwtUtil;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader("Authorization");
        if (header != null && !"".equals(header)){
            if (header.startsWith("Bearer ")){
                String token = header.substring(7);
                if (!"".equals(token)){
                    try {
                        Claims claims = jwtUtil.parseJwt(token);
                        if (claims != null){
                            String roles =(String) claims.get("roles");
                            if (roles.equals("admin")){
                                request.setAttribute("claims_admin",claims);
                            }
                            if (roles.equals("user")){
                                request.setAttribute("claims_user",claims);
                            }
                        }
                    }catch (Exception e){
                        throw new RuntimeException("令牌错误！");
                    }
                }
            }
        }
        return true;
    }
}
