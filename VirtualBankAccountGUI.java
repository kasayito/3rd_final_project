package trading;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * GUI for VirtualBankAccount
 * 
 * @author kasay
 *
 */
public class VirtualBankAccountGUI {
	// given arguments
	private JFrame f;
	private VirtualBankAccount serialized;

	// transactionEvent and historyEvent
	private JPanel accountPanel;
	private static final String[] option = { "buy", "sell", "deposit", "withdraw", "reset" };
	private JComboBox cb;
	private JTextField t;
	private JButton transactionButton;
	private JButton historyButton;
	private JPanel historyPanel;
	private JTable table;
	private JScrollPane sc;

	/**
	 * initialization
	 * 
	 * @param f
	 * @param serialized
	 */
	public VirtualBankAccountGUI(JFrame f, VirtualBankAccount serialized) {
		// initialization
		this.f = f;
		this.serialized = serialized;

		// JPanel for JComboBox, JTextField and JButton
		accountPanel = new JPanel();
		// transaction
		transactionButton = new JButton("do transaction");
		cb = new JComboBox(option);
		t = new JTextField();
		// history
		historyButton = new JButton("save and show account history");
		historyPanel = new JPanel();
		table = new JTable(serialized);
		sc = new JScrollPane(table);
	}

	/**
	 * defines action event
	 */
	public void transactionEvent() {
		/**
		 * inner class
		 * 
		 * @author kasay
		 *
		 */
		class TransactionActionListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String transactionType = (String) cb.getSelectedItem();

				// if type is not reset, then calculates amount of transaction
				if (transactionType.equals("reset"))
					serialized.reset();
				else {
					double amount = Double.parseDouble(t.getText());
					switch (transactionType) {
					case "buy":
						serialized.buy(amount);
						break;
					case "sell":
						serialized.sell(amount);
						break;
					case "deposit":
						serialized.deposit(amount);
						break;
					case "withdraw":
						serialized.withdraw(amount);
						break;
					}
				}
			}
		}

		// sets the action to the button
		transactionButton.addActionListener(new TransactionActionListener());
	}

	/**
	 * defines action event
	 */
	public void historyEvent() {
		/**
		 * inner class
		 * 
		 * @author kasay
		 *
		 */
		class HistoryActionListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent ae) {
				// shows table
				table.setFillsViewportHeight(true);
				serialized.fireTableDataChanged();

				// saves
				Main.save();
			}
		}

		// sets the action to the button
		historyButton.addActionListener(new HistoryActionListener());
	}

	/**
	 * called when displaying history of VirtualBankAccount
	 */
	public void setSwing() {
		// button action event
		transactionEvent();
		historyEvent();

		// adds transaction components to the panel
		t.setPreferredSize(new Dimension(72, 24));
		accountPanel.add(cb);
		accountPanel.add(t);
		accountPanel.add(transactionButton);

		// adds history components to the panel
		accountPanel.add(historyButton);
		historyPanel.add(table);

		// adds the JPanel to the JFrame
		f.add(accountPanel, BorderLayout.NORTH);
		f.add(historyPanel, BorderLayout.EAST);
	}

}
