package fr.eni.bookhub.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//Pour que Validation prenne en compte la contrainte custom
// Qd il voit @Isbn il apelle isValid sur le champ
@Constraint(validatedBy = IsbnValidator.class)
//contrainte sur un champ, pas une méthode, un paramètre ni une classe
@Target(ElementType.FIELD)
//annotation dispo jusqu'au runtime
@Retention(RetentionPolicy.RUNTIME)
public @interface Isbn {
    //méthode qui donne un message qd validation échoue
    String message() default "ISBN invalide (10 ou 13 chiffres attendus)";
    Class<?>[] groups() default {};
    Class<?extends Payload>[] payload() default {};
}
