package trading;

/**
 * the class that predicts future index by given source index
 * 
 * @author kasay
 *
 */
public class FuturePrediction {

	// before and after prediction
	private IndexContainer sourceIndex;
	private IndexContainer predictedIndex;

	// weight values
	private static double[] w = new double[16];

	/**
	 * initialization
	 */
	public FuturePrediction() {
		// initialization
		predictedIndex = new IndexContainer();
		// clears added data called in constructor of IndexContainer when predictedIndex
		// is created
		predictedIndex.clear();
	}

	/**
	 * from index, calculates target value with weight parameters
	 * 
	 * @param i
	 * @param j
	 * @return calculated value
	 */
	public double calculate(int i, int j) {
		System.out.println(0 + j * 4 + ": " + w[0 + j * 4]);
		System.out.println(1 + j * 4 + ": " + w[1 + j * 4]);
		System.out.println(2 + j * 4 + ": " + w[2 + j * 4]);
		System.out.println(3 + j * 4 + ": " + w[3 + j * 4]);

		return w[0 + j * 4] * sourceIndex.openData.get(i) + w[1 + j * 4] * sourceIndex.highData.get(i)
				+ w[2 + j * 4] * sourceIndex.lowData.get(i) + w[3 + j * 4] * sourceIndex.closeData.get(i);
	}

	/**
	 * algorithm how to find weight values
	 * 
	 * @param indexContainer
	 * @return predictedIndex for 10 future index data
	 */
	public IndexContainer algorithm(IndexContainer indexContainer) {
		// deep copy from indexContainer
		sourceIndex = new IndexContainer();
		sourceIndex.xData.addAll(indexContainer.xData);
		sourceIndex.openData.addAll(indexContainer.openData);
		sourceIndex.highData.addAll(indexContainer.highData);
		sourceIndex.lowData.addAll(indexContainer.lowData);
		sourceIndex.closeData.addAll(indexContainer.closeData);
		sourceIndex.timeData.addAll(indexContainer.timeData);
		sourceIndex.tmp = indexContainer.tmp;

		// process
		// for each set of index data, calculate prediction
		// calculate for each data (open, high, low, close)
		// get correct value called target (solution from past value)
		// calculate difference between target and calculated
		// idealW is weight value for for solution
		// the new w is determined by average between idealW and current w (0<w<1)
		double calculated = 0, target = 0, difference = 0;
		for (int i = 0; i < sourceIndex.xData.size() - 1; i++) {
			for (int j = 0; j < 4; j++) {
				calculated = calculate(i, j);

				switch (j) {
				case 0:
					target = sourceIndex.openData.get(i + 1);
					break;
				case 1:
					target = sourceIndex.highData.get(i + 1);
					break;
				case 2:
					target = sourceIndex.lowData.get(i + 1);
					break;
				case 3:
					target = sourceIndex.closeData.get(i + 1);
					break;
				}

				difference = target - calculated;

				double[] idealW = new double[16];

				idealW[0 + j * 4] = w[0 + j * 4] + (difference / 4) / sourceIndex.openData.get(i);
				idealW[1 + j * 4] = w[1 + j * 4] + (difference / 4) / sourceIndex.highData.get(i);
				idealW[2 + j * 4] = w[2 + j * 4] + (difference / 4) / sourceIndex.lowData.get(i);
				idealW[3 + j * 4] = w[3 + j * 4] + (difference / 4) / sourceIndex.closeData.get(i);
				for (int k = 0; k < 4; k++) {
					w[k + j * 4] = (idealW[k + j * 4] + w[k + j * 4]) / 2;

					if (w[k + j * 4] > 1)
						w[k + j * 4] = 1;
					else if (w[k + j * 4] < 0)
						w[k + j * 4] = 0;
				}
			}
			System.out.println(i);
			System.out.println("calculated: " + calculated);
			System.out.println("target: " + target);
			System.out.println("difference: " + difference);
		}

		// position of element that is last index data of resourceIndex
		int lastElementPosition = sourceIndex.xData.size() - 1;
		// x-axis value that is first position of predictedIndex
		long x = sourceIndex.xData.get(lastElementPosition);

		// initial values
		predictedIndex.addData(x, sourceIndex.openData.get(lastElementPosition),
				sourceIndex.highData.get(lastElementPosition), sourceIndex.lowData.get(lastElementPosition),
				sourceIndex.closeData.get(lastElementPosition), x);

		// predicts 10 index data
		for (int i = 0; i < 10; i++) {
			x += 10;
			predictedIndex.addData(x,
					w[0] * predictedIndex.openData.get(i) + w[1] * predictedIndex.highData.get(i)
							+ w[2] * predictedIndex.lowData.get(i) + w[3] * predictedIndex.closeData.get(i),
					w[4] * predictedIndex.openData.get(i) + w[5] * predictedIndex.highData.get(i)
							+ w[6] * predictedIndex.lowData.get(i) + w[7] * predictedIndex.closeData.get(i),
					w[8] * predictedIndex.openData.get(i) + w[9] * predictedIndex.highData.get(i)
							+ w[10] * predictedIndex.lowData.get(i) + w[11] * predictedIndex.closeData.get(i),
					w[12] * predictedIndex.openData.get(i) + w[13] * predictedIndex.highData.get(i)
							+ w[14] * predictedIndex.lowData.get(i) + w[15] * predictedIndex.closeData.get(i),
					x);
		}
		return predictedIndex;
	}
}
