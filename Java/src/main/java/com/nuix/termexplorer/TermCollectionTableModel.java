package com.nuix.termexplorer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class TermCollectionTableModel extends DefaultTableModel {
	String[] headers = new String[] {
			"Term",
	};

	Set<String> distinctTerms = new HashSet<String>();
	List<String> terms = new ArrayList<String>();
	
	@Override
	public int getRowCount() {
		if(terms == null) { return 0; }
		else { return terms.size(); }
	}

	@Override
	public int getColumnCount() { return headers.length; }

	@Override
	public String getColumnName(int column) { return headers[column]; }

	@Override
	public boolean isCellEditable(int row, int column) { return false; }

	@Override
	public Object getValueAt(int row, int column) {
		return terms.get(row);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}
	
	public void clear() {
		distinctTerms.clear();
		terms.clear();
		this.fireTableDataChanged();
	}
	
	public List<String> getTerms(){
		List<String> result = new ArrayList<String>();
		result.addAll(terms);
		return result;
	}
	
	public int getTermCount() {
		return terms.size();
	}
	
	public void removeTerms(int[] rowIndices) {
		Arrays.sort(rowIndices);
		for (int r = rowIndices.length-1; r >= 0 ; r--) {
			int rowIndex = rowIndices[r];
			String term = terms.get(rowIndex);
			distinctTerms.remove(term);
			terms.remove(rowIndex);
			this.fireTableRowsDeleted(rowIndex, rowIndex);
		}
	}
	
	public void addTerm(String term) {
		if(!distinctTerms.contains(term)) {
			terms.add(term);
			distinctTerms.add(term);
		}
		Collections.sort(terms);
		this.fireTableDataChanged();
	}
	
	public void addTerms(Collection<String> terms) {
		for(String term : terms) {
			if(!distinctTerms.contains(term)) {
				terms.add(term);
				distinctTerms.add(term);
			}
		}
		Collections.sort(this.terms);
		this.fireTableDataChanged();
	}
}
