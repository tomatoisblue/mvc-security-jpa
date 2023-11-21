package com.example.mvcsecurityjpa.service.userDetails;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.mvcsecurityjpa.entity.User;
import com.example.mvcsecurityjpa.exception.EmailNotFoundException;
import com.example.mvcsecurityjpa.repository.UserRepository;

/**
 * CustomAuthenticationUserDetailsService
 */
@Service
public class CustomAuthenticationUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

  private UserRepository userRepository;
  private UserDetailsService userDetailsService;

  public CustomAuthenticationUserDetailsService(
      UserRepository userRepository,
      UserDetailsService userDetailsService) {
    this.userRepository = userRepository;
    this.userDetailsService = userDetailsService;
  }

  @Override
  public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token)
                                                        throws  JWTDecodeException,
                                                                TokenExpiredException,
                                                                EmailNotFoundException {

    DecodedJWT decodedJwt;

    try {
      decodedJwt = JWT.require(Algorithm.HMAC256("CfNeddheorhDgnUfYqm8pjT5lykpTMUi")).build().verify(token.getPrincipal().toString());
    } catch (JWTDecodeException e) {
      throw new BadCredentialsException("Authorization header token is invalid");
    } catch (TokenExpiredException e) {
      throw e;
    }

    if (decodedJwt.getToken().isEmpty()) {
      throw new EmailNotFoundException("Authorization header must not be empty");
    }
    User user = userRepository.findByUsername(decodedJwt.getClaim("username").asString());
    return userDetailsService.loadUserByUsername(user.getEmail());
  }
}