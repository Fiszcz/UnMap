package com.youmap.api;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youmap.entity.User;
import com.youmap.repository.UserDAO;

@RestController
@RequestMapping("/profile")
public class Profile {
	
	@Autowired
	UserDAO userDAO;
	
	@GetMapping("/get")
	public Map<String, String> newPhotoPoint(@AuthenticationPrincipal Principal principal) {
		
		User user = userDAO.findByUsername(principal.getName());
		
	    Map<String,String> map=new HashMap<String,String>();  
	    map.put("name", user.getName());
	    map.put("email", user.getEmail());
	    map.put("surname", user.getSurname());

		return map;
	}
}