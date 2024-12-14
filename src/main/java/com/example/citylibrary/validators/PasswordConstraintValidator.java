package com.example.citylibrary.validators;

import com.example.citylibrary.constraints.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword arg0) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        // this sets all the rules a password must follow, using the passay library
        PasswordValidator validator = new PasswordValidator(
                // length between 8 and 30 characters
                new LengthRule(8, 30),
                // at least one upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                // at least on lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                // at least one digit character
                new CharacterRule(EnglishCharacterData.Digit, 1),
                // at least one symbol
                new CharacterRule(EnglishCharacterData.Special, 1),

                // no whitespace
                new WhitespaceRule(),

                // define a few illegal sequences
                new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false),
                new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false),
                new IllegalSequenceRule(EnglishSequenceData.USQwerty, 5, false)
        );

        // TODO: possibly look into better error handling for this?
        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        } else {
            for (String msg : validator.getMessages(result)) {
                System.out.println(msg);
            }
            return false;
        }

    }
}
