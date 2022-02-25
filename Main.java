package trading;

import java.util.*;
import java.io.*;

/**
 * entry class of this application
 * 
 * @author kasay
 *
 */
public class Main {

	// static OHLCChart chart;
	private static String title = "Bitcoin / Japanese yen";
	private static GUI gui;

	// number of markets
	private static int numberMarkets = 2;
	// markets URL (Coinchart and Coincheck)
	private static String url0 = "http://api.bitcoincharts.com/v1/weighted_prices.json";
	private static String url1 = "https://coincheck.com/api/ticker";

	// a list of IndexContainer for each markets
	private static List<IndexContainer> containerList;
	// a list of UpdateData for each markets
	private static List<UpdateData> updateList;

	// serialized VirtualBankAccount for the account history
	private static VirtualBankAccount serialized;

	/**
	 * entry point of this application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// creates indexContainer for each market
		containerList = new ArrayList<IndexContainer>();
		for (int i = 0; i < numberMarkets; i++)
			containerList.add(new IndexContainer());

		// creates threads for updating index data for each market
		updateList = new ArrayList<UpdateData>();
		updateList.add(new UpdateData(url0, containerList.get(0)));
		updateList.add(new UpdateData(url1, containerList.get(1)));

		// starts threading for updating fetched data
		for (int i = 0; i < numberMarkets; i++)
			updateList.get(i).start();

		// creates VirtualBankAccount for simulation
		serialized = new VirtualBankAccount(containerList.get(1));
		load();

		// creates GUI object
		gui = new GUI(title, containerList, serialized);
		gui.setupWindow();
	}

	/**
	 * loads the file containing account history data
	 */
	public static void load() {
		try {
			// loads serialized VirtualBankAccount object
			File file = new File("acount_history.dat");
			// if the file is not empty
			if (file.length() != 0) {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream("acount_history.dat"));

				// copies the serialized object
				serialized.history = (List<Object[]>) ois.readObject();
				// if there is at least one history
				if (serialized.history.size() > 0) {
					serialized.cash = (double) serialized.history.get(serialized.history.size() - 1)[2];
					serialized.cryptocurrency = (double) serialized.history.get(serialized.history.size() - 1)[3];
				}
				ois.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * saves the file containing account history
	 */
	public static void save() {
		try {
			// saves serialized VirtualBankAccount object
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("acount_history.dat"));
			oos.writeObject(serialized.history);
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * returns source IndexContainer for future prediction algorithm calculation
	 * 
	 * @return IndexContainer for Coincheck
	 */
	public static IndexContainer getSource() {
		return containerList.get(1);
	}
}