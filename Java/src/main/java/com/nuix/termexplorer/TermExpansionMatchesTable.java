package com.nuix.termexplorer;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JToolBar;
import java.awt.GridBagConstraints;
import javax.swing.JScrollPane;
import java.awt.Insets;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import com.nuix.superutilities.misc.ExpandedTermInfo;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class TermExpansionMatchesTable extends JPanel {
	private JTable table;
	private TermExpansionMatchesTableModel model = new TermExpansionMatchesTableModel();
	private Consumer<List<ExpandedTermInfo>> sendToCollectionCallback = null;
	
	public TermExpansionMatchesTable() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.insets = new Insets(0, 0, 5, 0);
		gbc_toolBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolBar.gridx = 0;
		gbc_toolBar.gridy = 0;
		add(toolBar, gbc_toolBar);
		
		JButton btnCheckAll = new JButton("Check All");
		btnCheckAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.checkAllRecords();
			}
		});
		btnCheckAll.setIcon(new ImageIcon(TermExpansionMatchesTable.class.getResource("/com/nuix/termexplorer/accept.png")));
		toolBar.add(btnCheckAll);
		
		JButton btnUncheckAll = new JButton("Uncheck All");
		btnUncheckAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.uncheckAllRecords();
			}
		});
		btnUncheckAll.setIcon(new ImageIcon(TermExpansionMatchesTable.class.getResource("/com/nuix/termexplorer/unaccept.png")));
		toolBar.add(btnUncheckAll);
		
		JButton btnAddToCollection = new JButton("Add to Collection");
		btnAddToCollection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(sendToCollectionCallback != null) {
					sendToCollectionCallback.accept(model.getCheckedRecords());
				}
			}
		});
		btnAddToCollection.setIcon(new ImageIcon(TermExpansionMatchesTable.class.getResource("/com/nuix/termexplorer/arrow_right.png")));
		toolBar.add(btnAddToCollection);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		add(scrollPane, gbc_scrollPane);
		
		table = new JTable(model);
		table.setFillsViewportHeight(true);
		scrollPane.setViewportView(table);
		
		table.getColumnModel().getColumn(0).setWidth(30);
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(0).setMaxWidth(30);
	}
	
	public void onSendToCollection(Consumer<List<ExpandedTermInfo>> callback) {
		sendToCollectionCallback = callback;
	}

	public void setMatchedTerms(List<ExpandedTermInfo> matchedTerms) {
		model.setMatchedTerms(matchedTerms);
	}
}
