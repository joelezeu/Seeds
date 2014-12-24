/**
 * 
 */
package com.diadementi.seeds.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author user pc
 * 
 */
public class SeedsResponse {

	/**
	 * 
	 */
	int code;
	String response;
	final static String CONNECT_TIMEOUT="Connection TimeOut";
	final static String TAG_MESSAGE = "message";
	final static String SERVER_ERROR="A problem has occurred and it's not your fault";
	public SeedsResponse(int code, String response) {
		// TODO Auto-generated constructor stub
		this.code=code;
		this.response=response;
	}
	public static void checkResponse(int code,String response){
		if(code>300&&code<500){
			String message=retrieveError(response);
			throw new SeedsException(message,code);
		}else if(code >500){
			throw new SeedsException(SERVER_ERROR,code);
		}
		
	}

	private static String retrieveError(String text) {
		try {
			JSONObject message = new JSONObject(text);
			String error = message.getString(TAG_MESSAGE);
			return error;
		} catch (JSONException ex) {
			return SERVER_ERROR;
		}
	}
}
