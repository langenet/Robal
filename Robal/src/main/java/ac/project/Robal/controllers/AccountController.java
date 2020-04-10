package ac.project.Robal.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
	
	@GetMapping("/")
	public String home() {
		return ("welcome anyone");
	}
	
	@GetMapping("/user")
	public String user() {
		return ("welcome user");
	}
	
	@GetMapping("/")
	public String admin() {
		return ("welcome admin");
	}

}
