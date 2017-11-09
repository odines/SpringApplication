package vsg.rest.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyInvocation;
import org.glassfish.jersey.internal.guava.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vsg.rest.settings.AuthData;
import vsg.rest.task.GetProductsCallable;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

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

	public Map<String, String> getCategories() {
		JerseyClient client = JerseyClientBuilder.createClient();
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
					collectionMap.put(categoryJson.getAsJsonPrimitive("id").toString().replaceAll("\"", ""), categoryJson
							.getAsJsonPrimitive
									("displayName").toString());

				}
			}
		}
		return collectionMap;
	}


	public JsonObject getProducts() {
		ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("getProducts-%d").build();
		ExecutorService threadPool = Executors.newFixedThreadPool(5, factory);
		Map<String, String> categoriesMap = getCategories();
		List<Callable<String>> tasksList = new ArrayList<>();
		for (Map.Entry entry : categoriesMap.entrySet()) {
			String categoryId = (String) entry.getKey();
			tasksList.add(new GetProductsCallable(categoryId, authData.getAuthToken()));
		}
		List<Future<String>> resultList = null;
		JsonObject resultProductsObject = null;
		try {
			resultList = threadPool.invokeAll(tasksList);
		} catch (InterruptedException pE) {
			LOGGER.error("Exception in ThreadPool: e = " + pE.getMessage());
		} finally {
			threadPool.shutdown();
		}
		resultProductsObject = aggregateProducts(resultList);

		return resultProductsObject == null ? new JsonObject() : resultProductsObject;
	}

	private JsonObject aggregateProducts(List<Future<String>> futureList) {
		JsonArray resultProductsArray = new JsonArray();
		for (Future<String> future : futureList) {
			String productsJson = null;
			try {
				productsJson = future.get();
			} catch (InterruptedException | ExecutionException pE) {
				LOGGER.error("Exception in aggregateProducts(List<Future<String>> futureList) method. Exception = " + pE
						.getMessage());
			}
			resultProductsArray.addAll(getProductsFromJson(productsJson));
		}
		JsonObject result = new JsonObject();
		result.add("products", resultProductsArray);
		return result;
	}

	private JsonArray getProductsFromJson(String json) {

		Gson gson = new Gson();
		JsonObject productsJsonObject = gson.fromJson(json, JsonObject.class);
		JsonArray productsArray = new JsonArray();
		if (!productsJsonObject.isJsonNull()) {
			productsArray = productsJsonObject.getAsJsonArray("items");
		}
		return productsArray;
	}


}
