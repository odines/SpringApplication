package vsg.rest.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Denis Orlov.
 */
@RestController
public class AuthController {
	@RequestMapping("/secure-sample/helloWorld")
	@PreAuthorize("hasRole('SECURE_USER')")
	public String saySecureHello() {
		return "SPRING SECURITY HELLO!";
	}

}
