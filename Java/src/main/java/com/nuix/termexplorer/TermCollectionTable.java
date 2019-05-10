package com.nuix.termexplorer;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JToolBar;
import java.awt.GridBagConstraints;
import javax.swing.JScrollPane;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.nuix.superutilities.loadfiles.SimpleTextFileWriter;
import com.nuix.superutilities.query.QueryHelper;

import nuix.Window;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.Font;

@SuppressWarnings("serial")
public class TermCollectionTable extends JPanel {
	private JTable table;
	private TermCollectionTableModel model = new TermCollectionTableModel();
	private JComboBox<String> termOperator;
	
	public TermCollectionTable(Window nuixWindow) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.anchor = GridBagConstraints.WEST;
		gbc_toolBar.insets = new Insets(0, 0, 5, 0);
		gbc_toolBar.gridx = 0;
		gbc_toolBar.gridy = 0;
		add(toolBar, gbc_toolBar);
		
		JButton btnSearch = new JButton("Search Query");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Map<String,Object> tabSettings = new HashMap<String,Object>();
				tabSettings.put("search", buildQuery());
				nuixWindow.openTab("workbench", tabSettings);
			}
		});
		btnSearch.setIcon(new ImageIcon(TermCollectionTable.class.getResource("/com/nuix/termexplorer/magnifier.png")));
		toolBar.add(btnSearch);
		
		JButton btnCopyQuery = new JButton("Copy Query");
		btnCopyQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StringSelection query = new StringSelection(buildQuery());
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(query, null);
			}
		});
		btnCopyQuery.setIcon(new ImageIcon(TermCollectionTable.class.getResource("/com/nuix/termexplorer/page_copy.png")));
		toolBar.add(btnCopyQuery);
		
		JButton btnSaveQuery = new JButton("Save Query");
		btnSaveQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File textFile = CommonDialogs.saveFileDialog("C:\\", "Text File (*.txt)", "txt", "Save Query");
				if(textFile != null) {
					try(SimpleTextFileWriter sw = new SimpleTextFileWriter(textFile)){
						sw.writeLine(buildQuery());
					} catch (IOException e1) {
						CommonDialogs.showError("Error saving query to text file: "+e1.getMessage());
					}
				}
			}
		});
		btnSaveQuery.setIcon(new ImageIcon(TermCollectionTable.class.getResource("/com/nuix/termexplorer/page_save.png")));
		toolBar.add(btnSaveQuery);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		toolBar.add(separator);
		
		JLabel lblTermOperator = new JLabel("Query Type:");
		lblTermOperator.setBorder(new EmptyBorder(0, 10, 0, 5));
		toolBar.add(lblTermOperator);
		
		termOperator = new JComboBox<String>();
		termOperator.setFont(new Font("Consolas", Font.PLAIN, 11));
		termOperator.setModel(new DefaultComboBoxModel<String>(new String[] {"a OR b OR c", "a AND b AND c", "NOT (a OR b OR c)"}));
		toolBar.add(termOperator);
		
		JToolBar toolBar_1 = new JToolBar();
		toolBar_1.setFloatable(false);
		GridBagConstraints gbc_toolBar_1 = new GridBagConstraints();
		gbc_toolBar_1.anchor = GridBagConstraints.WEST;
		gbc_toolBar_1.insets = new Insets(0, 0, 5, 0);
		gbc_toolBar_1.gridx = 0;
		gbc_toolBar_1.gridy = 1;
		add(toolBar_1, gbc_toolBar_1);
		
		JButton btnCopyTerms = new JButton("Copy Terms");
		btnCopyTerms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StringJoiner termList = new StringJoiner("\n");
				for(String term : model.getTerms()) {
					termList.add(term);
				}
				StringSelection termListString = new StringSelection(termList.toString());
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(termListString, null);
			}
		});
		btnCopyTerms.setIcon(new ImageIcon(TermCollectionTable.class.getResource("/com/nuix/termexplorer/page_white_copy.png")));
		toolBar_1.add(btnCopyTerms);
		
		JButton btnSaveTerms = new JButton("Save Terms");
		btnSaveTerms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
		});
		btnSaveTerms.setIcon(new ImageIcon(TermCollectionTable.class.getResource("/com/nuix/termexplorer/table_save.png")));
		toolBar_1.add(btnSaveTerms);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		toolBar_1.add(separator_1);
		
		JButton btnRemoveSelected = new JButton("Remove Selected");
		btnRemoveSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] selectedRows = table.getSelectedRows();
				model.removeTerms(selectedRows);
			}
		});
		toolBar_1.add(btnRemoveSelected);
		btnRemoveSelected.setIcon(new ImageIcon(TermCollectionTable.class.getResource("/com/nuix/termexplorer/table_row_delete.png")));
		
		JButton btnRemoveAll = new JButton("Remove All");
		btnRemoveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.clear();
			}
		});
		toolBar_1.add(btnRemoveAll);
		btnRemoveAll.setIcon(new ImageIcon(TermCollectionTable.class.getResource("/com/nuix/termexplorer/bin_closed.png")));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		add(scrollPane, gbc_scrollPane);
		
		table = new JTable(model);
		table.setFillsViewportHeight(true);
		scrollPane.setViewportView(table);

	}

	public void addTerm(String term) {
		model.addTerm(term);
	}

	public void addTerms(Collection<String> terms) {
		model.addTerms(terms);
	}

	public String buildQuery() {
		String query = "";
		int opIndex = termOperator.getSelectedIndex(); 
		if(opIndex == 0) {
			// ORed query
			query = QueryHelper.joinByOr(model.getTerms());
		} else if(opIndex == 1) {
			//ANDed query
			query = QueryHelper.joinByAnd(model.getTerms());
		} else {
			//NOT/OR
			query = QueryHelper.notJoinByOr(model.getTerms());
		}
		return query;
	}
}
