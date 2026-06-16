package com.example.shortesttmpath.filter;

import com.example.shortesttmpath.service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/**
 * The authentication filter.
 */
public class AuthenticationFilter extends GenericFilterBean {
  private static final List<String> WHITELIST = List.of(
      "/v3/api-docs",
      "/swagger-ui",
      "/swagger-ui.html"
  );

  /**
   * Default constructor.
   */
  public AuthenticationFilter() {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException {
    try {
      String path = ((HttpServletRequest) request).getRequestURI();

      if (WHITELIST.stream().anyMatch(path::startsWith)) {
        filterChain.doFilter(request, response);

        return;
      }

      Authentication authentication =
          AuthenticationService.getAuthentication((HttpServletRequest) request);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      filterChain.doFilter(request, response);
    } catch (Exception exp) {
      HttpServletResponse httpResponse = (HttpServletResponse) response;
      httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
      PrintWriter writer = httpResponse.getWriter();
      writer.print(exp.getMessage());
      writer.flush();
      writer.close();
    }
  }
}