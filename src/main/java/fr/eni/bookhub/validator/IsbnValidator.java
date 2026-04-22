package fr.eni.bookhub.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// ConstraintValidator indique nom de l'annotation et type du champ
public class IsbnValidator implements ConstraintValidator<Isbn, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isBlank()) return true;
        String isbn = value.trim().replace("-", "");
        return isbn.matches(("\\d{10}|\\d{13}|\\d{9}X"));

    }
}
