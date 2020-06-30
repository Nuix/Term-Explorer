package com.nuix.termexplorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.nuix.superutilities.loadfiles.SimpleTextFileWriter;
import com.nuix.superutilities.query.QueryHelper;

import nuix.Window;

@SuppressWarnings("serial")
public class TermCollectionTable extends JPanel {
	private JTable table;
	private TermCollectionTableModel model = new TermCollectionTableModel();
	private JComboBox<String> termOperator;
	private JLabel lblTermCount;
	
	public TermCollectionTable(Window nuixWindow) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Query", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 3;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar_1 = new JToolBar();
		toolBar_1.setFloatable(false);
		panel.add(toolBar_1);
		
		JButton btnSearch = new JButton("Search");
		toolBar_1.add(btnSearch);
		btnSearch.setToolTipText("Execute Query");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(hasNoTerms()) {
					CommonDialogs.showError("You currently have no terms in your list.", "No Terms");
				} else {
					Map<String,Object> tabSettings = new HashMap<String,Object>();
					tabSettings.put("search", buildQuery());
					nuixWindow.openTab("workbench", tabSettings);	
				}
			}
		});
		btnSearch.setIcon(new ImageIcon(TermCollectionTable.class.getResource("/com/nuix/termexplorer/magnifier.png")));
		
		JButton btnCopyQuery = new JButton("Copy");
		toolBar_1.add(btnCopyQuery);
		btnCopyQuery.setToolTipText("Copy Query");
		btnCopyQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(hasNoTerms()) {
					CommonDialogs.showError("You currently have no terms in your list.", "No Terms");
				} else {
					StringSelection query = new StringSelection(buildQuery());
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(query, null);
				}
			}
		});
		btnCopyQuery.setIcon(new ImageIcon(TermCollectionTable.class.getResource("/com/nuix/termexplorer/page_copy.png")));
		
		JButton btnSaveQuery = new JButton("Save");
		toolBar_1.add(btnSaveQuery);
		btnSaveQuery.setToolTipText("Save Query");
		btnSaveQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(hasNoTerms()) {
					CommonDialogs.showError("You currently have no terms in your list.", "No Terms");
				} else {
					File textFile = CommonDialogs.saveFileDialog("C:\\", "Text File (*.txt)", "txt", "Save Query");
					if(textFile != null) {
						try(SimpleTextFileWriter sw = new SimpleTextFileWriter(textFile)){
							sw.writeLine(buildQuery());
						} catch (IOException e1) {
							CommonDialogs.showError("Error saving query to text file: "+e1.getMessage());
						}
					}
				}
			}
		});
		btnSaveQuery.setIcon(new ImageIcon(TermCollectionTable.class.getResource("/com/nuix/termexplorer/page_save.png")));
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		toolBar_1.add(horizontalStrut);
		
		JLabel lblNewLabel = new JLabel("Query Type:");
		toolBar_1.add(lblNewLabel);
		
		termOperator = new JComboBox<String>();
		termOperator.setToolTipText("Query Type");
		toolBar_1.add(termOperator);
		termOperator.setFont(new Font("Consolas", Font.PLAIN, 11));
		termOperator.setModel(new DefaultComboBoxModel<String>(new String[] {"a OR b OR c", "a AND b AND c", "NOT (a OR b OR c)", "content:(a OR b OR c)", "properties:(a OR b OR c)", "content:(a OR b) OR properties(a OR B)", "NOT content:(a OR b OR c)", "NOT properties:(a OR b OR c)", "NOT (content:(a OR b) OR properties(a OR B))"}));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Terms", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		add(panel_1, gbc_panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar_2 = new JToolBar();
		toolBar_2.setFloatable(false);
		panel_1.add(toolBar_2, BorderLayout.CENTER);
		
		JButton btnCopyTerms = new JButton("Copy");
		toolBar_2.add(btnCopyTerms);
		btnCopyTerms.setToolTipText("Copy Terms");
		btnCopyTerms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(hasNoTerms()) {
					CommonDialogs.showError("You currently have no terms in your list.", "No Terms");
				} else {
					StringJoiner termList = new StringJoiner("\n");
					for(String term : model.getTerms()) {
						termList.add(term);
					}
					StringSelection termListString = new StringSelection(termList.toString());
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(termListString, null);
				}
			}
		});
		btnCopyTerms.setIcon(new ImageIcon(TermCollectionTable.class.getResource("/com/nuix/termexplorer/page_white_copy.png")));
		
		JButton btnSaveTerms = new JButton("Save");
		toolBar_2.add(btnSaveTerms);
		btnSaveTerms.setToolTipText("Save Terms");
		btnSaveTerms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(hasNoTerms()) {
					CommonDialogs.showError("You currently have no terms in your list.", "No Terms");
				} else {
					File textFile = CommonDialogs.saveFileDialog("C:\\", "Text File (*.txt)", "txt", "Save Term List");
					if(textFile != null) {
						try(SimpleTextFileWriter sw = new SimpleTextFileWriter(textFile)){
							for(String term : model.getTerms()) {
								sw.writeLine(term);
							}
						} catch (IOException e1) {
							CommonDialogs.showError("Error saving terms to text file: "+e1.getMessage());
						}
					}
				}
			}
		});
		btnSaveTerms.setIcon(new ImageIcon(TermCollectionTable.class.getResource("/com/nuix/termexplorer/table_save.png")));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Remove", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 1;
		gbc_panel_2.gridy = 1;
		add(panel_2, gbc_panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar_3 = new JToolBar();
		toolBar_3.setFloatable(false);
		panel_2.add(toolBar_3, BorderLayout.CENTER);
		
		JButton btnRemoveSelected = new JButton("Selected");
		toolBar_3.add(btnRemoveSelected);
		btnRemoveSelected.setToolTipText("Remove Selected");
		btnRemoveSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(hasTerms()) {
					int[] selectedRows = table.getSelectedRows();
					if(selectedRows.length > 0) {
						String message = String.format("Removed selected %s terms?",selectedRows.length);
						if(CommonDialogs.getConfirmation(message, "Remove Selected Terms?")) {
							model.removeTerms(selectedRows);
							lblTermCount.setText("Term Count: "+model.getTermCount());
						}	
					}
				}
			}
		});
		btnRemoveSelected.setIcon(new ImageIcon(TermCollectionTable.class.getResource("/com/nuix/termexplorer/table_row_delete.png")));
		
		JButton btnRemoveAll = new JButton("All");
		toolBar_3.add(btnRemoveAll);
		btnRemoveAll.setToolTipText("Remove All");
		btnRemoveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(hasTerms()) {
					if(CommonDialogs.getConfirmation("Remove all terms?", "Remove All Terms?")) {
						model.clear();
						lblTermCount.setText("Term Count: "+model.getTermCount());	
					}
				}
			}
		});
		btnRemoveAll.setIcon(new ImageIcon(TermCollectionTable.class.getResource("/com/nuix/termexplorer/bin_closed.png")));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 4;
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

	public void addTerm(String term) {
		model.addTerm(term);
		lblTermCount.setText("Term Count: "+model.getTermCount());
	}

	public void addTerms(Collection<String> terms) {
		model.addTerms(terms);
		lblTermCount.setText("Term Count: "+model.getTermCount());
	}
	
	public boolean hasNoTerms() {
		return model.getTermCount() == 0;
	}
	
	public boolean hasTerms() {
		return model.getTermCount() > 0;
	}

	public String buildQuery() {
//		0: a OR b OR c
//		1: a AND b AND c
//		2: NOT (a OR b OR c)
//		3: content:(a OR b OR c)
//		4: properties:(a OR b OR c)
//		5: content:(a OR b) OR properties(a OR B)
//		6: NOT content:(a OR b OR c)
//		7: NOT properties:(a OR b OR c)
//		8: NOT (content:(a OR b) OR properties(a OR B))
		String query = "";
		String termsORed = QueryHelper.joinByOr(model.getTerms());
		int opIndex = termOperator.getSelectedIndex();
		switch(opIndex) {
		case 0:
			query = termsORed;
			break;
		case 1:
			query = QueryHelper.joinByAnd(model.getTerms());
			break;
		case 2:
			query = QueryHelper.notJoinByOr(model.getTerms());
			break;
		case 3:
			query = String.format("content:(%s)", QueryHelper.joinByOr(model.getTerms()));
			break;
		case 4:
			query = String.format("properties:(%s)", QueryHelper.joinByOr(model.getTerms()));
			break;
		case 5:
			query = String.format("content:(%s) OR properties:(%s)", termsORed, termsORed);
			break;
		case 6:
			query = String.format("NOT content:(%s)", QueryHelper.joinByOr(model.getTerms()));
			break;
		case 7:
			query = String.format("NOT properties:(%s)", QueryHelper.joinByOr(model.getTerms()));
			break;
		case 8:
			query = String.format("NOT (content:(%s) OR properties:(%s))", termsORed, termsORed);
			break;
		}
		return query;
	}
}
