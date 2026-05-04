package fr.eni.bookhub.validator;

import fr.eni.bookhub.controller.dto.InscriptionDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MotDePasseIdentiquesValidator
        implements ConstraintValidator<MotDePasseIdentiques, InscriptionDTO> {

    @Override
    public boolean isValid(InscriptionDTO dto, ConstraintValidatorContext context) {
        if (dto.password() == null || dto.confirmPassword() == null) return true;
        return dto.password().equals(dto.confirmPassword());
    }
}