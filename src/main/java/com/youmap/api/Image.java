package com.youmap.api;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youmap.entity.Point;
import com.youmap.entity.Travel;
import com.youmap.repository.PointDAO;
import com.youmap.repository.TravelDAO;
import com.youmap.repository.UserDAO;


@RestController
@RequestMapping("/photo")
public class Image {
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	PointDAO pointDAO;
	
	@Autowired
	TravelDAO travelDAO;
	
	@GetMapping("/{idTravel}/{namePhoto}")
	public void getPhoto(@PathVariable String idTravel ,@PathVariable String namePhoto,HttpServletResponse response)
			throws IOException{
		
		Point point = pointDAO.findByName(namePhoto);
		Travel travel = travelDAO.findByCode(idTravel);
		if(travel == null) {
			//error
			return;
		}
		response.setContentType(point.getContentType());
		response.getOutputStream().write(point.getPhoto());
	}
}