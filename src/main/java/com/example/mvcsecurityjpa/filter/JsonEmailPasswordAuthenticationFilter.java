package com.example.mvcsecurityjpa.filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JsonEmailPasswordAuthenticationFilter
 */
public class JsonEmailPasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
  private ObjectMapper objectMapper = new ObjectMapper();

  private String emailParameter = "email";
  private String passwordParameter = "password";


  public JsonEmailPasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
    super(new AntPathRequestMatcher("/login", "POST"));
    this.setAuthenticationManager(authenticationManager);


    // Set JWT Token on header When Authentication successed
    Date issuedAt = new Date();
    this.setAuthenticationSuccessHandler((req, res, auth) -> {
      System.out.println("AUTHENTICATION SUCCESS!");
      String username = auth.getName();

      String token = JWT.create()
                      .withIssuer("tomatoisblue.net")
                      .withIssuedAt(issuedAt)
                      .withExpiresAt(new Date(issuedAt.getTime() + 1000 * 60 * 60))
                      .withClaim("username", username)
                      .withClaim("role", auth.getAuthorities().iterator().next().toString())
                      .sign(Algorithm.HMAC256("secret"));
      System.out.println("JWT : " + token);
      res.setHeader("X-AUTH-TOKEN", token);
      res.setStatus(HttpServletResponse.SC_OK);
    });

    // Set 401 on header When Authentication Failed
    this.setAuthenticationFailureHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_UNAUTHORIZED));
  }


  public String getEmailParameter() {
    return emailParameter;
  }

  public void setEmailParameter(String emailParameter) {
    this.emailParameter = emailParameter;
  }

  public String getPasswordParameter() {
    return passwordParameter;
  }

  public void setPasswordParameter(String passwordParameter) {
    this.passwordParameter = passwordParameter;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException{
    Map<String, Object> requestObject;

    try {
      requestObject = objectMapper.readValue(request.getInputStream(), Map.class);
    } catch (IOException e) {
      requestObject = new HashMap<>();
    }

    String email = Optional.ofNullable(requestObject.get(emailParameter))
                           .map(Object::toString)
                           .map(String::trim)
                           .orElse("");

    String password = Optional.ofNullable(requestObject.get(passwordParameter))
                           .map(Object::toString)
                           .orElse("");

    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);

    authRequest.setDetails(authenticationDetailsSource.buildDetails(request));

    return this.getAuthenticationManager().authenticate(authRequest);

  }
}