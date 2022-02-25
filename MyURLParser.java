package trading;

import java.io.*;
import java.net.*;

/**
 * this class is not possible to be instantiated, but only for static getResult
 * method
 * 
 * @author kasay
 *
 */
public abstract class MyURLParser {
	/**
	 * 
	 * this method has a responsibility that returns String result from a URL
	 * 
	 * @param urlString
	 * @return String data of a URL
	 */
	public static String getResult(String urlString) {
		String result = "";
		try {
			URL url = new URL(urlString);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/*
			 * System.out.println( "ResponseCode: " + con.getResponseCode() + "\n" +
			 * "ResponseMessage: " + con.getResponseMessage());
			 */
			con.connect();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String tmp = "";
			while ((tmp = br.readLine()) != null) {
				result += tmp;
			}
			br.close();
			con.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
