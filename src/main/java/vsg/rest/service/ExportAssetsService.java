package vsg.rest.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vsg.rest.settings.AuthData;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Denis Orlov.
 */
@Service
public class ExportAssetsService {

	private final AuthData authData;
	private static final Logger LOGGER = LoggerFactory.getLogger(ExportAssetsService.class);
	private static final String URI_EXPORT = "https://ccadmin-z8ga.oracleoutsourcing.com/ccadminui/v1/asset/export";
	private static final String URI_COLLECTIONS = "https://ccadmin-z8ga.oracleoutsourcing.com/ccadminui/v1/collections";

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

	public Map<String, String> getCollections() {
		JerseyClient client = JerseyClientBuilder.createClient();
		WebTarget target = client.target("");
		Map<String, String> result = new HashMap<>();
		LOGGER.info("GET REQUEST: " + URI_COLLECTIONS);
		JerseyInvocation.Builder builder = client.target(URI_COLLECTIONS).request(MediaType
				.APPLICATION_JSON).header("Authorization", "Bearer " + authData.getAuthToken());
		Response response = builder.get();
		if (response.getStatus() == 200) {
			LOGGER.info("EXPORT COMPLETE. ExportStatus:" + response.getStatus());
			result = convertJSONToCollections(response.readEntity(String.class));
		} else {
			LOGGER.error(response.getStatusInfo().getReasonPhrase());
		}
		return result;
	}

	private Map<String, String> convertJSONToCollections(String inputJson) {
		Gson gson = new Gson();
		JsonObject json = gson.fromJson(inputJson, JsonObject.class);
		Map<String, String> collectionMap = new HashMap<>();
		if (!json.isJsonNull()) {
			JsonArray collectionItems = json.getAsJsonArray("items");
			if (!collectionItems.isJsonNull()) {
				for (int index = 0; index < 5; index++) {
					JsonObject categoryJson = (JsonObject) collectionItems.get(index);
					collectionMap.put(categoryJson.getAsJsonPrimitive("id").toString(), categoryJson.getAsJsonPrimitive
							("displayName").toString());

				}
			}
		}
		return collectionMap;
	}


	public String getProducts() {


		JerseyClient client = JerseyClientBuilder.createClient();
		Map<String, String> categoriesMap = getCollections();


		for (Map.Entry entry : categoriesMap.entrySet()) {


			entry.getKey();
		}

		return "";
	}


}
