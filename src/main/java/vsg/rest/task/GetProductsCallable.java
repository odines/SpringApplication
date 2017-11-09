package vsg.rest.task;

import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.Callable;

/**
 * Created by Denis Orlov.
 */
public class GetProductsCallable implements Callable<String> {
	private static final String URI_PRODUCTS = "https://ccadmin-z8ga.oracleoutsourcing.com/ccadminui/v1/products";
	private static final Logger LOGGER = LoggerFactory.getLogger(GetProductsCallable.class);
	private String authToken;
	private String categoryId;

	public GetProductsCallable(String categoryId, String authToken) {
		this.categoryId = categoryId;
		this.authToken = authToken;
	}

	@Override
	public String call() throws Exception {
		JerseyClient client = JerseyClientBuilder.createClient();
		String result = null;
		LOGGER.info("GET REQUEST: " + URI_PRODUCTS);
		JerseyInvocation.Builder builder = client.target(URI_PRODUCTS).queryParam("categoryId", categoryId).queryParam
				("limit", "20").request
				(MediaType
						.APPLICATION_JSON).header("Authorization", "Bearer " + authToken);
		Response response = builder.get();
		if (response.getStatus() == 200) {
			LOGGER.info("EXPORT COMPLETE. ExportStatus:" + response.getStatus());
			result = response.readEntity(String.class);
		} else {
			LOGGER.error(response.getStatusInfo().getReasonPhrase());
		}
		return result;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String pCategoryId) {
		categoryId = pCategoryId;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String pAuthToken) {
		authToken = pAuthToken;
	}

}
