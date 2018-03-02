package com.youmap.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youmap.entity.Point;
import com.youmap.entity.Travel;
import com.youmap.entity.User;
import com.youmap.repository.PointDAO;
import com.youmap.repository.TravelDAO;
import com.youmap.repository.UserDAO;

@Service
public class TravelCreatorService {
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	TravelDAO travelDAO;
	
	@Autowired
	PointDAO pointDAO;
	
	public String changePoint(String username, int option, String idTravel, HashMap<String, String> change) {
		User user = userDAO.findByUsername(username);
		Travel travel = travelDAO.findByCode(idTravel);
		Point point = pointDAO.findByName(change.get("code"));
		if(!user.getTravels().contains(travel) || !travel.getPoints().contains(point)) {
			//error
			return "Nie masz tutaj dostępu!";
		}
		
		switch(option) {
		//delete point
		case 1:
			travel.setHowManyPoints(travel.getHowManyPoints()-1);
			travelDAO.save(travel);
			pointDAO.delete(point);
			return "Success";
			
		//change point description
		case 2:
			point.setDescription(change.get("description"));
			pointDAO.save(point);
			return "Success";
			
		//change 'when' value for point
		case 3:
			point.setWhen(Long.parseLong(change.get("when")));
			pointDAO.save(point);
			return "Success";
			
		//change location for point
		case 4:
			point.setOx(Float.parseFloat(change.get("ox")));
			point.setOy(Float.parseFloat(change.get("oy")));
			pointDAO.save(point);
			return "Success";
		}
		
		//wrong option
		return "";
	}
	
	public String changeTravel(String username, int option, String idTravel, String what) {
		User user = userDAO.findByUsername(username);
		Travel travel = travelDAO.findByCode(idTravel);
		if(!user.getTravels().contains(travel)) {
			//error
			return "Nie masz tutaj dostępu!";
		}
		
		switch(option) {
		//change description
		case 1:
			travel.setDescription(what);
			travelDAO.save(travel);
			return "Success";
			
		//change title
		case 2:
			travel.setTitle(what);
			travelDAO.save(travel);
			return "Success";
		}
		return "";
	}
}