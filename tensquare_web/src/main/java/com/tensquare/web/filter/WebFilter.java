package com.tensquare.web.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class WebFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";  //过滤器执行位置
    }

    @Override
    public int filterOrder() {
        return 0;   //过滤器执行优先度  数字越大，优先度越低
    }

    @Override
    public boolean shouldFilter() {
        return true;  //是否执行该过滤器，此处为true，说明需要过滤
    }

    @Override
    public Object run() throws ZuulException {
        //不进行权限验证，转发header信息
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        //获取头信息
        String header = request.getHeader("Authorization");
        if (header != null && !"".equals(header)){
            requestContext.addZuulRequestHeader("Authorization",header);
        }
        return null;
    }
}
