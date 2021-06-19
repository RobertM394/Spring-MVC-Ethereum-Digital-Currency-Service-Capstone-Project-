package zuess_mvc_application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

import zuess_mvc_application.services.UserService;
import zuess_mvc_application.domain.*;

@Controller
public class ZuessWebController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	HttpSession session;
	
	/***HTTP Routes***/
	@GetMapping("/registration")
	public String getUserRegistrationForm() {
		return "new_user_registration";
	}
	
	@PostMapping("/submitNewUserRegistration")
	public String persistNewUser(Model model, HttpSession session,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam("first_name") String first_name,
			@RequestParam("last_name") String last_name
			) {
			
			model.addAttribute("first_name", first_name);
			model.addAttribute("last_name", last_name);
			
			//persist new user to database
			User user = new User();
			user.setEmail(email);
			user.setPassword(password);
			user.setFirst_name(first_name);
			user.setLast_name(last_name);
			userService.persistNewUser(user);
		
		return "standard_user_account";
	}
	
}
