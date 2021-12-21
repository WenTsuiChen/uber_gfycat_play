package utils;


import com.fasterxml.jackson.databind.JsonNode;

import play.cache.Cache;

/**
 * This class is an helper to store/retrieve objects (from cache).
 * 
 * @author tammy
 * @since 1.0.0
 */
public class StorageHelper {
	
	private static String token = "access_token";
	private static String trending = "trending";

	public static String getToken() {
		return (String) Cache.get(token);
	}

	public static void setToken(String token, int timeout) {
		Cache.set(token, token, timeout);
	}
	
	public static void removeToken() {
		Cache.remove(token);
	}

	/**
	 * Get an object from storage.
	 * 
	 * @param key a key
	 * @return the object
	 */
	public static JsonNode getTrending() {
		return (JsonNode) Cache.get(trending);
	}
	
	/**
	 * Save an object in storage.
	 * 
	 * @param key     a key
	 * @param value   a value to store
	 * @param timeout the timeout
	 */
	public static void saveTrending(final Object value) {
		Cache.set(trending, value);
	}

	/**
	 * Remove an object from storage.
	 * 
	 * @param key a key
	 */
	public static void removeTrending() {
		Cache.remove(trending);
	}

}
