package com.goeuro.test.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import org.json.JSONArray;

public class JSONService {

	private String urlBase = "http://api.goeuro.com/api/v2/position/suggest/en/";
	
	public String getUrlBase() {
		return urlBase;
	}

	public void setUrlBase(String urlBase) {
		this.urlBase = urlBase;
	}
	
	public String getJSONObject(String string) throws Exception {
		StringBuilder json = new StringBuilder();
		
		Properties properties = loadProperties();
		if ((String) properties.getProperty("url") != null)
			urlBase =  (String) properties.getProperty("url") ;
		
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
	
	public JSONArray getJSONArray(String string) throws Exception {
		StringBuilder json = new StringBuilder();
		JSONArray array = null;
		
		Properties properties = loadProperties();
		if ((String) properties.getProperty("url") != null)
			urlBase =  (String) properties.getProperty("url") ;
		
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
	
	private Properties loadProperties() {
		Properties properties = new Properties();
		FileInputStream file;
		String path = null;
		try {
			path = JSONService.class.getClassLoader().getResource("com//goeuro//test//properties//Parameters.properties").getPath();
			file = new FileInputStream(path);
			properties.load(file);
		} catch (FileNotFoundException e) {
			properties = null;
		} catch (IOException e) {
			properties = null;
		}
		
		return properties;
	}
}
