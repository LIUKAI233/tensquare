package com.tensquare.qa.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JWTUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component //放到spring容器中
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JWTUtil jwtUtil;

    //拦截器
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //获取头信息
        String hander = request.getHeader("Authorization");
        if (hander != null && !"".equals(hander)){
            //如果传入令牌信息，则验证权限
            if (hander.startsWith("Bearer ")){
                String token = hander.substring(7);
                if (!"".equals(token)){
                    try {
                        Claims claims = jwtUtil.parseJwt(token);
                        if (claims != null){
                            String roles = (String) claims.get("roles");
                            if (roles.equals("admin")){
                                request.setAttribute("token_admin",token);
                            }
                            if (roles.equals("user")){
                                request.setAttribute("token_user",token);
                            }
                        }
                    }catch (Exception e){
                        throw new RuntimeException("令牌错误!");
                    }
                }
            }
        }
        return true;
    }
}
