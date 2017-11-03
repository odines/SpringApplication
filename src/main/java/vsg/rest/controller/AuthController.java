package vsg.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vsg.rest.service.AuthService;

import javax.ws.rs.core.MediaType;

/**
 * Created by Denis Orlov.
 */
@RestController
public class AuthController {

	private final AuthService authenticationService;

	@Autowired
	public AuthController(AuthService pAuthenticationService) {
		authenticationService = pAuthenticationService;
	}

	@RequestMapping(value = "/get_token", produces = MediaType.APPLICATION_JSON)
	public boolean getAuthToken() {
		return authenticationService.authenticateRequest();
	}

	@RequestMapping("/secure-sample/helloWorld")
	@PreAuthorize("hasRole('SECURE_USER')")
	public String saySecureHello() {
		return "SPRING SECURITY HELLO!";
	}


}
