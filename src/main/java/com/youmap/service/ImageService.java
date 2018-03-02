package com.youmap.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youmap.entity.Point;
import com.youmap.entity.Travel;
import com.youmap.repository.TravelDAO;

import functions.Random;

@Service
public class ImageService {
	
	@Autowired
	TravelDAO travelDAO;
	
	public Point newPhotoPoint (String username, String idTravel, Point point) throws ParseException {
		
		String codeOfPhoto = Random.randomString(30);
		
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
}