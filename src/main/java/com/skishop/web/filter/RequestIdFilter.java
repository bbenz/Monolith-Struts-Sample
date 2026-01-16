package com.skishop.web.filter;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.MDC;

public class RequestIdFilter implements Filter {
  private static final String HEADER_NAME = "X-Request-Id";
  private static final String MDC_KEY = "reqId";

  public void init(FilterConfig filterConfig) {
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    String requestId = null;
    if (request instanceof HttpServletRequest) {
      requestId = ((HttpServletRequest) request).getHeader(HEADER_NAME);
    }
    if (isBlank(requestId)) {
      requestId = UUID.randomUUID().toString();
    }
    MDC.put(MDC_KEY, requestId);
    if (response instanceof HttpServletResponse) {
      ((HttpServletResponse) response).setHeader(HEADER_NAME, requestId);
    }
    try {
      chain.doFilter(request, response);
    } finally {
      MDC.remove(MDC_KEY);
    }
  }

  public void destroy() {
  }

  private boolean isBlank(String value) {
    if (value == null) {
      return true;
    }
    for (int i = 0; i < value.length(); i++) {
      if (!Character.isWhitespace(value.charAt(i))) {
        return false;
      }
    }
    return true;
  }
}
