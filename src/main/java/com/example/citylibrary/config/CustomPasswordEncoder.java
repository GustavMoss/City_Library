package com.example.citylibrary.config;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
@Configuration
public class CustomPasswordEncoder implements PasswordEncoder {

    // Lösenordskryptering
    public PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    // Valideringsobjekt, används vid validering av lösenord och för att leta efter fel
    public Validator validation = Validation.buildDefaultValidatorFactory().getValidator();

    // Metod för att validera lösenord via parametern
    private void validatePassword(String password) {
        // Tar in lösenordsvalideringsklassen (se nedanför)
        PasswordValidator passwordValidator = new PasswordValidator(password);

        // En lista som samlar in alla fel på <password>
        Set<ConstraintViolation<PasswordValidator>> constraintViolations = validation.validate(passwordValidator);

        // Om Set-listan är INTE tom, throw en ConstraintViolationException som returnerar alla fel
        if (!constraintViolations.isEmpty()) {
            StringBuilder errors = new StringBuilder();
            for (ConstraintViolation<PasswordValidator> constraintViolation : constraintViolations) {
                errors.append(constraintViolation.getMessage()).append("\n");
            }
            throw new ConstraintViolationException(errors.toString().trim(), constraintViolations);
        }

    }

    @Override
    // Metod för att kryptera klartext-lösenorden, validerar lösenordet först
    public String encode(CharSequence rawPassword) {
        validatePassword(rawPassword.toString());
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    // Metod som jämför klartext-lösenordet med ett krypterat lösenord
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Getter
    // Lösenordsklass med valideringannoteringar
    // Varning om att detta kunde vara en record istället?
    public static class PasswordValidator {
        @NotBlank(message = "Password can not be empty")
        @NotNull(message = "Password can not be null")
        @Pattern(regexp = "^[a-zA-Z0-9]*$",
                message = "Password can not contain any special symbols, only a-z, A-Z and 0-9") // Tillåter bara a-z, A-Z och 0-9
        @Size(min = 8, message = "Password must be at least 8 characters long")
        private final String password;

        public PasswordValidator(String password) {
            this.password = password;
        }
    }
}
