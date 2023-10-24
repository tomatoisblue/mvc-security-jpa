package com.example.mvcsecurityjpa.validator;

import org.springframework.stereotype.Component;

import com.example.mvcsecurityjpa.annotation.UniqueEmail;
import com.example.mvcsecurityjpa.service.user.UserServiceHelper;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * UniqueEmailValidator
 */
@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

  private UserServiceHelper userServiceHelper;

  public UniqueEmailValidator(UserServiceHelper userServiceHelper) {
    this.userServiceHelper = userServiceHelper;
  }

  @Override
  public void initialize(UniqueEmail constraintAnnotation) {
  }

  @Override
  public boolean isValid(String email, ConstraintValidatorContext context) {
    return email != null && !userServiceHelper.isEmailExists(email);
  }
}