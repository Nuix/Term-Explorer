package com.nuix.termexplorer;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import com.nuix.superutilities.misc.ExpandedTermInfo;

@SuppressWarnings("serial")
public class TermExpansionMatchesTableModel extends DefaultTableModel {
	String[] headers = new String[] {
			"Originating Expressions",
			"Matched Term",
			"Similarity",
			"Occurrences",
	};
	
	private List<ExpandedTermInfo> matchedTerms = new ArrayList<ExpandedTermInfo>();
	
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
			case 0: return matchedTerm.getOriginalTerm();
			case 1: return matchedTerm.getMatchedTerm();
			case 2:
				if(matchedTerm.getSimilarity() < 0.0f) { return ""; }
				else { return String.format("%.3f", matchedTerm.getSimilarity()); }
			case 3:
				if(matchedTerm.getOcurrences() < 0) {return ""; }
				else { return Long.toString(matchedTerm.getOcurrences()); }
			default: return "?";
		}
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch(columnIndex) {
			case 0: return String.class;
			case 1: return String.class;
			case 2: return String.class;
			case 3: return String.class;
			default: return String.class;
		}
	}
	
	public void setMatchedTerms(List<ExpandedTermInfo> matchedTerms) {
		this.matchedTerms = matchedTerms;
		this.fireTableDataChanged();
	}
	
	public List<ExpandedTermInfo> getRecords(int[] rowIndices){
		List<ExpandedTermInfo> result = new ArrayList<ExpandedTermInfo>();
		for (int i = 0; i < rowIndices.length; i++) {
			result.add(matchedTerms.get(rowIndices[i]));
		}
		return result;
	}
	
	public List<ExpandedTermInfo> getRecords(){
		List<ExpandedTermInfo> result = new ArrayList<ExpandedTermInfo>();
		result.addAll(matchedTerms);
		return result;
	}
}
