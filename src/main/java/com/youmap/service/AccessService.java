package com.youmap.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import com.google.common.collect.ImmutableMap;
import com.youmap.entity.Role;
import com.youmap.entity.User;
import com.youmap.repository.UserDAO;
import com.youmap.validation.UserValidator;

@Service
public class AccessService {
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder; 

    @Autowired
    UserDAO userDAO; 
    
    @Autowired
    UserValidator userValidator;
    
    @Transactional
    public ResponseEntity<Object> register(User user) {
    	BeanPropertyBindingResult errors = new BeanPropertyBindingResult(user, "user");
    	userValidator.validateRegister(user, errors);
    	if(errors.hasErrors()) {
    		List<ImmutableMap<String, String>> registrationFieldErrors = errors
    				.getFieldErrors()
    				.stream()
    				.map(fieldError -> ImmutableMap.of("code", fieldError.getCode(), "message", fieldError.getDefaultMessage()))
    				.collect(Collectors.toList());
    		
    		//return new ResponseEntity<>(registrationFieldErrors, HttpStatus.CONFLICT);
    		return new ResponseEntity<>(registrationFieldErrors, HttpStatus.FORBIDDEN);
    	}
    
    	String hashPassword = passwordEncoder.encode(user.getPassword().trim());
    	user.setPassword(hashPassword);
    	user.setRole(Role.ROLE_USER);
    	user.setEnabled(true);
    	userDAO.save(user);
    	return new ResponseEntity<>(ImmutableMap.of("code", "register.done", "message", "Registration completed. You can sign in now!"), HttpStatus.CREATED);
    }

	@Transactional
    public String changePassword(String username, String oldPassword, String newPassword) {
    	User user = userDAO.findByUsername(username);
		String hashPassword = passwordEncoder.encode(oldPassword);
		if(hashPassword == user.getPassword()) {
			hashPassword = passwordEncoder.encode(newPassword);
			user.setPassword(hashPassword);
			userDAO.save(user);
			return "Pomyślnie zmieniono!";
		}
		//error
		return "Blad!";
    }
}