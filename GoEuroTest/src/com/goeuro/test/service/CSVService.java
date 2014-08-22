package com.goeuro.test.service;

import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;

public class CSVService {
	
	public static String parseJSON(String json) throws JSONException {
		JSONArray array = new JSONArray(json);
		return CDL.toString(array);
	}
	
	public static String parseJSON(JSONArray json) throws JSONException {
		return CDL.toString(json);
	}
}
