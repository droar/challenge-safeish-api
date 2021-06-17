package com.droar.safeish.infrastructure.rest.validation;

import java.util.Arrays;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

/**
 * This class is a simple password constraint validator. It will serve to make sure the safe box
 * password is strong enough
 *
 * @author droar
 */
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

  /**
   * Initialize.
   *
   * @param arg0 the arg 0
   */
  @Override
  public void initialize(final ValidPassword arg0) {
    // No functionality needed
  }

  /**
   * Checks if password is valid.
   *
   * @param password the password
   * @param context the context
   * @return true, if is valid
   */
  @Override
  public boolean isValid(final String password, final ConstraintValidatorContext context) {
    final PasswordValidator validator = new PasswordValidator(Arrays.asList(
        // length between 8 and 16 characters
        new LengthRule(6, 16),
        // at least one upper-case character
        new CharacterRule(EnglishCharacterData.UpperCase, 1),
        // at least one lower-case character
        new CharacterRule(EnglishCharacterData.LowerCase, 1),
        // at least one digit character
        new CharacterRule(EnglishCharacterData.Digit, 1),
        // at least one symbol (special character)
        new CharacterRule(EnglishCharacterData.Special, 1),
        // no whitespace
        new WhitespaceRule(),
        // rejects passwords that contain a sequence of >= 5 characters alphabetical (e.g. abcdef)
        new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false),
        // rejects passwords that contain a sequence of >= 5 characters numerical (e.g. 12345)
        new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false)));

    final RuleResult result = validator.validate(new PasswordData(password));
    if (result.isValid()) {
      return true;
    }
    context.disableDefaultConstraintViolation();

    List<String> messages = validator.getMessages(result);
    String messageTemplate = String.join(",", messages);
    context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation().disableDefaultConstraintViolation();

    return false;
  }
}
