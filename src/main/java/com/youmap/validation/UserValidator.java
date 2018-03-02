package com.youmap.validation;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.youmap.entity.User;
import com.youmap.repository.UserDAO;

@Component
public class UserValidator {
	
	@Autowired
	UserDAO userDAO;
	
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	
    
    public void validateUsername(String username, Errors errors) {
    	if(username.length() > 3 ) {
    		if(userDAO.findByUsername(username) != null) {
    			errors.rejectValue("username", "username.repeated", "This login is in use!");
    		}
    	}
    	else {
			errors.rejectValue("username", "username.syntax", "Invalid syntax of login");
		}
    }
    
    public void validateEmail(String email, Errors errors) {
    	if(VALID_EMAIL_ADDRESS_REGEX.matcher(email).matches()) {
    		if(userDAO.findByEmail(email) != null)
    		{
    			errors.rejectValue("email", "email.repeated", "This email is in use!");
    		}
    	}
    	else {
    		errors.rejectValue("email", "email.syntax", "Invalid syntax of email");
    	}
    }
    
    public void validateRegister(User user, Errors errors) {
    	validateUsername(user.getUsername(), errors);
    	validateEmail(user.getEmail(), errors);
    }
}