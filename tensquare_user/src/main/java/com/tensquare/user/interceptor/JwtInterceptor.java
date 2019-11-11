package com.tensquare.user.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JWTUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component //放入spring容器中
public class JwtInterceptor implements HandlerInterceptor {

    private JWTUtil jwtUtil;

    @Autowired
    private void setJwtUtil(JWTUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    //配置拦截器
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        //System.out.println("经过了拦截器");
        //权限验证
        //获取令牌信息
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")){
            //头信息中包含令牌信息，才进行权限验证
            String token = header.substring(7);
            if (!token.equals("")){
                try {
                    Claims claims = jwtUtil.parseJwt(token);
                    if(claims != null){
                        String roles =(String) claims.get("roles");
                        if (roles != null && !roles.equals("")){
                            if (roles.equals("admin")){
                                request.setAttribute("token_admin",token);
                            }
                            if (roles.equals("user")){
                                request.setAttribute("token_user",token);
                            }
                        }
                    }
                }catch (Exception e){
                    throw new RuntimeException("令牌信息错误");
                }
            }
        }
        return true;
    }
}
