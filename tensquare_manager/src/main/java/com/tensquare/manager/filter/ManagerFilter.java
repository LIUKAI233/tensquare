package com.tensquare.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JWTUtil;

import javax.servlet.http.HttpServletRequest;

@Component
public class ManagerFilter extends ZuulFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public String filterType() { //过滤器类型
        return "pre";  //前置过滤器
    }

    @Override
    public int filterOrder() {
        return 0;  //优先度
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        //如果是OPTIONS方法，不进行权限验证
        if (request.getMethod().equals("OPTIONS")){
            return null;
        }

        //如果是登录方法，不进行权限验证
        if (request.getRequestURL().indexOf("login")>0){
            return null;
        }

        String header = request.getHeader("Authorization");
        if (header != null && !"".equals(header)){
            if (header.startsWith("Bearer ")){
                String token = header.substring(7);
                try {
                    Claims claims = jwtUtil.parseJwt(token);
                    String roles =(String) claims.get("roles");
                    if (roles.equals("admin")){
                        //把头信息转发下去，并且放行
                        requestContext.addZuulRequestHeader("Authorization",header);
                        return null;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    requestContext.setSendZuulResponse(false);
                }
            }
        }
        requestContext.setSendZuulResponse(false);
        requestContext.setResponseStatusCode(403);
        requestContext.setResponseBody("权限不足");
        requestContext.getResponse().setContentType("test/html;charset=UTF-8");

        return null;
    }
}
