package com.youmap.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youmap.entity.Travel;
import com.youmap.entity.User;
import com.youmap.repository.TravelDAO;
import com.youmap.repository.UserDAO;

import functions.Random;

@Service 
public class TravelService {
	
	@Autowired
	TravelDAO travelDAO;
	
	@Autowired
	UserDAO userDAO; 
	
	public Map<String, String> newCode(String username, String idTravel) {
		User user = userDAO.findByUsername(username);
		Travel travel = travelDAO.findByCode(idTravel);
		if(!user.getTravels().contains(travel)) {
			//error
		}
		String codeOfTravel = Random.randomString(20);
		travel.setCode(codeOfTravel);
		travelDAO.save(travel);
		return Collections.singletonMap("code", codeOfTravel);
	}
	
	public String newTravel(String username) {
		User user = userDAO.findByUsername(username);
		Travel travel = new Travel();
		String codeOfTravel = Random.randomString(20);
		travel.setCode(codeOfTravel);
		travel.setCreated(new Date());
		List<Travel> travels = user.getTravels();
		travels.add(travel);
		user.setTravels(travels);
		userDAO.save(user);
		return codeOfTravel; 
	}
}