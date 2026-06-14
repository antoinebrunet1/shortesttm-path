package com.example.shortesttmpath.service;

import com.example.shortesttmpath.authenticationtoken.ApiKeyAuthentication;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * The authentication service.
 */
public class AuthenticationService {
  private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";
  private static final String AUTH_TOKEN = Dotenv.configure()
      .directory("backend/shortesttm_path/etc/secrets").load().get("API_KEY");

  /**
   * Default constructor.
   */
  public AuthenticationService() {
  }

  /**
   * Returns the ApiKeyAuthentication.
   *
   * @param request The request.
   * @return The ApiKeyAuthentication.
   */
  public static Authentication getAuthentication(HttpServletRequest request) {
    String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
    if (apiKey == null || !apiKey.equals(AUTH_TOKEN)) {
      throw new BadCredentialsException("Invalid API Key");
    }
    return new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
  }
}