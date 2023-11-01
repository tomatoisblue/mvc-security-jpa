package com.example.mvcsecurityjpa.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import com.example.mvcsecurityjpa.authenticationProvider.CustomAuthenticationProvider;
import com.example.mvcsecurityjpa.filter.CustomRequestHeaderAuthenticationFilter;
import com.example.mvcsecurityjpa.filter.JsonEmailPasswordAuthenticationFilter;
import com.example.mvcsecurityjpa.service.userDetails.CustomAuthenticationUserDetailsService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * SecurityConfig
 */
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final CustomAuthenticationProvider authenticationProvider;
  private final CustomAuthenticationUserDetailsService authenticationUserDetailsService;

  public SecurityConfig(CustomAuthenticationProvider authenticationProvider,
                        CustomAuthenticationUserDetailsService authenticationUserDetailsService,
                        AuthenticationManagerBuilder authenticationManagerBuilder) {
    this.authenticationProvider = authenticationProvider;
    this.authenticationUserDetailsService = authenticationUserDetailsService;
    this.authenticationManagerBuilder = authenticationManagerBuilder;
  }

  /**
   * @param http
   * @return
   * @throws Exception
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // http.csrf(Customizer.withDefaults())
    http.csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
          .requestMatchers("/css/**", "/signup", "/login", "/error").permitAll()
          // .anyRequest().permitAll());
          .anyRequest().authenticated());

    http.headers(headers -> headers
      .frameOptions(frame -> frame.disable()));

    JsonEmailPasswordAuthenticationFilter jsonEmailPasswordAuthenticationFilter
      = new JsonEmailPasswordAuthenticationFilter(authenticationManager(http));

    CustomRequestHeaderAuthenticationFilter customRequestHeaderAuthenticationFilter
      = new CustomRequestHeaderAuthenticationFilter(authenticationManager(http));

    var preAuthenticatedAuthenticationProvider = new PreAuthenticatedAuthenticationProvider();
    preAuthenticatedAuthenticationProvider.setPreAuthenticatedUserDetailsService(authenticationUserDetailsService);
    preAuthenticatedAuthenticationProvider.setUserDetailsChecker(new AccountStatusUserDetailsChecker());
    authenticationManagerBuilder.authenticationProvider(preAuthenticatedAuthenticationProvider);

    http.addFilterAt(jsonEmailPasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    http.addFilter(customRequestHeaderAuthenticationFilter);

    http.exceptionHandling(handling -> handling.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
    http.exceptionHandling(handling -> handling.accessDeniedHandler((req, res, ex) -> res.setStatus(HttpServletResponse.SC_FORBIDDEN)));


    // http.logout(logout -> logout
    //   .logoutUrl("/logout")
    //   .logoutSuccessHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK))
    //   .invalidateHttpSession(true));


    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder =
      http.getSharedObject(AuthenticationManagerBuilder.class);
      authenticationManagerBuilder.authenticationProvider(authenticationProvider);
      return authenticationManagerBuilder.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}