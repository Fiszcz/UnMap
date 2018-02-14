package com.youmap.api;

import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youmap.entity.Point;
import com.youmap.entity.Travel;
import com.youmap.entity.User;
import com.youmap.repository.TravelDAO;
import com.youmap.repository.UserDAO;

import functions.Random;

@RestController
@RequestMapping("/new")
public class New {
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	TravelDAO travelDAO;
	
	@GetMapping(value="/travel", produces="application/json")
	public Map<String, String> newTravel(@AuthenticationPrincipal Principal principal) {
		
		User user = userDAO.findByUsername(principal.getName());
		Travel travel = new Travel();
		String codeOfTravel = Random.randomString(40);
		travel.setCode(codeOfTravel);
		travel.setCreated(new Date());
		List<Travel> travels = user.getTravels();
		travels.add(travel);
		user.setTravels(travels);
		userDAO.save(user);
		return Collections.singletonMap("idTravel", codeOfTravel);
	}
	
	@PostMapping("/photo/{idTravel}")
	public Point newPhotoPoint(@AuthenticationPrincipal Principal principal,@RequestBody Point point
			,@PathVariable String idTravel) throws ParseException {
		
		String codeOfPhoto = Random.randomString(20);
		
		javaxt.io.Image image = new javaxt.io.Image(point.getPhoto());
		java.util.HashMap<Integer, Object> exif = image.getExifTags();
		Integer height = image.getHeight();
		Integer width = image.getWidth();
		double[] coord = image.getGPSCoordinate();
		String dateString = (String) exif.get(0x0132);
		DateFormat format = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		Date date = format.parse(dateString);
		Long when = (Long) date.getTime();
		
		point.setHeight(height);
		point.setWidth(width);
		point.setWhen(when);
		point.setName(codeOfPhoto);
		point.setAdded(new Date());
		point.setOx((float) coord[0]);
		point.setOy((float) coord[1]);
		Travel travel = travelDAO.findByCode(idTravel); 
		//To do: check if travel != null
		List<Point> points = travel.getPoints();
		points.add(point);
		travel.setHowManyPoints(travel.getHowManyPoints()+1);
		travel.setPoints(points);
		travelDAO.save(travel);
		
		point.setPhoto(null);
		return point;
	}
	
	@PostMapping("/point/{idTravel}")
	public Point newPoint(@AuthenticationPrincipal Principal principal,@RequestBody Point point
			,@PathVariable String idTravel) {
		
		String codeOfPoint = Random.randomString(20);
		
		if(point.getPhoto() != null) {
			javaxt.io.Image image = new javaxt.io.Image(point.getPhoto());
			java.util.HashMap<Integer, Object> exif = image.getExifTags();
			double[] coord = image.getGPSCoordinate();
			Date date = (Date) exif.get(0x0132);
			Long when = (Long) date.getTime();

			point.setWhen(when);
			point.setName(codeOfPoint);
			point.setAdded(new Date());
			point.setOx((float) coord[0]);
			point.setOy((float) coord[1]);
		}
		
		Travel travel = travelDAO.findByCode(idTravel);
		List<Point> points = travel.getPoints();
		points.add(point);
		travel.setHowManyPoints(travel.getHowManyPoints()+1);
		travel.setPoints(points);
		travelDAO.save(travel);
		
		point.setPhoto(null);
		return point;
	}
}