package trading;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//XChart
import org.knowm.xchart.*;
import org.knowm.xchart.style.*;

/**
 * main GUI for graph plotting
 * 
 * @author kasay
 *
 */
public class GraphGUI extends Thread {
	// given arguments
	private java.util.List<IndexContainer> containerList;

	// JFrame of main window of this application
	private JFrame f;

	// visibilityEvent
	private JPanel visibilityPanel;
	private JButton visibilityButton;

	// XChart Swing
	private SwingWrapper<OHLCChart> chartGUI;
	private OHLCChart chart;
	private java.util.List<IndexContainer> referredContainers;
	private java.util.List<OHLCSeries> referredSeries;

	/**
	 * initialization
	 * 
	 * @param title
	 * @param containerList
	 */
	public GraphGUI(String title, java.util.List<IndexContainer> containerList) {
		// initialization
		this.containerList = containerList;

		// sets up for toggling visibility of series 0 when the button was pressed
		visibilityPanel = new JPanel();
		visibilityButton = new JButton("enable/disable visibility of series 0");

		// memory allocation
		chart = new OHLCChartBuilder().width(800).height(600).title(title).build();
		referredSeries = new ArrayList<OHLCSeries>();
		referredContainers = new ArrayList<IndexContainer>();
	}

	/**
	 * called when setting IndexContainer and OHLCSeries before setSwing
	 */
	private void setData() {
		// sets up for referredContainer
		referredContainers.addAll(containerList);

		// sets up for referredSeries
		for (int i = 0; i < referredContainers.size(); i++)
			referredSeries.add(chart.addSeries("series " + i, referredContainers.get(i).xData,
					referredContainers.get(i).openData, referredContainers.get(i).highData,
					referredContainers.get(i).lowData, referredContainers.get(i).closeData));
	}

	/**
	 * defines action event
	 */
	public void visibilityEvent() {
		/**
		 * inner class that defines action of the button
		 * 
		 * @author kasay
		 *
		 */
		class VisibilityActionListener implements ActionListener {
			// if enabled currently => disable
			// if disabled currently => enable
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (((Boolean) referredSeries.get(0).isEnabled()).equals(true))
					referredSeries.get(0).setEnabled(false);
				else if (((Boolean) referredSeries.get(0).isEnabled()).equals(false))
					referredSeries.get(0).setEnabled(true);
			}
		}

		// sets the action to the button
		visibilityButton.addActionListener(new VisibilityActionListener());
	}

	/**
	 * called when drawing the graphs
	 */
	public JFrame setSwing() {
		// sets up referredContainers and referredSeries
		setData();

		// button action event
		visibilityEvent();

		// sets up chart
		chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
		chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
		chart.getStyler().setToolTipsEnabled(true);
		chartGUI = new SwingWrapper<OHLCChart>(chart);

		// adds the JPanel to the JFrame
		visibilityPanel.add(visibilityButton);
		f = chartGUI.displayChart();
		f.add(visibilityPanel, BorderLayout.SOUTH);

		// JFrame of main window of this application
		return f;
	}

	/**
	 * entry point when the GUI class starts threading of this class
	 */
	@Override
	public void run() {
		while (true) {
			// accesses to the all containers and updates current referred series
			for (int i = 0; i < referredContainers.size(); i++)
				chart.updateOHLCSeries("series " + i, referredContainers.get(i).xData,
						referredContainers.get(i).openData, referredContainers.get(i).highData,
						referredContainers.get(i).lowData, referredContainers.get(i).closeData);

			// repaints the chart
			chartGUI.repaintChart();

			// sleeps one second to avoid crash
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
