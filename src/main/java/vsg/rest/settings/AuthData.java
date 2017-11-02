package vsg.rest.settings;

import org.springframework.stereotype.Component;

/**
 * Created by Denis Orlov.
 */
@Component
public class AuthData {
	private String authToken;

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String pAuthToken) {
		authToken = pAuthToken;
	}
}
