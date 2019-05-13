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
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;

@SuppressWarnings("serial")
public class TermExpansionMatchesTable extends JPanel {
	private JTable table;
	private TermExpansionMatchesTableModel model = new TermExpansionMatchesTableModel();
	private Consumer<List<ExpandedTermInfo>> sendToCollectionCallback = null;
	private JLabel lblTermCount;
	
	public TermExpansionMatchesTable() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{110, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Add to Collection", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar = new JToolBar();
		panel.add(toolBar, BorderLayout.CENTER);
		toolBar.setFloatable(false);
		
		JButton btnAddToCollection = new JButton("");
		btnAddToCollection.setToolTipText("Add Selected to Collection");
		btnAddToCollection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(sendToCollectionCallback != null) {
					int[] rowIndices = table.getSelectedRows();
					sendToCollectionCallback.accept(model.getRecords(rowIndices));
				}
			}
		});
		btnAddToCollection.setIcon(new ImageIcon(TermExpansionMatchesTable.class.getResource("/com/nuix/termexplorer/control_play_blue.png")));
		toolBar.add(btnAddToCollection);
		
		JButton btnAddAllTo = new JButton("");
		btnAddAllTo.setToolTipText("Add All to Collection");
		btnAddAllTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendToCollectionCallback.accept(model.getRecords());
			}
		});
		btnAddAllTo.setIcon(new ImageIcon(TermExpansionMatchesTable.class.getResource("/com/nuix/termexplorer/control_fastforward_blue.png")));
		toolBar.add(btnAddAllTo);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		add(scrollPane, gbc_scrollPane);
		
		table = new JTable(model);
		table.setFillsViewportHeight(true);
		scrollPane.setViewportView(table);
		
		lblTermCount = new JLabel("Term Count: 0");
		GridBagConstraints gbc_lblTermCount = new GridBagConstraints();
		gbc_lblTermCount.insets = new Insets(0, 0, 0, 5);
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
