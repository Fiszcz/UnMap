package com.youmap.api;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youmap.service.ProfileService;

@RestController
@RequestMapping("/profile")
public class Profile {
	
	@Autowired
	ProfileService profileService;
	
	@GetMapping("/get")
	public Map<String, String> getProfile(@AuthenticationPrincipal Principal principal) {
		return profileService.getProfile(principal.getName());
	}
}