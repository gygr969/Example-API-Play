package models;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import validators.NombreValidator;

@Target({java.lang.annotation.ElementType.FIELD}) 
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME) 
@Constraint(validatedBy = NombreValidator.class)
public @interface Nombre 
{
	String message() default "Nombre inválido"; 
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
