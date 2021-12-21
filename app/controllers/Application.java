package controllers;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import play.data.DynamicForm;
import play.data.Form;

import play.mvc.*;
import utils.Api;
import utils.StorageHelper;
import views.html.*;

public class Application extends Controller {
	
	// HOMEPAGE
	public static Result index() throws Exception {
		// getTrending with tagName "friends"
//		String tagName = "friends";
//		JsonNode trending = Api.getTrending(tagName, 10);
		JsonNode trending = StorageHelper.getTrending();

		return ok(index.render(trending, "Trending GIFs"));
	}
	
	// Search Result
	public static Result search(String q) throws Exception {
		String keyword = null;
		if (StringUtils.isEmpty(q)) {
			DynamicForm requestData = Form.form().bindFromRequest();
			if (requestData == null) {
				return Results.badRequest();
			}
			keyword = requestData.get("searchinput");
			return redirect(controllers.routes.Application.search(keyword)); 
		} else {
			keyword = q;
		}
		JsonNode items = Api.getSearchItem(keyword, 10);

		return ok(index.render(items, "Search result : " + keyword));
	}
}
