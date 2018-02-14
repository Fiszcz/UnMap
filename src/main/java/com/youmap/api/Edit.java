package com.youmap.api;

import java.security.Principal;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youmap.entity.Point;
import com.youmap.entity.Travel;
import com.youmap.entity.User;
import com.youmap.repository.PointDAO;
import com.youmap.repository.TravelDAO;
import com.youmap.repository.UserDAO;

@RestController
@RequestMapping("/edit")
public class Edit {
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	PointDAO pointDAO;
	
	@Autowired
	TravelDAO travelDAO;
	
	@Transactional
	@PostMapping("/travel/points/delete/{idTravel}")
	public String deletePoint(@AuthenticationPrincipal Principal principal, @RequestBody String codeOfPoint
			,@PathVariable String idTravel) {
		
		User user = userDAO.findByUsername(principal.getName());
		Travel travel = travelDAO.findByCode(idTravel);
		if(!user.getTravels().contains(travel)) {
			//error
			return "Nie masz tutaj dostępu!";
		}
		travel.setHowManyPoints(travel.getHowManyPoints()-1);
		travelDAO.save(travel);
		pointDAO.deleteByName(codeOfPoint);
		
		return "Success";
	}
	
	@PostMapping("/travel/points/description/{idTravel}")
	public String changePointDescription(@AuthenticationPrincipal Principal principal,@RequestBody HashMap<String, String> change
			,@PathVariable String idTravel) {
		
		User user = userDAO.findByUsername(principal.getName());
		Travel travel = travelDAO.findByCode(idTravel);
		if(!user.getTravels().contains(travel)) {
			//error
			return "Nie masz tutaj dostępu!";
		}
		Point point = pointDAO.findByName(change.get("code"));
		if(point != null) {
			point.setDescription(change.get("description"));
			pointDAO.save(point);
			return "Success";
		}
		return "Punkt który próbujesz zmienić nie istnieje";
	}
	
	@PostMapping("/travel/points/when/{idTravel}")
	public String changePointWhen(@AuthenticationPrincipal Principal principal,@RequestBody HashMap<String, String> change
			,@PathVariable String idTravel) {
		
		User user = userDAO.findByUsername(principal.getName());
		Travel travel = travelDAO.findByCode(idTravel);
		if(!user.getTravels().contains(travel)) {
			//error
			return "Nie masz tutaj dostępu!";
		}
		Point point = pointDAO.findByName((String) change.get("code"));
		if(point != null) {
			point.setWhen(Long.parseLong(change.get("when")));
			pointDAO.save(point);
			return "Success";
		}
		return "Punkt który próbujesz zmienić nie istnieje";
	}
	
	@PostMapping("/travel/points/location/{idTravel}")
	public String changePointLocation(@AuthenticationPrincipal Principal principal,@RequestBody HashMap<String, String> change
			,@PathVariable String idTravel) {
		
		User user = userDAO.findByUsername(principal.getName());
		Travel travel = travelDAO.findByCode(idTravel);
		if(!user.getTravels().contains(travel)) {
			//error
			return "Nie masz tutaj dostępu!";
		}
		Point point = pointDAO.findByName(change.get("code"));
		if(point != null) {
			point.setOx(Float.parseFloat(change.get("ox")));
			point.setOy(Float.parseFloat(change.get("oy")));
			pointDAO.save(point);
			return "Success";
		}
		return "Punkt który próbujesz zmienić nie istnieje.";
	}
	
	@PostMapping("/travel/description/{idTravel}")
	public String changeDescription(@AuthenticationPrincipal Principal principal,@RequestBody String description
			,@PathVariable String idTravel) {
		
		User user = userDAO.findByUsername(principal.getName());
		Travel travel = travelDAO.findByCode(idTravel);
		if(!user.getTravels().contains(travel)) {
			//error
			return "Nie masz tutaj dostępu!";
		}
		travel.setDescription(description);
		travelDAO.save(travel);
		return "Success";
	}
	
	@PostMapping("/travel/title/{idTravel}")
	public String changeTitle(@AuthenticationPrincipal Principal principal,@RequestBody String title
			,@PathVariable String idTravel) {
		
		User user = userDAO.findByUsername(principal.getName());
		Travel travel = travelDAO.findByCode(idTravel);
		if(!user.getTravels().contains(travel)) {
			//error
			return "Nie masz tutaj dostępu!";
		}
		travel.setTitle(title);
		travelDAO.save(travel);
		return "Success";
	}
}