package utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.WS.Response;
import play.mvc.Http;

public class Authenticate {
	
	private static final Logger logger = LoggerFactory.getLogger(Authenticate.class);

	// store id/secret on configs for testing/production environment
	public static String client_id = "2_3efLoA";
	public static String client_secret = "QtaZjBhjWa5A2h1mwAnPoS6W7l33NfRbLkAyHJ8diqFZXX9DWPVy4ZnBeTbWJEFy";

	/**
	 * Gets the token.
	 */
	public static void token() {

		// authenticate
		Map<String, Object> rq = new HashMap<String, Object>();
		Map<String, Object> client = new HashMap<String, Object>();
		client.put("grant_type", "client_credentials");
		client.put("client_id", client_id);
		client.put("client_secret", client_secret);
		rq.put("requestBody", client);

		Response response = Api.getWSResponse(Api.AUTH_TOKEN_URL, rq, true);
		if (response.getStatus() == Http.Status.OK) {
			logger.debug("auth:" + response.getStatus() + response.getBody());
			JsonNode auth = response.asJson();
			int expires = auth.get("expires_in").asInt();
			String access_token = auth.get("access_token").asText();

			// store token to server cache
			StorageHelper.setToken(access_token, expires);

		}
	}

	/**
	 * refreshToken.
	 * 
	 */
	public static void refreshToken() {

		// authenticate
		Map<String, Object> rq = new HashMap<String, Object>();
		Map<String, Object> client = new HashMap<String, Object>();
		client.put("grant_type", "refresh");
		client.put("client_id", client_id);
		client.put("client_secret", client_secret);
		client.put("refresh_token", StorageHelper.getToken());
		rq.put("requestBody", client);

		Response response = Api.getWSResponse(Api.AUTH_TOKEN_URL, rq, true);
		if (response.getStatus() == Http.Status.OK) {
			logger.debug("auth:" + response.getStatus() + response.getBody());
			JsonNode auth = response.asJson();
			int expires = auth.get("expires_in").asInt();
			String access_token = auth.get("access_token").asText();

			// store token to server cache
			StorageHelper.setToken(access_token, expires);

		}
	}
}
