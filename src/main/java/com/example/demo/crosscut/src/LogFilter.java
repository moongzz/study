package com.example.demo.crosscut.src;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter
public class LogFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("Filter: 요청 전");
        chain.doFilter(request, response); // 다음 필터 또는 서블릿 호출
        System.out.println("Filter: 요청 후");
    }
}
