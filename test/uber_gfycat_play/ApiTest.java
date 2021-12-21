package uber_gfycat_play;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.WS.Response;
import play.mvc.Http;
import utils.Api;
import utils.Authenticate;

public class ApiTest {

	@Test
	public void test() {
		String after = "";
		String before = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2NDAwNjM0MDUsImlzcyI6IjJfM2VmTG9BIiwicm9sZXMiOlsiQ29udGVudF9SZWFkZXIiXX0._T6noC1CceZ3vLBTp-63dDr_N3861MnFTvtq0C7JL-8";

		// authenticate
		Map<String, Object> rq = new HashMap<String, Object>();
		Map<String, Object> client = new HashMap<String, Object>();
		Response response = null;
//		client.put("grant_type", "client_credentials");
//		client.put("client_id", Authenticate.client_id);
//		client.put("client_secret", Authenticate.client_secret);
//		rq.put("requestBody", client);
//
//		response = Api.getWSResponse(Api.AUTH_TOKEN_URL, rq, true);
//		System.out.println("before : " + response.getStatus() +  response.getBody());
//		if (response.getStatus() == Http.Status.OK) {
//			JsonNode auth = response.asJson();
//			before = auth.get("access_token").asText();
//		}
		// refresh
		client = new HashMap<String, Object>();
		client.put("grant_type", "refresh");
		client.put("client_id", Authenticate.client_id);
		client.put("client_secret", Authenticate.client_secret);
		client.put("refresh_token", before);
		rq.put("requestBody", client);

		response = Api.getWSResponse(Api.AUTH_TOKEN_URL, rq, true);
		System.out.println("after : " + response.getStatus() +  response.getBody());
		if (response.getStatus() == Http.Status.OK) {
			JsonNode auth = response.asJson();
			after = auth.get("access_token").asText();
		}
		System.out.println("before : " + before + "; after : " + after);
		assertNotEquals(before, after);
	}

}
