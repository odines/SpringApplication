package vsg.rest.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vsg.rest.settings.AuthData;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Denis Orlov.
 */
@Service
public class AuthService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
	private static final String AUTH_URI = "https://ccadmin-z8ga.oracleoutsourcing.com/ccadminui/v1/login/";
	private static final String ENTITY_STRING = "grant_type=password&username=denis.orlov@vsgcommerce" +
			".com&password=Qwerty!234";
	private final AuthData authData;

	@Autowired
	public AuthService(AuthData authData) {
		this.authData = authData;
	}

	public boolean authenticateRequest() {
		boolean result = false;
		JerseyClient jersey = JerseyClientBuilder.createClient();
		LOGGER.info("POST REQUEST: URI = " + AUTH_URI);
		JerseyInvocation.Builder builderJersey = jersey.target(AUTH_URI).request();
		Response response = builderJersey.post(getPostEntity());
		if (response.getStatus() == 200) {
			authData.setAuthToken(getTokenFromResponse(response));
			result = true;
		}
		return result;
	}

	private Entity<String> getPostEntity() {
		return Entity.entity(ENTITY_STRING, MediaType.APPLICATION_FORM_URLENCODED);
	}

	private String getTokenFromResponse(Response response) {
		Gson gson = new Gson();
		JsonObject jsonResponse = gson.fromJson(response.readEntity(String.class), JsonObject.class);
		String token = jsonResponse.getAsJsonPrimitive("access_token").toString();
		token = token.replaceAll("\"", "");
		LOGGER.info("Access token = " + token);
		return token;
	}
}
