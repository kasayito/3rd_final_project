package trading;

import java.util.*;
import java.io.*;
import javax.swing.table.AbstractTableModel;

/**
 * the class that simulates trading
 * 
 * @author kasay
 *
 */
public class VirtualBankAccount extends AbstractTableModel implements Serializable {

	// referring IndexContainer
	private IndexContainer indexContainer;

	public double cash, cryptocurrency;
	public List<Object[]> history;

	/**
	 * initialization
	 * 
	 * @param indexContainer
	 */
	public VirtualBankAccount(IndexContainer indexContainer) {
		// initialization
		this.indexContainer = indexContainer;
		cash = 0;
		cryptocurrency = 0;
		history = new ArrayList<Object[]>();
	}

	/**
	 * @return Japanese yen per Bitcoin
	 */
	public double getRatio() {
		return indexContainer.closeData.get(indexContainer.closeData.size() - 1);
	}

	/* parameter amount [japanese yen] unit based */
	public void buy(double amount) {
		cash -= amount;
		cryptocurrency += amount / getRatio();

		Object[] transaction = { "buy", amount, cash, cryptocurrency };
		history.add(transaction);
	}

	public void sell(double amount) {
		cash += amount;
		cryptocurrency -= amount / getRatio();

		Object[] transaction = { "sell", amount, cash, cryptocurrency };
		history.add(transaction);
	}

	public void deposit(double amount) {
		cash += amount;

		Object[] transaction = { "deposit", amount, cash, cryptocurrency };
		history.add(transaction);
	}

	public void withdraw(double amount) {
		cash -= amount;

		Object[] transaction = { "withdraw", amount, cash, cryptocurrency };
		history.add(transaction);
	}

	public void reset() {
		cash = cryptocurrency = 0;
		history.clear();
	}

	/* AbstractTableModel */
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return history.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public void setValueAt(Object val, int rowIndex, int columnIndex) {
		history.get(rowIndex)[columnIndex] = val;
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return (Object) history.get(rowIndex)[columnIndex];
	}

	@Override
	public String getColumnName(int col) {
		String[] cmn = { "transaction", "transaction amount", "current cash", "current cryptocurrency" };
		return cmn[col];
	}
}
