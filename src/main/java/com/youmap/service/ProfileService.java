package com.youmap.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.youmap.entity.Travel;
import com.youmap.entity.User;
import com.youmap.repository.TravelDAO;
import com.youmap.repository.UserDAO;

@Service
public class ProfileService {
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder; 
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	TravelDAO travelDAO;
	
	public String deleteAccount(String username, String password) {
		
		User user = userDAO.findByUsername(username);
		if(passwordEncoder.encode(password.trim()) == user.getPassword()) {
			travelDAO.delete(user.getTravels());
			userDAO.delete(user);
			return "Usunięto!";
		}
		return "Coś poszło nie tak!";
	}
	
	
	public Map<String, String> getProfile(String username){
		
		User user = userDAO.findByUsername(username);
		
	    Map<String,String> map=new HashMap<String,String>(); 
	    map.put("name", user.getName());
	    map.put("email", user.getEmail());
	    map.put("surname", user.getSurname());

		return map;
	}
	
	
	public List<Travel> getTravelForUser(String username){
		
		User user = userDAO.findByUsername(username);
		List<Travel> travels = user.getTravels();
		return travels;
	}
}