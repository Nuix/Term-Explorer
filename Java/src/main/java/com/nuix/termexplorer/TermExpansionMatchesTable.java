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
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class TermExpansionMatchesTable extends JPanel {
	private JTable table;
	private TermExpansionMatchesTableModel model = new TermExpansionMatchesTableModel();
	private Consumer<List<ExpandedTermInfo>> sendToCollectionCallback = null;
	private JLabel lblTermCount;
	
	public TermExpansionMatchesTable() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.insets = new Insets(0, 0, 5, 0);
		gbc_toolBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolBar.gridx = 0;
		gbc_toolBar.gridy = 0;
		add(toolBar, gbc_toolBar);
		
		JButton btnAddToCollection = new JButton("Add Selected to Collection");
		btnAddToCollection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(sendToCollectionCallback != null) {
					int[] rowIndices = table.getSelectedRows();
					sendToCollectionCallback.accept(model.getRecords(rowIndices));
				}
			}
		});
		btnAddToCollection.setIcon(new ImageIcon(TermExpansionMatchesTable.class.getResource("/com/nuix/termexplorer/arrow_right.png")));
		toolBar.add(btnAddToCollection);
		
		JButton btnAddAllTo = new JButton("Add All to Collection");
		btnAddAllTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendToCollectionCallback.accept(model.getRecords());
			}
		});
		btnAddAllTo.setIcon(new ImageIcon(TermExpansionMatchesTable.class.getResource("/com/nuix/termexplorer/arrow_right.png")));
		toolBar.add(btnAddAllTo);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		add(scrollPane, gbc_scrollPane);
		
		table = new JTable(model);
		table.setFillsViewportHeight(true);
		scrollPane.setViewportView(table);
		
		lblTermCount = new JLabel("Term Count: 0");
		GridBagConstraints gbc_lblTermCount = new GridBagConstraints();
		gbc_lblTermCount.anchor = GridBagConstraints.WEST;
		gbc_lblTermCount.gridx = 0;
		gbc_lblTermCount.gridy = 2;
		add(lblTermCount, gbc_lblTermCount);
	}
	
	public void onSendToCollection(Consumer<List<ExpandedTermInfo>> callback) {
		sendToCollectionCallback = callback;
	}

	public void setMatchedTerms(List<ExpandedTermInfo> matchedTerms) {
		model.setMatchedTerms(matchedTerms);
		lblTermCount.setText("Term Count: "+matchedTerms.size());
	}
}
