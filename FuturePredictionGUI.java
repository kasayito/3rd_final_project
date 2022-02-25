package trading;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//XChart
import org.knowm.xchart.*;
import org.knowm.xchart.style.*;

/**
 * GUI for FuturePrediction that creates new window for plotting different graph
 * 
 * @author kasay
 *
 */
public class FuturePredictionGUI {
	// given arguments
	private JFrame f;
	private IndexContainer sourceIndex;

	// predicted index data
	private FuturePrediction predicted;
	private IndexContainer predictedIndex;

	// components for button
	private JPanel predictionPanel;
	private JButton predictionButton;
	private OHLCChart predictedChart;

	/**
	 * initialization
	 */
	public FuturePredictionGUI(JFrame f, IndexContainer sourceData) {
		// initialization
		this.f = f;
		this.sourceIndex = sourceData;

		// JPanel for JButton on main JFrame of this application
		predictionPanel = new JPanel();
		predictionButton = new JButton("future prediction");
	}

	/**
	 * defines action event
	 */
	public void predictionEvent() {
		/**
		 * inner class
		 * 
		 * @author kasay
		 *
		 */
		class PredictionListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					predicted = new FuturePrediction();
					predictedIndex = predicted.algorithm(Main.getSource());

					predictedChart = new OHLCChartBuilder().width(800).height(600).title("index prediction").build();
					predictedChart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
					predictedChart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);

					predictedChart.addSeries("predicted index", predictedIndex.xData, predictedIndex.openData,
							predictedIndex.highData, predictedIndex.lowData, predictedIndex.closeData);

					predictedChart.getStyler().setToolTipsEnabled(true);

					/* not working */
					// predictedSwing = new SwingWrapper<OHLCChart>(predictedChart);
					// predictedSwing.displayChart();

					/* copied from original code instead of above */
					JFrame predictedGraphFrame = new JFrame();
					predictedGraphFrame.setDefaultCloseOperation(predictedGraphFrame.DISPOSE_ON_CLOSE);

					java.util.List<OHLCChart> charts = new ArrayList<OHLCChart>();
					charts.add(predictedChart);
					java.util.List<XChartPanel<OHLCChart>> chartPanels = new ArrayList<XChartPanel<OHLCChart>>();

					XChartPanel<OHLCChart> chartPanel = new XChartPanel<OHLCChart>(charts.get(0));
					chartPanels.add(chartPanel);
					predictedGraphFrame.add(chartPanel);

					predictedGraphFrame.pack();
					/*
					 * if (isCentered) { predictionFrame.setLocationRelativeTo(null); }
					 */
					predictedGraphFrame.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		// sets the action to the button
		predictionButton.addActionListener(new PredictionListener());
	}

	/**
	 * called when displaying graph of FuturePrediction
	 */
	public void setSwing() {
		// button action event
		predictionEvent();

		// adds the JPanel to the JFrame
		predictionPanel.add(predictionButton);
		f.add(predictionPanel, BorderLayout.WEST);
	}
}
