package com.youmap.api;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youmap.entity.Travel;
import com.youmap.entity.User;
import com.youmap.repository.TravelDAO;
import com.youmap.repository.UserDAO;


@RestController
@RequestMapping("/travel")
public class Travels {
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	TravelDAO travelDAO;
	
	@GetMapping("/get/all/forUser")
	public List<Travel> getTravelsForUser(@AuthenticationPrincipal Principal principal) {
		
		User user = userDAO.findByUsername(principal.getName());
		List<Travel> travels = user.getTravels();
		return travels;
	}
}