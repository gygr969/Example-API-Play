package models;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import validators.NombreTareaValidator;

@Target({java.lang.annotation.ElementType.FIELD}) 
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME) 
@Constraint(validatedBy = NombreTareaValidator.class)
public @interface NombreTarea 
{
	String message() default "Nombre Tarea inválido"; 
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}