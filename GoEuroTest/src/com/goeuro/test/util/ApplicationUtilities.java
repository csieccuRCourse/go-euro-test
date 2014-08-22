package com.goeuro.test.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Simple class to handle useful operations.
 * @author Farouk
 *
 */
public class ApplicationUtilities {

	/**
	 * Loads application parameters.
	 * @return {@link Properties} object containing application global parameters.
	 */
	public static Properties loadProperties() {
		Properties properties = new Properties();
		InputStream is = null;
		try {
			is = ApplicationUtilities.class.getClassLoader().getResourceAsStream("com/goeuro/test/properties/Parameters.properties");
			properties.load(is);
		} catch (FileNotFoundException e) {
			properties = null;
		} catch (IOException e) {
			properties = null;
		}
		
		return properties;
	}
}
