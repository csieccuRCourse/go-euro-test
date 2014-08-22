package com.goeuro.test.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import org.json.JSONArray;

import com.goeuro.test.util.ApplicationUtilities;

/**
 * JSONService is responsible for different JSON operations. 
 * It handles urlBase String since the application asks for retrieving JSON Stream from a given url.
 * To make maintenance as easy as possible, url is in a Properties file.
 * 
 * @author Farouk
 * @version 1.0
 */
public class JSONService {

	private String urlBase = "http://api.goeuro.com/api/v2/position/suggest/en/";
	
	/**
	 * Public constructor initializes {@link #urlBase} value. It loads it from Properties file.
	 */
	public JSONService() {
		Properties properties = ApplicationUtilities.loadProperties();
		if ((String) properties.getProperty("url") != null)
			urlBase =  (String) properties.getProperty("url") ;
	}
	
	//--------------------- GETTERS & SETTERS --------------------------
	
	public String getUrlBase() {
		return urlBase;
	}

	public void setUrlBase(String urlBase) {
		this.urlBase = urlBase;
	}
	
	//------------------ END OF GETTERS & SETTERS ----------------------
	
	/**
	 * Retrieve JSON Stream.
	 * @param STRING concatenated to {@link #urlBase} used to retrieve JSON stream.
	 * @return JSON Stream
	 * @throws Exception
	 */
	public String getJSONObject(String string) throws Exception {
		StringBuilder json = new StringBuilder();
		
		 try {
	            URL url = new URL(this.urlBase+string);
	            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
	            String strTemp = "";
	            while (null != (strTemp = br.readLine())) {
	                json.append(strTemp);
	            }
	        } catch (Exception ex) {
	            throw ex;
	        }
		
		 return json.toString();
	}
	
	/**
	 * Retrieve JSON Stream and transforms it into JSONArray object.
	 * @param STRING concatenated to {@link #urlBase} used to retrieve JSON stream.
	 * @return JSON Stream
	 * @throws Exception
	 */
	public JSONArray getJSONArray(String string) throws Exception {
		StringBuilder json = new StringBuilder();
		JSONArray array = null;
		
		try {
	            URL url = new URL(this.urlBase+string);
	            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
	            String strTemp = "";
	            while (null != (strTemp = br.readLine())) {
	                json.append(strTemp);
	            }
	            
	            array = new JSONArray(json.toString());
	            
	        } catch (Exception ex) {
	            throw ex;
	        }
		 
		 return array;
	}
	
}
