package utils;

import play.libs.WS.Response;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.WSRequestHolder;
import play.mvc.Http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Api {

	private static final Logger logger = LoggerFactory.getLogger(Api.class);

	private static String endpoint = "api.gfycat.com";

	public static String AUTH_TOKEN_URL = "/v1/oauth/token";

	public static String TRENDING_URL = "/v1/gfycats/trending";
	
	public static String POP_URL = "/v1/reactions/populated";

	public static String SEARCH_URL = "/v1/gfycats/search";

	/**
	 * Gets the trending.
	 * 
	 * @param tagName the trending
	 * @return the trending
	 */
	public static JsonNode getTrendingList(int count) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("count", count);

		Response response = getWSResponse(Api.POP_URL, params, false, false);
		if (response.getStatus() == Http.Status.OK) {
			JsonNode asJson = response.asJson();
			if (asJson != null) {
				return asJson;
			}
		}
		return null;
	}

	/**
	 * Gets the trending.
	 * 
	 * @param tagName the trending
	 * @return the trending
	 */
	public static JsonNode getTrending(String tagName, int count) {
		if (tagName == null || tagName.isEmpty()) {
			return null;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tagName", tagName);
		params.put("count", count);

		Response response = getWSResponse(Api.TRENDING_URL, params, false, false);
		if (response.getStatus() == Http.Status.OK) {
			JsonNode asJson = response.asJson();
			if (asJson != null) {
				return asJson;
			}
		}
		return null;
	}

	/**
	 * Gets the Search item.
	 * 
	 * @param q the keywords
	 * @return the item
	 */
	public static JsonNode getSearchItem(String q, int count) {
		if (q == null || q.isEmpty()) {
			return null;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("search_text", q);
		params.put("count", count);

		Response response = getWSResponse(Api.SEARCH_URL, params, false, false);
		if (response.getStatus() == Http.Status.OK) {
			JsonNode asJson = response.asJson();
			if (asJson != null) {
				return asJson;
			}
		}
		return null;
	}

	/**
	 * Gets the WS response.
	 * 
	 * @param url           the url
	 * @param params        the params
	 * @param isPostRequest the isPostRequest
	 * @return the WS response
	 */
	public static Response getWSResponse(String url, Map<String, Object> params, boolean isPostRequest) {
		return getWSResponse(url, params, isPostRequest, false);
	}

	/**
	 * Gets the WS response.
	 * 
	 * @param url           the url
	 * @param params        the params
	 * @param isPostRequest the isPostRequest
	 * @param needAuth      the needAuth
	 * @return the WS response
	 */
	public static Response getWSResponse(String url, Map<String, Object> params, boolean isPostRequest,
			boolean needAuth) {
		long startTime = System.currentTimeMillis();
		long timeout = 60000; // set timeout to 60s
		Response response = null;
		try {
			Promise<WS.Response> wsInfo = null;
			WSRequestHolder wsUrl = WS.url("https://" + endpoint + url);
			wsUrl.setContentType("application/json");

			if (needAuth) {
				if (StringUtils.isEmpty(StorageHelper.getToken())) {
					Authenticate.token();
				}
				wsUrl.setHeader("Authorization", "Bearer " + StorageHelper.getToken());
			}

			String reqeustBodyJsonStr = null;
			if (params != null) {
				for (String key : params.keySet()) {

					Object object = params.get(key);
					if (object == null) {
						continue;
					}

					if ("requestBody".equals(key)) { // POST requestBody
						try {
							reqeustBodyJsonStr = new ObjectMapper().writeValueAsString(object);
						} catch (JsonProcessingException e) {
							logger.error(e.getMessage(), e);
						}
					} else { // GET/POST request parameter
						if (object instanceof String) {
							wsUrl.setQueryParameter(key, (String) object);
						} else if (object instanceof String[]) {
							for (String value : (String[]) object) {
								wsUrl.setQueryParameter(key, value);
							}
						}
					}
				}
			}

			if (isPostRequest) { // POST method
				logger.debug("getWSResponse POST:" + url + " body: " + reqeustBodyJsonStr);
				wsInfo = wsUrl.post(reqeustBodyJsonStr);
			} else { // GET method
				logger.debug("getWSResponse GET : " + url + " params: " + params);
				wsInfo = wsUrl.get();
			}
			response = wsInfo.get(timeout);

			// The accessToken is invalid or has been revoked.
			if (needAuth && response.getStatus() == 401) {
				Authenticate.refreshToken();
				response = wsInfo.get(timeout);
			}

			return response;
		} finally {
			logger.debug("getWSResponse Status : " + response == null ? "null"
					: response.getStatus() + (System.currentTimeMillis() - startTime) + " ms...");
		}
	}
}
