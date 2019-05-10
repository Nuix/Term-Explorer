package com.nuix.termexplorer;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import com.nuix.superutilities.misc.ExpandedTermInfo;

@SuppressWarnings("serial")
public class TermExpansionMatchesTableModel extends DefaultTableModel {
	String[] headers = new String[] {
			" ",
			"Matched Term",
			"Similarity",
			"Occurrences",
	};
	
	private List<ExpandedTermInfo> matchedTerms = new ArrayList<ExpandedTermInfo>();
	private List<Boolean> recordCheckState = new ArrayList<Boolean>();
	
	@Override
	public int getRowCount() {
		if(matchedTerms == null) { return 0; }
		else { return matchedTerms.size(); } 
	}
	
	@Override
	public int getColumnCount() { return headers.length; }
	
	@Override
	public String getColumnName(int column) {
		return headers[column];
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return column == 0;
	}
	
	@Override
	public Object getValueAt(int row, int column) {
		ExpandedTermInfo matchedTerm = matchedTerms.get(row);
		switch(column) {
			case 0: return recordCheckState.get(row);
			case 1: return matchedTerm.getMatchedTerm();
			case 2: return matchedTerm.getSimilarity();
			case 3: return matchedTerm.getOcurrences();
			default: return "?";
		}
	}
	
	@Override
	public void setValueAt(Object aValue, int row, int column) {
		if(column == 0) {
			recordCheckState.set(row, (Boolean)aValue);
		}
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch(columnIndex) {
			case 0: return Boolean.class;
			case 1: return String.class;
			case 2: return Float.class;
			case 3: return Long.class;
			default: return String.class;
		}
	}
	
	public void setMatchedTerms(List<ExpandedTermInfo> matchedTerms) {
		recordCheckState.clear();
		for (int i = 0; i < matchedTerms.size(); i++) {
			recordCheckState.add(false);
		}
		this.matchedTerms = matchedTerms;
		this.fireTableDataChanged();
	}
	
	public void checkAllRecords() {
		for (int i = 0; i < recordCheckState.size(); i++) {
			recordCheckState.set(i, true);
			this.fireTableCellUpdated(i, 0);
		}
	}
	
	public void uncheckAllRecords() {
		for (int i = 0; i < recordCheckState.size(); i++) {
			recordCheckState.set(i, false);
			this.fireTableCellUpdated(i, 0);
		}
	}
	
	public List<ExpandedTermInfo> getCheckedRecords(){
		List<ExpandedTermInfo> result = new ArrayList<ExpandedTermInfo>();
		for (int i = 0; i < matchedTerms.size(); i++) {
			if(recordCheckState.get(i) == true) {
				result.add(matchedTerms.get(i));
			}
		}
		return result;
	}
}
