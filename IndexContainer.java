package trading;

import java.util.*;

/**
 * the collection that contains all index data for one market
 * 
 * @author kasay
 *
 */
public class IndexContainer {

	// each data set for OHLC graph
	public List<Long> xData;
	public List<Double> openData;
	public List<Double> highData;
	public List<Double> lowData;
	public List<Double> closeData;

	// for checking whether the fetched index data already exists in this container
	// or not
	public List<Long> timeData;

	// temporary value for openData referring same value of previous closeData
	public double tmp;

	/**
	 * initialization
	 */
	public IndexContainer() {
		// initialization
		xData = new ArrayList<Long>();
		openData = new ArrayList<Double>();
		highData = new ArrayList<Double>();
		lowData = new ArrayList<Double>();
		closeData = new ArrayList<Double>();

		timeData = new ArrayList<Long>();

		// to avoid the thrown exception: "~Data cannot be empty!"
		xData.add((long) 1);
		openData.add((double) 1);
		highData.add((double) 1);
		lowData.add((double) 1);
		closeData.add((double) 1);

		tmp = 1;
	}

	/**
	 * adds one set of index data to the existed container
	 * 
	 * @param x
	 * @param open
	 * @param high
	 * @param low
	 * @param close
	 * @param time
	 */
	public void addData(long x, double open, double high, double low, double close, long time) {
		xData.add(x);
		highData.add(high);
		lowData.add(low);
		closeData.add(close);

		/* management for openData */
		// if this adding data to the container is first two (x=10 and x=20) => then
		// removes first element for each Data
		if (((Long) x) < 30) {
			xData.remove(0);
			openData.remove(0);
			highData.remove(0);
			lowData.remove(0);
			closeData.remove(0);

			openData.add(tmp);
		}
		// if the container contains more than 30 set of index data => then removes
		// first element for each Data
		else if (timeData.size() > 30) {
			xData.remove(0);
			openData.remove(0);
			highData.remove(0);
			lowData.remove(0);
			closeData.remove(0);

			openData.add(tmp);
		} else
			openData.add(tmp);

		// temporary value for openData referring same value of previous closeData
		tmp = close;

		timeData.add(time);
	}

	/**
	 * checks whether the index data is new or not
	 * 
	 * @param time
	 * @return if there is already same timestamp in the timeData, then returns
	 *         false, otherwise true because it is new index data
	 */
	public boolean isNewIndex(long time) {
		if (timeData.contains(time))
			return false;
		else
			return true;
	}

	/**
	 * clears all data, called by FuturePrediction
	 */
	public void clear() {
		xData.clear();
		openData.clear();
		highData.clear();
		lowData.clear();
		closeData.clear();
		timeData.clear();
	}
}
