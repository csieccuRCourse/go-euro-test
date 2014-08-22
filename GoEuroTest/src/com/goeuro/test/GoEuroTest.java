package com.goeuro.test;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.goeuro.test.service.CSVService;
import com.goeuro.test.service.JSONService;

public class GoEuroTest {
	
	private static final int VALID = 0;
	private static final int NO_ARGS = 1;
	private static final int ARGS_EXCEEDING = 2;
	
	public static void main(String[] args) {
		
		JSONService service = null;
		String string = null;
		JSONArray json = null;
		String csv = null;
		
		int valid = control(args);
		
		switch (valid) {
			case NO_ARGS : 
				System.err.println("Command needs 1 argument");
				System.exit(1);
			case ARGS_EXCEEDING : 
				System.err.println("Command needs no more than one argument, if it needs spaces, use \"");
				System.exit(1);
			case VALID :
				System.out.println("Valid args, processing");
			try {
				service = new JSONService();
				string = args[0];
				json = filterObjects(service.getJSONArray(string));
				
				System.out.println("url is : "+ service.getUrlBase() + string);
				System.out.println("JSON is : " + json);
				
				csv = CSVService.parseJSON(json);
				System.out.println("CSV : ");
				if (csv != null)
					System.out.println(csv);
				else System.out.println("No CSV results for an empty JSON file");
			} catch (SocketTimeoutException ste) {
				System.err.println("Read timed out, verify your internet connection");
				System.exit(1);
			} catch (ConnectException ce) {
				System.err.println("Connection timed out, verify your internet connection");
				System.exit(1);
			} catch (UnknownHostException uhe) {
				System.err.println("Cannot reach host " + service.getUrlBase());
				System.exit(1);
			} catch (IOException e) {
				System.err.println("Connection problem");
				System.exit(1);
			}catch (Exception e) {
				System.err.println("Error occured");
				System.exit(1);
			}
		}
			
	}
	
	private static JSONArray filterObjects(JSONArray jsonArray) throws JSONException {
		String[] filter = new String[] {"_type", "_id", "name", "type"};
		JSONArray newArray = new JSONArray();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject object = new JSONObject(jsonArray.getJSONObject(i), filter);
			JSONObject geo_position = jsonArray.getJSONObject(i).getJSONObject("geo_position");
			object.put("latitude", geo_position.opt("latitude"));
			object.put("longitude", geo_position.opt("longitude"));
			
			newArray.put(object);
		}
		
		return newArray;
	}


	public static int control(String [] args) {
		int valid = VALID;
		
		if (args == null || args.length == 0) 
			valid = NO_ARGS;
		else if (args.length > 1)
			valid = ARGS_EXCEEDING;
		
		return valid;
	}
}
