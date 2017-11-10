package vsg.rest.service;

import com.google.gson.*;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyInvocation;
import org.glassfish.jersey.internal.guava.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vsg.rest.settings.AuthData;
import vsg.rest.task.GetProductsCallable;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.text.MessageFormat;
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
	private static final String URI_EXECUTE_EXPORT = "https://ccadmin-z8ga.oracleoutsourcing.com/ccadminui/v1/exportProcess";
	private static final String URI_EXECUTE_EXPORT_PARAM = "https://ccadmin-z8ga.oracleoutsourcing" +
			".com/ccadminui/v1/exportProcess/{0}";

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

	public InputStream getAlternativeExport() {
		JerseyClient client = JerseyClientBuilder.createClient();
		Response executeExportResponse = executePostRequest(client, URI_EXECUTE_EXPORT, getExportPostEntity());
		InputStream result = null;
		if (isValidResponse(executeExportResponse)) {
			String processId = extractProcessIdFromResponse(executeExportResponse.readEntity(String.class));
			LOGGER.info("EXPORT COMPLETE. RESULT = " + processId);
			try {
				LOGGER.info("Waiting for completion of export ....");
				Thread.sleep(5000);
			} catch (InterruptedException pE) {
				LOGGER.error("InterruptedException: " + pE.getMessage());
			}
			String exportURI = MessageFormat.format(URI_EXECUTE_EXPORT_PARAM,
					processId);
			Response exportResponse = executeGetRequest(client, exportURI);
			if (isValidResponse(exportResponse)) {
				String archiveUri = extractUrlFromResponse(exportResponse.readEntity(String.class));
				if (!StringUtils.isEmpty(archiveUri)) {
					Response archiveResponse = executeGetRequest(client, archiveUri);
					if (isValidResponse(archiveResponse)) {
						result = archiveResponse.readEntity(InputStream.class);
					}
				}
			}
		}
		return result;
	}

	private String extractUrlFromResponse(String responseString) {
		LOGGER.info("EXPORT RESPONSE = " + responseString);
		JsonObject responseJson = convertStringToJson(responseString);
		String responseStatus = responseJson.getAsJsonPrimitive("completed").getAsString().replaceAll("\"", "");
		String resultUri = null;
		if (responseStatus.contains("true")) {
			JsonArray linksArray = responseJson.getAsJsonArray("links");
			if (!linksArray.isJsonNull()) {
				for (JsonElement linkItem : linksArray) {
					if (!linkItem.isJsonNull() && linkItem.isJsonObject()) {
						JsonObject linkObject = linkItem.getAsJsonObject();
						if (linkObject.getAsJsonPrimitive("rel").toString().contains("file")) {
							resultUri = linkObject.getAsJsonPrimitive("href").getAsString();
							LOGGER.info("EXTRACTED URI: " + resultUri);
						}
					}
				}
			}
		}
		return resultUri;
	}

	private Response executeGetRequest(JerseyClient client, String uri) {
		LOGGER.info("GET REQUEST: " + uri);
		JerseyInvocation.Builder builder = client.target(uri)
				.request(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + authData.getAuthToken()).header
						("Accept-Encoding", "gzip").header("Content-Encoding", "deflate");
		return builder.get();
	}

	private boolean isValidResponse(final Response response) {
		boolean result;
		if (null != response) {
			Response.StatusType responseStatus = response.getStatusInfo();
			if (responseStatus.equals(Response.Status.OK) || responseStatus.equals(Response.Status.ACCEPTED)) {
				LOGGER.info("Request was successfully sent. ResponseStatus = " + responseStatus);
				result = true;
			} else {
				result = false;
				LOGGER.error("ResponseStatus is not valid. Reason: " + response.getStatusInfo().getReasonPhrase());
			}
		} else {
			result = false;
		}
		return result;
	}

	private Response executePostRequest(JerseyClient client, String uri, Entity<String> postEntity) {
		LOGGER.info("POST REQUEST: " + uri);
		JerseyInvocation.Builder builder = client.target(uri)
				.request(MediaType
						.APPLICATION_JSON).header("Authorization", "Bearer " + authData.getAuthToken()).header
						("Accept-Encoding", "gzip").header("Content-Encoding", "deflate");
		return builder.post(postEntity);
	}

	private String extractProcessIdFromResponse(String responseString) {
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		JsonObject json = gson.fromJson(responseString, JsonObject.class);
		return json.get("processId").toString().replaceAll("\"", "");

	}


	private Entity<String> getExportPostEntity() {

		JsonObject entity = new JsonObject();
		entity.addProperty("fileName", "testExport.zip");
		JsonArray jsonArray = new JsonArray();
		JsonObject exportType1 = new JsonObject();
		exportType1.addProperty("format", "csv");
		exportType1.addProperty("id", "Profiles");

		JsonObject exportType2 = new JsonObject();
		exportType2.addProperty("format", "json");
		exportType2.addProperty("id", "Products");
		jsonArray.add(exportType1);
		jsonArray.add(exportType2);
		entity.add("items", jsonArray);
		return Entity.entity(entity.toString(), MediaType.APPLICATION_JSON);
	}

	private Map<String, String> getCategories() {
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
			//throw new RuntimeException(response.getStatusInfo().getReasonPhrase())
			LOGGER.error(response.getStatusInfo().getReasonPhrase());
		}
		return result;
	}

	public JsonObject getProducts() {
		ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("getProducts-%d").build();
		ExecutorService threadPool = Executors.newFixedThreadPool(5, factory);
		List<Callable<String>> tasksList = getCallableTasksList();
		List<Future<String>> resultList = null;
		try {
			resultList = threadPool.invokeAll(tasksList);
			threadPool.shutdown();
			while (!threadPool.isTerminated()) {
				//threadPool.awaitTermination(10, TimeUnit.SECONDS);
				LOGGER.info("Wait for termination ...");
			}
		} catch (InterruptedException pE) {
			LOGGER.error("Exception in ThreadPool: e = " + pE.getMessage());
		} finally {
			threadPool.shutdown();
		}
		JsonObject resultProductsObject = null;
		if (threadPool.isTerminated()) {
			LOGGER.info("ThreadPool was terminated");
			resultProductsObject = aggregateProducts(resultList);
		}
		return resultProductsObject == null ? new JsonObject() : resultProductsObject;
	}

	private Map<String, String> convertJSONToCollections(String inputJson) {
		JsonObject json = convertStringToJson(inputJson);
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

	private List<Callable<String>> getCallableTasksList() {
		Map<String, String> categoriesMap = getCategories();
		List<Callable<String>> tasksList = new ArrayList<>();
		for (Map.Entry entry : categoriesMap.entrySet()) {
			String categoryId = (String) entry.getKey();
			tasksList.add(new GetProductsCallable(categoryId, authData.getAuthToken()));
		}
		return tasksList;
	}

	private JsonArray getProductsFromJson(String json) {
		JsonObject productsJsonObject = convertStringToJson(json);
		JsonArray productsArray = new JsonArray();
		if (!productsJsonObject.isJsonNull()) {
			productsArray = productsJsonObject.getAsJsonArray("items");
		}
		return productsArray;
	}

	private JsonObject convertStringToJson(String inputString) {
		Gson gson = new Gson();
		return gson.fromJson(inputString, JsonObject.class);
	}


}
