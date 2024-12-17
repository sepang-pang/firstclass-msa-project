package com.first_class.msa.product.presentation.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MdcLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            String userId = httpRequest.getHeader("X-User-Id");
            String keyword = httpRequest.getParameter("name");
            String type = getRequestType(httpRequest);

            MDC.put("type", type);

            if (userId != null) {
                MDC.put("userId", userId);
            }
            if (keyword != null) {
                MDC.put("keyword", keyword);
            }

            chain.doFilter(request, response);

        } finally {

            MDC.clear();
        }
    }

    // HTTP 메서드와 URI에 따라 'type' 값을 설정하는 메서드
    private String getRequestType(HttpServletRequest request) {
        String type = null;
        String method = request.getMethod();
        String uri = request.getRequestURI();

        if (method.equals("POST") && uri.contains("/products")) {
            type = "상품 생성";
        } else if (method.equals("GET") && uri.contains("/products")) {
            type = "상품 검색";
        } else if (method.equals("PUT") && uri.contains("/products")) {
            type = "상품 수정";
        } else if (method.equals("DELETE") && uri.contains("/products")) {
            type = "상품 삭제";
        }

        return type;
    }
}