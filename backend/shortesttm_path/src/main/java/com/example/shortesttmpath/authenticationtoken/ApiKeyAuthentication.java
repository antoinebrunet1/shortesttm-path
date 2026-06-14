package com.example.shortesttmpath.authenticationtoken;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * The ApiKeyAuthentication.
 */
public class ApiKeyAuthentication extends AbstractAuthenticationToken {
  /**
   * The API key.
   */
  private final String apiKey;

  /**
   * The constructor.
   *
   * @param apiKey      The API key.
   * @param authorities The authorities.
   */
  public ApiKeyAuthentication(String apiKey, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.apiKey = apiKey;
    setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return apiKey;
  }
}