package com.nuix.termexplorer;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import com.nuix.superutilities.misc.ExpandedTermInfo;

@SuppressWarnings("serial")
public class TermExpansionMatchesTable extends JPanel {
	private JTable table;
	private TermExpansionMatchesTableModel model = new TermExpansionMatchesTableModel();
	private Consumer<List<ExpandedTermInfo>> sendToCollectionCallback = null;
	private JLabel lblTermCount;
	
	public TermExpansionMatchesTable() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{110, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
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
		
		JButton btnAddToCollection = new JButton("Add Selected");
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
		
		JButton btnAddAllTo = new JButton("Add All");
		btnAddAllTo.setToolTipText("Add All to Collection");
		btnAddAllTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendToCollectionCallback.accept(model.getRecords());
			}
		});
		btnAddAllTo.setIcon(new ImageIcon(TermExpansionMatchesTable.class.getResource("/com/nuix/termexplorer/control_fastforward_blue.png")));
		toolBar.add(btnAddAllTo);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Export", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 0;
		add(panel_1, gbc_panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar_1 = new JToolBar();
		toolBar_1.setFloatable(false);
		panel_1.add(toolBar_1);
		
		JButton btnNewButton = new JButton("Export CSV");
		btnNewButton.setIcon(new ImageIcon(TermExpansionMatchesTable.class.getResource("/com/nuix/termexplorer/table_save.png")));
		btnNewButton.addActionListener(new ActionListener() {
			public String toCsv(String... values) {
				StringJoiner result = new StringJoiner(",");
				for (int i = 0; i < values.length; i++) { result.add("\""+values[i]+"\""); }
				return result.toString();
			}
			
			public void actionPerformed(ActionEvent evnt) {
				File outputCsvFile = CommonDialogs.saveFileDialog("C:\\", "Comma Separated Values (*.csv)", "csv", "Save Matched Terms CSV");
				if(outputCsvFile != null) {
					FileOutputStream fos = null;
					BufferedWriter bw = null;
					try {
						fos = new FileOutputStream(outputCsvFile);
						bw = new BufferedWriter(new OutputStreamWriter(fos));
						// Write headers
						bw.write(toCsv("Original Expression","Matched Term","Highest Similarity","Occurrences"));
						bw.newLine();
						
						// Write each expanded term info
						for(ExpandedTermInfo eti : model.getRecords()) {
							bw.write(toCsv(
								eti.getOriginalTerm(),
								eti.getMatchedTerm(),
								Float.toString(eti.getSimilarity()),
								Long.toString(eti.getOcurrences())
							));
							bw.newLine();
						}
						bw.close();
						fos.close();
						
						CommonDialogs.showMessage("CSV saved to "+outputCsvFile.getAbsolutePath(), "CSV Saved");
					} catch (Exception e) {
						CommonDialogs.showError("Error while saving CSV: "+e.getMessage(), "Error Saving CSV");
					} 
				}
			}
		});
		toolBar_1.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("Note: You can select rows in the table below and then copy and past them as well (On Windows CTRL+C / CTRL+V)");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 3;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		add(lblNewLabel, gbc_lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		add(scrollPane, gbc_scrollPane);
		
		table = new JTable(model);
		table.setFillsViewportHeight(true);
		scrollPane.setViewportView(table);
		
		lblTermCount = new JLabel("Term Count: 0");
		GridBagConstraints gbc_lblTermCount = new GridBagConstraints();
		gbc_lblTermCount.insets = new Insets(0, 0, 0, 5);
		gbc_lblTermCount.anchor = GridBagConstraints.WEST;
		gbc_lblTermCount.gridx = 0;
		gbc_lblTermCount.gridy = 3;
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
