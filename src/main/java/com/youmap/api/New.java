package com.youmap.api;

import java.security.Principal;
import java.text.ParseException;
import java.util.Collections;
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
import com.youmap.service.ImageService;
import com.youmap.service.TravelService;

@RestController
@RequestMapping("/new")
public class New {
	
	@Autowired
	TravelService travelService;
	
	@Autowired
	ImageService imageService;
	
	@GetMapping(value="/travel", produces="application/json")
	public Map<String, String> newTravel(@AuthenticationPrincipal Principal principal) {
		
		return Collections.singletonMap("idTravel", travelService.newTravel(principal.getName()));
	}
	
	@PostMapping("/photo/{idTravel}")
	public Point newPhotoPoint(@AuthenticationPrincipal Principal principal,@RequestBody Point point
			,@PathVariable String idTravel) throws ParseException {
		
		return imageService.newPhotoPoint(principal.getName(), idTravel, point);
	}
	
	
/*	@PostMapping("/point/{idTravel}")
	public Point newPoint(@AuthenticationPrincipal Principal principal,@RequestBody Point point
			,@PathVariable String idTravel) {
		
		
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
		
		String codeOfPoint = Random.randomString(30);
		Travel travel = travelDAO.findByCode(idTravel);
		List<Point> points = travel.getPoints();
		points.add(point);
		travel.setHowManyPoints(travel.getHowManyPoints()+1);
		travel.setPoints(points);
		travelDAO.save(travel);
		
		point.setPhoto(null);
		return point;
	}*/
}