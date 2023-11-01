package com.example.mvcsecurityjpa.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * CustomRequestHeaderAuthenticationFilter
 */
public class CustomRequestHeaderAuthenticationFilter extends RequestHeaderAuthenticationFilter {

  public CustomRequestHeaderAuthenticationFilter(AuthenticationManager authenticationManager) {

    setPrincipalRequestHeader("Authorization");
    setExceptionIfHeaderMissing(false);
    setAuthenticationManager(authenticationManager);
    setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/boards"));

    this.setAuthenticationSuccessHandler((req, res, auth) -> {
      System.out.println("Success: Header Authentication");
    });

    this.setAuthenticationFailureHandler((req, res, auth) -> {
      System.out.println("Fail: Header Authentication");
    });

  }


}