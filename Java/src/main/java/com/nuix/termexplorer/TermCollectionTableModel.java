package com.nuix.termexplorer;

import java.util.ArrayList;
import java.util.Collection;
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
		for (int r = 0; r < rowIndices.length; r++) {
			String term = terms.get(r);
			distinctTerms.remove(term);
			terms.remove(r);
			this.fireTableRowsDeleted(r, r);
		}
	}
	
	public void addTerm(String term) {
		if(!distinctTerms.contains(term)) {
			terms.add(term);
			distinctTerms.add(term);
			int newRowIndex = terms.size()-1;
			this.fireTableRowsInserted(newRowIndex, newRowIndex);
		}
	}
	
	public void addTerms(Collection<String> terms) {
		for(String term : terms) {
			addTerm(term);
		}
	}
}
