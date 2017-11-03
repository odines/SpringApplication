package vsg.rest.service;

import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vsg.rest.settings.AuthData;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Denis Orlov.
 */
@Service
public class ExportAssetsService {

	private final AuthData authData;
	private static final Logger LOGGER = LoggerFactory.getLogger(ExportAssetsService.class);
	private static final String URI_EXPORT = "https://ccadmin-z8ga.oracleoutsourcing.com/ccadminui/v1/asset/export";

	@Autowired
	public ExportAssetsService(AuthData pAuthData) {
		authData = pAuthData;
	}

	public String getExportedFile() {
		JerseyClient client = JerseyClientBuilder.createClient();
		LOGGER.info("GET REQUEST: " + URI_EXPORT);
		JerseyInvocation.Builder builder = client.target(URI_EXPORT).queryParam("type",
				"Product")
				.request(MediaType
						.APPLICATION_FORM_URLENCODED).header("Authorization", "Bearer " + authData.getAuthToken()).header
						("Accept-Encoding", "gzip").header("Content-Encoding", "deflate");
		Response response = builder.get();
		String result = null;
		if (response.getStatus() == 200) {
			result = response.readEntity(String.class);
			LOGGER.info("EXPORT COMPLETE. ExportStatus:" + response.getStatus());
		} else {
			LOGGER.error(response.getStatusInfo().getReasonPhrase());
		}
		return result;
	}
}
