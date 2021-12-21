/**
 * CONFIDENTIAL
 * @Package:  configs
 * @FileName: InitialSettings.java
 * @author:   tammy
 * @date:     2021/12/19
 */
package configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import play.Application;
import play.GlobalSettings;
import utils.Api;
import utils.Authenticate;
import utils.StorageHelper;

public class InitialSettings extends GlobalSettings {
	
	private static final Logger logger = LoggerFactory.getLogger(InitialSettings.class);

	/*
	 * @Override public Result onError(final RequestHeader requestHeader, final
	 * Throwable t) { return
	 * play.mvc.Controller.internalServerError(views.html.error500.render()); }
	 */

	@Override
	public void onStart(final Application app) {
		// authenticate
		Authenticate.token();
		logger.info("access_token : " + StorageHelper.getToken());

		// Trending
		JsonNode pop = Api.getTrendingList(100);
		if (pop != null) {
			StorageHelper.saveTrending((JsonNode) pop.get("tags"));
		}
	}
}
