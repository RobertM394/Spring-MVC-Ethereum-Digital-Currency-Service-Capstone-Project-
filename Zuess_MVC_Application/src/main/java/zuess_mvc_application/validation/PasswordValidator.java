package zuess_mvc_application.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator <PasswordValidConstraint, String> {
	
	public PasswordValidator() {
		
	}
	
	@Override
	public void initialize(PasswordValidConstraint constraint) {
		
	}
	
	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		System.out.println(password);
		boolean validEntry = true;
		context.disableDefaultConstraintViolation();
		if(password == null) {
			context
				.buildConstraintViolationWithTemplate("Password cannot be empty.")
				.addConstraintViolation();
			return false;
		} else {
			if(!password.matches(".*[a-z].*")) {
				context
					.buildConstraintViolationWithTemplate("Password must contain a lowercase letter.")
					.addConstraintViolation();

				validEntry = false;
			}
			if(!password.matches(".*[A-Z].*")) {
				context
					.buildConstraintViolationWithTemplate("Password must contain an uppercase letter.")
					.addConstraintViolation();
				validEntry = false;
			}
			if(!password.matches(".*[0-9].*")) {
				context
					.buildConstraintViolationWithTemplate("Password must contain a number.")
					.addConstraintViolation();
				validEntry = false;
			}
			if(!password.matches(".*[~`!@#$%^&*()_+=].*")) {
				context
					.buildConstraintViolationWithTemplate("Password must contain a special character. (~`!@#$%^&*()_+=)")
					.addConstraintViolation();
				validEntry = false;
			}
			if(password.length() < 8) {
				context
					.buildConstraintViolationWithTemplate("Password must be 8 characters or longer.")
					.addConstraintViolation();
				validEntry = false;
			}
			if(password.length() > 100){
				context
					.buildConstraintViolationWithTemplate("Password must be 100 characters or shorter.")
					.addConstraintViolation();
				validEntry = false;
			}
		}
		return validEntry;
	}
}
