package trading;

import java.util.*;
import javax.swing.*;

/**
 * this class has a responsibility to do execution of each GUI, i.e.) determines
 * whether/when the GUI of each functionalities are provided
 * 
 * @author kasay
 *
 */
public class GUI {

	// GraphGUI
	private String title;
	private List<IndexContainer> containerList;
	// JFrame of main window of this application
	private JFrame f;

	// VirtualBankAccountGUI
	private VirtualBankAccount serialized;

	// FuturePredictionGUI
	private FuturePrediction predicted;

	/**
	 * initialization
	 * 
	 * @param title
	 */
	public GUI(String title, List<IndexContainer> containerList, VirtualBankAccount serialized) {
		// initialization
		this.title = title;
		this.containerList = containerList;
		this.serialized = serialized;
	}

	/**
	 * sets up all GUI
	 */
	public void setupWindow() {
		// the GUI for graphs
		graphs();

		// the GUI for VirtualBankAccount
		account();

		// the GUI for FuturePrediction
		prediction();
	}

	/**
	 * graphs for normal fetched index data
	 */
	public void graphs() {
		// initialization
		GraphGUI graphGUI = new GraphGUI(title, containerList);

		// //sets up Swing of graphGUI, and f is automatically created JFrame that is
		// main window of this application
		f = graphGUI.setSwing();

		// threading for updating graphs based on IndexContainer
		graphGUI.start();
	}

	/**
	 * account of VirtualBankAccount
	 */
	public void account() {
		// initialization
		VirtualBankAccountGUI accountGUI = new VirtualBankAccountGUI(f, serialized);

		// sets up Swing of accountGUI
		accountGUI.setSwing();
	}

	/**
	 * graph of FuturePrediction
	 */
	public void prediction() {
		// initialization
		FuturePredictionGUI predictionGUI = new FuturePredictionGUI(f, containerList.get(1));

		// sets up Swing of predictionGUI
		predictionGUI.setSwing();
	}
}
