package com.youmap.api;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youmap.entity.User;
import com.youmap.repository.TravelDAO;
import com.youmap.repository.UserDAO;


@RestController
@RequestMapping("/editProfile")
public class EditProfile {
	
	@Autowired
    BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	TravelDAO travelDAO;
	
	@PostMapping("/setName")
	public String setName(@AuthenticationPrincipal Principal principal, @RequestBody Map<String, String> json) {
		
		User user = userDAO.findByUsername(principal.getName());
		user.setName(json.get("name"));
		user.setSurname(json.get("surname"));
		userDAO.save(user);
		return "Pomyślnie zmieniono!";
	}
	
	@PostMapping("/changePassword")
	public String changePassword(@AuthenticationPrincipal Principal principal, @RequestBody Map<String, String> passwords) {
		
		User user = userDAO.findByUsername(principal.getName());
		String hashPassword = passwordEncoder.encode(passwords.get("oldPassword").trim());
		if(hashPassword == user.getPassword()) {
			hashPassword = passwordEncoder.encode(passwords.get("newPassword").trim());
			user.setPassword(hashPassword);
			userDAO.save(user);
			return "Pomyślnie zmieniono!";
		}
		//error
		return "Blad!";
	}
	
	@PostMapping("/changeEmail")
	public String changeEmail(@AuthenticationPrincipal Principal principal, @RequestBody String email) {
		
		User user = userDAO.findByUsername(principal.getName());
		user.setEmail(email);
		userDAO.save(user);
		return "Pomyślnie zmieniono!";
	}
	
	@PostMapping("/deleteAccount")
	public String deleteAccount(@AuthenticationPrincipal Principal principal, @RequestBody String password) {
		User user = userDAO.findByUsername(principal.getName());
		if(passwordEncoder.encode(password.trim()) == user.getPassword()) {
			travelDAO.delete(user.getTravels());
			userDAO.delete(user);
			return "Usunięto!";
		}
		return "Coś poszło nie tak!";
	}
}