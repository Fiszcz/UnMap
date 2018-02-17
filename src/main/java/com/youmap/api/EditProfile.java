package com.youmap.api;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youmap.service.AccessService;
import com.youmap.service.EditProfileService;
import com.youmap.service.ProfileService;


@RestController
@RequestMapping("/editProfile")
public class EditProfile {
	
	@Autowired
    BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	EditProfileService editProfileService;
	
	@Autowired
	AccessService accessService;
	
	@Autowired
	ProfileService profileService;
	
	@PostMapping("/setName")
	public String setName(@AuthenticationPrincipal Principal principal, @RequestBody Map<String, String> json) {
		return editProfileService.setName(principal.getName(), json.get("name"), json.get("surname"));
	}
	
	@PostMapping("/changePassword")
	public String changePassword(@AuthenticationPrincipal Principal principal, @RequestBody Map<String, String> passwords) {
		return accessService.changePassword(principal.getName(), passwords.get("oldPassword").trim(), passwords.get("newPassword").trim());
	}
	
	@PostMapping("/changeEmail")
	public String changeEmail(@AuthenticationPrincipal Principal principal, @RequestBody String email) {
		return editProfileService.setEmail(principal.getName(), email);
	}
	
	@PostMapping("/deleteAccount")
	public String deleteAccount(@AuthenticationPrincipal Principal principal, @RequestBody String password) {
		return profileService.deleteAccount(principal.getName(), password);
	}
}