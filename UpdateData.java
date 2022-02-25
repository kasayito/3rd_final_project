package trading;

import java.util.*;

//JSONSimple
import org.json.simple.*;
import org.json.simple.parser.*;

/**
 * this thread updates the index data in every second
 * 
 * @author kasay
 *
 */
public class UpdateData extends Thread {
	// parsing JSON from URL
	private String urlString;
	private IndexContainer indexContainer;
	private JSONParser jpars;
	public JSONObject jobj;

	// counting the x-axis
	public static int x = 0;

	/**
	 * initialization
	 * 
	 * @param urlString
	 * @param myContainer
	 */
	public UpdateData(String urlString, IndexContainer indexContainer) {
		this.urlString = urlString;
		this.indexContainer = indexContainer;
		jpars = new JSONParser();
	}

	/**
	 * this method has a responsibility that parses to IndexContainer from
	 * JSONObject that is fetched from Coinchart
	 */
	public void myCoinchartParser() {
		// for debug
		System.out.println("\n0 timestamp: " + jobj.get("timestamp"));

		// x-axis: 10, 20, 30, ...
		x += 10;

		double[] tmp = new double[3];
		tmp[0] = Double.parseDouble((String) ((JSONObject) jobj.get("JPY")).get("24h"));
		tmp[1] = Double.parseDouble((String) ((JSONObject) jobj.get("JPY")).get("30d"));
		tmp[2] = Double.parseDouble((String) ((JSONObject) jobj.get("JPY")).get("7d"));

		Arrays.sort(tmp);

		indexContainer.addData(x + 5, 0, tmp[2], tmp[0], tmp[1], (long) jobj.get("timestamp"));
	}

	/**
	 * this method has a responsibility that parses to IndexContainer from
	 * JSONObject that is fetched from Coincheck
	 */
	public void myCoincheckParser() {
		// for debug
		System.out.println("\n1 timestamp: " + jobj.get("timestamp"));

		// x-axis: 10, 20, 30, ...
		x += 10;

		//
		double[] tmp = new double[3];
		tmp[0] = (double) jobj.get("bid");
		tmp[1] = (double) jobj.get("ask");
		tmp[2] = (double) jobj.get("last");

		Arrays.sort(tmp);

		indexContainer.addData(x, 0, tmp[2], tmp[0], tmp[1], (long) jobj.get("timestamp"));
	}

	/**
	 * entry point when the Main class starts threading of this class
	 */
	@Override
	public void run() {
		while (true) {
			// get String data from the webAPI
			String result = MyURLParser.getResult(urlString);

			try {
				// converts from String data to JSONObject
				jobj = (JSONObject) jpars.parse(result);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// if the timestamp is same => then the fetched index data is same
			// => non-new index data => do not add it to container => ignore it
			if (indexContainer.isNewIndex((long) jobj.get("timestamp"))) {
				if (urlString.equals("http://api.bitcoincharts.com/v1/weighted_prices.json"))
					myCoinchartParser();
				else if (urlString.equals("https://coincheck.com/api/ticker"))
					myCoincheckParser();
			}

			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
