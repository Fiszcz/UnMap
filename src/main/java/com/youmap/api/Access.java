package com.youmap.api;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youmap.entity.Role;
import com.youmap.entity.Travel;
import com.youmap.entity.User;
import com.youmap.repository.TravelDAO;
import com.youmap.repository.UserDAO;

import functions.Random;

@RestController
@RequestMapping("/access")
public class Access {
	
	@Autowired
    BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	TravelDAO travelDAO;
	
	@GetMapping("/travel/newCode/{idTravel}")
	public Map<String, String> newPhotoPoint(@AuthenticationPrincipal Principal principal, @PathVariable String idTravel) {
		
		User user = userDAO.findByUsername(principal.getName());
		Travel travel = travelDAO.findByCode(idTravel);
		if(!user.getTravels().contains(travel)) {
			//error
		}
		String codeOfTravel = Random.randomString(40);
		travel.setCode(codeOfTravel);
		travelDAO.save(travel);
		return Collections.singletonMap("code", codeOfTravel);
	}
	
	@PostMapping("/register")
	public String registration(@RequestBody User user) {
		if(user.getUsername() == null || user.getPassword() == null)
			return "Nie wypełnione pola!";
//            throw new NotAllMandatoryAttributesException();
        else if(userDAO.findByUsername((String) user.getUsername()) != null)
            return "Taki login już istnieje :(";
		String hashPassword = passwordEncoder.encode(user.getPassword().trim());
		user.setPassword(hashPassword);
		user.setRole(Role.ROLE_USER);
		user.setEnabled(true);
		userDAO.save(user);
		return "Możesz się już zalogować!";
	}
}