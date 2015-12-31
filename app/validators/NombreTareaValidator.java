package validators;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintValidator;

import play.data.validation.Constraints;
import play.libs.F;
import play.libs.F.Tuple;

public class NombreTareaValidator extends Constraints.Validator<String> implements ConstraintValidator<Annotation, String>
{
	@Override
	public Tuple<String, Object[]> getErrorMessageKey() 
	{ 
		return new F.Tuple<String, Object[]>("Este nombre de tarea no es valido", new Object[]{""}); 
	}
	@Override
	public boolean isValid(String value) 
	{
		return !value.equals(null);
	}
	@Override
	public void initialize(Annotation arg0) 
	{
		// TODO Auto-generated method stub
		
	} 
}
