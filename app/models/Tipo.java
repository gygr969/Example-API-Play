package models;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import validators.TipoValidator;

@Target({java.lang.annotation.ElementType.FIELD}) 
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME) 
@Constraint(validatedBy = TipoValidator.class)
public @interface Tipo
{
	String message() default "Tipo inválido"; 
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
