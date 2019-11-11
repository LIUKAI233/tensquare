package com.tensquare.spit.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JWTUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component //放置到spring容器中
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JWTUtil jwtUtil;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
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
                                request.setAttribute("token_admin",token);
                            }
                            if (roles.equals("user")){
                                request.setAttribute("token_user",token);
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
