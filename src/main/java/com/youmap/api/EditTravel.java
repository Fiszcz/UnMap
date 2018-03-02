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

import com.youmap.service.TravelCreatorService;

@RestController
@RequestMapping("/edit")
public class EditTravel {
	
	@Autowired
	TravelCreatorService travelCreatorService;
	
	@Transactional
	@PostMapping("/travel/points/delete/{idTravel}")
	public String deletePoint(@AuthenticationPrincipal Principal principal, @RequestBody String codeOfPoint
			,@PathVariable String idTravel) {
		return travelCreatorService.changePoint(principal.getName(), 1, idTravel, new HashMap<String, String>() {
			{
				put("code", codeOfPoint);
			}
		});
	}
	
	@PostMapping("/travel/points/description/{idTravel}")
	public String changePointDescription(@AuthenticationPrincipal Principal principal,@RequestBody HashMap<String, String> change
			,@PathVariable String idTravel) {
		return travelCreatorService.changePoint(principal.getName(), 2, idTravel, change);
	}
	
	@PostMapping("/travel/points/when/{idTravel}")
	public String changePointWhen(@AuthenticationPrincipal Principal principal,@RequestBody HashMap<String, String> change
			,@PathVariable String idTravel) {
		return travelCreatorService.changePoint(principal.getName(), 3, idTravel, change);
	}
	
	@PostMapping("/travel/points/location/{idTravel}")
	public String changePointLocation(@AuthenticationPrincipal Principal principal,@RequestBody HashMap<String, String> change
			,@PathVariable String idTravel) {
		return travelCreatorService.changePoint(principal.getName(), 4, idTravel, change);
	}
	
	@PostMapping("/travel/description/{idTravel}")
	public String changeDescription(@AuthenticationPrincipal Principal principal,@RequestBody String description
			,@PathVariable String idTravel) {
		return travelCreatorService.changeTravel(principal.getName(), 1, idTravel, description);
	}
	
	@PostMapping("/travel/title/{idTravel}")
	public String changeTitle(@AuthenticationPrincipal Principal principal,@RequestBody String title
			,@PathVariable String idTravel) {
		return travelCreatorService.changeTravel(principal.getName(), 2, idTravel, title);
	}
}