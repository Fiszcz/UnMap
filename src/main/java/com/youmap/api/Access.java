package com.youmap.api;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youmap.entity.User;
import com.youmap.service.RegisterService;
import com.youmap.service.TravelService;

@RestController
@RequestMapping("/access")
public class Access {
	
	@Autowired
	RegisterService registerService;
	
	@Autowired
	TravelService travelService;
	
	@GetMapping("/travel/newCode/{idTravel}")
	public Map<String, String> newCodeTravel(@AuthenticationPrincipal Principal principal, @PathVariable String idTravel) {
		return travelService.newCode(principal.getName(), idTravel);
	}
	
	@PostMapping("/register")
	public String registration(@RequestBody User user) {
		return registerService.register(user);
	}
}