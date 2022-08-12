package com.yusheng.reggie.filter;


//check the user if it is logined

import com.alibaba.fastjson.JSON;
import com.yusheng.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

//path compare
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
//    1.get URI
        String requestURI = request.getRequestURI();

        log.info("request received: {}", requestURI);

//        pages which do not need filter
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };

//        2. check the URI
        boolean check = check(urls,requestURI);

//        3. result processing
        if(check){
            log.info("request do not need to intercept: {}", requestURI);
            filterChain.doFilter(request,response);
            return;
        }
//        4. if have already logined
        if(request.getSession().getAttribute("employee")!=null){
            log.info("User already logined: {}", request.getSession().getAttribute("employee"));
            filterChain.doFilter(request,response);
            return;
        }

            log.info("User not logined");
//        5. if not , outputStream
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

    }
    public boolean check(String[] urls,String requestURI){
        for(String url:urls){
            boolean match = PATH_MATCHER.match(url,requestURI);
            if(match)   return true;
        }
        return false;
    }
}
