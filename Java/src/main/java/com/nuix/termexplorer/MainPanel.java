package com.nuix.termexplorer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import com.nuix.superutilities.misc.ExpandedTermInfo;
import com.nuix.superutilities.misc.TermExpander;

import nuix.Case;
import nuix.Window;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {

	private static Logger logger = Logger.getLogger("Term Explorer");
	
	private Case nuixCase;
	private JTextField txtSingletermexpression;
	private TermCollectionTable termCollectionTable;
	private TermExpansionMatchesTable termExpansionMatchesTable;
	private JPanel panel;
	private JComboBox<String> termFields;
	private JLabel lblScopeQuery;
	private JTextField txtScopequery;
	
	public MainPanel(Window nuixWindow, Case nuixCase) {
		setBorder(new EmptyBorder(100, 100, 100, 100));
		this.nuixCase = nuixCase;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{500, 30, 500, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 3;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 250, 0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblSingleTermExpression = new JLabel("Single Term Expression:");
		GridBagConstraints gbc_lblSingleTermExpression = new GridBagConstraints();
		gbc_lblSingleTermExpression.insets = new Insets(0, 0, 0, 5);
		gbc_lblSingleTermExpression.gridx = 0;
		gbc_lblSingleTermExpression.gridy = 0;
		panel.add(lblSingleTermExpression, gbc_lblSingleTermExpression);
		
		txtSingletermexpression = new JTextField();
		txtSingletermexpression.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				expandTerm(txtSingletermexpression.getText());
			}
		});
		GridBagConstraints gbc_txtSingletermexpression = new GridBagConstraints();
		gbc_txtSingletermexpression.insets = new Insets(0, 0, 0, 5);
		gbc_txtSingletermexpression.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSingletermexpression.gridx = 1;
		gbc_txtSingletermexpression.gridy = 0;
		panel.add(txtSingletermexpression, gbc_txtSingletermexpression);
		txtSingletermexpression.setColumns(10);
		
		termFields = new JComboBox<String>();
		termFields.setModel(new DefaultComboBoxModel<String>(new String[] {"Content and Properties", "Content Only", "Properties Only"}));
		GridBagConstraints gbc_termFields = new GridBagConstraints();
		gbc_termFields.insets = new Insets(0, 0, 0, 5);
		gbc_termFields.fill = GridBagConstraints.HORIZONTAL;
		gbc_termFields.gridx = 2;
		gbc_termFields.gridy = 0;
		panel.add(termFields, gbc_termFields);
		
		lblScopeQuery = new JLabel("Scope Query (blank is entire case):");
		GridBagConstraints gbc_lblScopeQuery = new GridBagConstraints();
		gbc_lblScopeQuery.anchor = GridBagConstraints.EAST;
		gbc_lblScopeQuery.insets = new Insets(0, 0, 0, 5);
		gbc_lblScopeQuery.gridx = 4;
		gbc_lblScopeQuery.gridy = 0;
		panel.add(lblScopeQuery, gbc_lblScopeQuery);
		
		txtScopequery = new JTextField();
		GridBagConstraints gbc_txtScopequery = new GridBagConstraints();
		gbc_txtScopequery.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtScopequery.gridx = 5;
		gbc_txtScopequery.gridy = 0;
		panel.add(txtScopequery, gbc_txtScopequery);
		txtScopequery.setColumns(10);
		
		termExpansionMatchesTable = new TermExpansionMatchesTable();
		GridBagConstraints gbc_termExpansionMatchesTable = new GridBagConstraints();
		gbc_termExpansionMatchesTable.insets = new Insets(0, 0, 0, 5);
		gbc_termExpansionMatchesTable.fill = GridBagConstraints.BOTH;
		gbc_termExpansionMatchesTable.gridx = 0;
		gbc_termExpansionMatchesTable.gridy = 1;
		add(termExpansionMatchesTable, gbc_termExpansionMatchesTable);
		
		termCollectionTable = new TermCollectionTable(nuixWindow);
		GridBagConstraints gbc_termCollectionTable = new GridBagConstraints();
		gbc_termCollectionTable.insets = new Insets(0, 0, 0, 5);
		gbc_termCollectionTable.fill = GridBagConstraints.BOTH;
		gbc_termCollectionTable.gridx = 2;
		gbc_termCollectionTable.gridy = 1;
		add(termCollectionTable, gbc_termCollectionTable);
		
		termExpansionMatchesTable.onSendToCollection((matchedTerms) -> {
			for(ExpandedTermInfo matchedTerm : matchedTerms) {
				termCollectionTable.addTerm(matchedTerm.getMatchedTerm());
			}
		});
	}
	
	private void expandTerm(String term) {
		try {
			String scopeQuery = txtScopequery.getText();
			int termFieldsIndex = termFields.getSelectedIndex();
			boolean content = termFieldsIndex == 0 || termFieldsIndex == 1;
			boolean properties = termFieldsIndex == 0 || termFieldsIndex == 2;
			List<ExpandedTermInfo> matchedTerms = TermExpander.expandTerm(nuixCase, content, properties, txtSingletermexpression.getText(), scopeQuery);
			termExpansionMatchesTable.setMatchedTerms(matchedTerms);
		} catch (Exception e) {
			CommonDialogs.showError("Error expanding term: "+e.getMessage());
			logger.error("Error expanding term",e);
		}
	}
}
