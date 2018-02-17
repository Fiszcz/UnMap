package com.youmap.api;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youmap.entity.Travel;
import com.youmap.service.ProfileService;


@RestController
@RequestMapping("/travel")
public class Travels {
	
	@Autowired
	ProfileService profileService;
	
	@GetMapping("/get/all/forUser")
	public List<Travel> getTravelsForUser(@AuthenticationPrincipal Principal principal) {
		return profileService.getTravelForUser(principal.getName());
	}
}