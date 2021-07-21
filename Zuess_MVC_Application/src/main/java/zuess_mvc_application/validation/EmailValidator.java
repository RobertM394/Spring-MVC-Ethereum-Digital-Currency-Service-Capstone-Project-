package zuess_mvc_application.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zuess_mvc_application.domain.User;
import zuess_mvc_application.repository.UserRepository;


@Service
public class EmailValidator implements ConstraintValidator <EmailUniqueConstraint, String> {
	
	@Autowired
	UserRepository userRepo;
	
	public EmailValidator() {
	}
	
	public EmailValidator(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	
	@Override
	public void initialize(EmailUniqueConstraint constraint) {
	}
	
	//This gets checked outside of registration and when doing so userRepo is nulled for some reason.
	//the check prevents it from crashing everything outside of initial validation.
	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		if(this.userRepo != null) {
			User tempUser = userRepo.findByEmail(email);
			if (tempUser == null) {
				return true;
			} else {
				return false;
			}
		}
		return true;
    }
}
