package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/scribejava/scribejava/tree/master/scribejava-apis/src/main/java/com/github/scribejava/apis
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */


// used by all activities
public class TwitterClient extends OAuthBaseClient {
	public static final BaseApi REST_API_INSTANCE = TwitterApi.instance(); // Change this-- to what?
	public static final String REST_URL = "https://api.twitter.com/1.1";

	// Landing page to indicate the OAuth flow worked in case Chrome for Android 25+ blocks navigation back to the app.
	public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

	// See https://developer.chrome.com/multidevice/android/intents
	public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

	public TwitterClient(Context context) {
		super(context, REST_API_INSTANCE,
				REST_URL,
				context.getString(R.string.api_key),
				context.getString(R.string.rest_consumer_secret),
				String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
						context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
	}

	// DEFINE METHODS for different API endpoints here
	public void getHomeTimeline(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("since_id", 1); // oldest tweet greater than this value (pull-to-refresh feature)
		client.get(apiUrl, params, handler);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

	public void sendTweet(String message, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", message);
		client.post(apiUrl, params, handler);
	}

	public void replyToTweet (String message, long id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", message);
		params.put("in_reply_to_status_id", id);
		client.post(apiUrl, params, handler);
	}

	public void favoriteTweet (boolean isFavorited, long id, AsyncHttpResponseHandler handler) {
		if (!isFavorited) {
			// not favorited yet
			Log.i("TwitterClient", "favoriting tweet");
			String apiUrl = getApiUrl("favorites/create.json");
			RequestParams params = new RequestParams();
			params.put("id", id);
			client.post(apiUrl, params, handler);
		}
		else {
			// favorited already
			Log.i("TwitterClient", "unfavoriting tweet");
			String apiUrl = getApiUrl("favorites/destroy.json");
			RequestParams params = new RequestParams();
			params.put("id", id);
			client.post(apiUrl, params, handler);
		}
	}

	public void retweet (long id, AsyncHttpResponseHandler handler) {
	    String apiUrl = getApiUrl("statuses/retweet/" + id + ".json");
	    Log.d("RetweetInTwitterClient", "reached this point");
        RequestParams params = new RequestParams();
        params.put("id", id);
        client.post(apiUrl, params, handler);
    }

	public void getFollowers(String screenName, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("followers/list.json");
		Log.d("getFollowers", "here");
		Log.d("getFollowers", String.format("%s", screenName));
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("screen_name", screenName);
		client.get(apiUrl, params, handler);
	}

	public void getFriends(String screenName, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("friends/list.json");
		RequestParams params = new RequestParams();
		params.put("screen_name", screenName);
		client.get(apiUrl, params, handler);
	}
}
