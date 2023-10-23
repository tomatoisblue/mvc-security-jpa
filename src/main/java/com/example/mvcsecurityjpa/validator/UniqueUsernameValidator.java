package com.example.mvcsecurityjpa.validator;

import org.springframework.stereotype.Component;

import com.example.mvcsecurityjpa.annotation.UniqueUsername;
import com.example.mvcsecurityjpa.service.UserRegistrationService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * UniqueUsernameValidator
 */
@Component
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

  private UserRegistrationService userRegistrationService;

  public UniqueUsernameValidator(UserRegistrationService userRegistrationService) {
    this.userRegistrationService = userRegistrationService;
  }

  @Override
  public void initialize(UniqueUsername constraintAnnotation) {
  }

  @Override
  public boolean isValid(String username, ConstraintValidatorContext context) {
    return username != null && !userRegistrationService.isUsernameExists(username);
  }
}