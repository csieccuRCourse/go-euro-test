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

/**
 * <p>
 * This is the main class. <br/>
 * It contains the main method, which includes the scenario given by the test specs. So here's the steps of the algorithm : 
 * 
 * <ol> 
 * 	<li> Control "args". There should be exactly 1 argument which is the STRING used to get the JSON stream. </li>
 * 	<li> Use JSONService object to retrieve JSON Stream from urlBase+STRING and transform it to JSON array of JSON Objects, where urlBase can be easily modified from Properties file
 * 	<li> Filter JSON Objects members and construct new ones with only the members required (_type, _id, name, type, latitude, longitude). 
 * 		 This step could have been done dynamically as a JSONService method, but due to lack of time (method should look for sub members I suppose) I've made static here.
 * 	<li> Use CSVService to transform JSON array to CSV Stream.
 * </ol>
 * @author Farouk
 * @version 1.0
 */
public class GoEuroTest {
	
	private static final int VALID = 0;
	private static final int NO_ARGS = 1;
	private static final int ARGS_EXCEEDING = 2;
	private static final int NO_SPACE = 3;
	
	public static void main(String[] args) {
		
		JSONService service = null;		// Service responsible of JSON operations
		String string = null;			// STRING item retrieved from args
		JSONArray json = null;			// JSON objects 
		String csv = null;				// csv stream
		
		int valid = control(args);		// control args given by the user
		
		switch (valid) {
			case NO_ARGS : 
				System.err.println("Command needs 1 argument");
				System.exit(NO_ARGS);
			
			case ARGS_EXCEEDING : 
				System.err.println("Command needs no more than one argument, if it needs spaces, use \"");
				System.exit(ARGS_EXCEEDING);
			
			case NO_SPACE :
				System.out.println("STRING parameter with spaces is not accepted");
				System.exit(NO_SPACE);
			
			case VALID :
				System.out.println("Valid args, processing");
			try {
				service = new JSONService();
				string = args[0];						// Get STRING item
				json = service.getJSONArray(string);	// Get JSON Stream and transform it to JSONArray
				json = filterObjects(json);				// Filter JSON Objects members.
				
				System.out.println("url is : "+ service.getUrlBase() + string);
				System.out.println("JSON is : \n" + json);
				
				csv = CSVService.parseJSON(json);		// Get CSV Stream from JSONArray object
				System.out.println("CSV : ");
				if (csv != null)
					System.out.println(csv);
				else System.out.println("No CSV results for an empty JSON file");
				
			} catch (SocketTimeoutException ste) {
				System.err.println("Read timed out, verify your internet connection");
				System.exit(-1);
			} catch (ConnectException ce) {
				System.err.println("Connection timed out, verify your internet connection");
				System.exit(-1);
			} catch (UnknownHostException uhe) {
				System.err.println("Cannot reach host " + service.getUrlBase());
				System.exit(-1);
			} catch (IOException e) {
				System.err.println("Connection problem");
				System.exit(-1);
			}catch (Exception e) {
				System.err.println("Error occured");
				System.exit(-1);
			}
		}
	}
	
	/**
	 * The method knows the structure of the JSONObject items, 
	 * it looks for _type, _id, name and type members then 
	 * it looks for latitude and longitude in the geo_position JSONObject item.
	 * @param JSONArray object where JSONObject items to be filtered
	 * @return JSONArray of filtered JSONObject items.
	 * @throws JSONException 
	 */
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

	/**
	 * 
	 * @param application arguments array
	 * @return int number whose value is either : 
	 * <ul>
	 * 	<li> {@link #VALID} : if control is valid
	 * 	<li> {@link #NO_ARGS}, {@link #NO_SPACE}, {@link #ARGS_EXCEEDING} : if control is not valid, it returns a message that matches the problem.
	 * </ul> 
	 */
	public static int control(String [] args) {
		int valid = VALID;
		
		if (args == null || args.length == 0) 
			valid = NO_ARGS;
		else if (args.length > 1)
			valid = ARGS_EXCEEDING;
		else if (args[0].split(" ").length > 1) 
			valid = NO_SPACE;
		
		return valid;
	}
}
