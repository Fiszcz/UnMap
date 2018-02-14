package com.youmap.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youmap.entity.Travel;
import com.youmap.repository.TravelDAO;
import com.youmap.repository.UserDAO;


@RestController
@RequestMapping("/view")
public class View {
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	TravelDAO travelDAO;
	
	@GetMapping("/travel/{idTravel}")
	public Travel viewTravel(@PathVariable String idTravel) {
		
		return travelDAO.findByCode(idTravel);
	}
}