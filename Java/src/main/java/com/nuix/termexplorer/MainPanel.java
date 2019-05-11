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
import com.nuix.superutilities.misc.SimilarityCalculation;
import com.nuix.superutilities.misc.TermExpander;

import nuix.Case;
import nuix.Window;
import javax.swing.JProgressBar;
import javax.swing.JButton;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {

	private static Logger logger = Logger.getLogger("Term Explorer");
	
	private Case nuixCase;
	private JTextField txtSingletermexpression;
	private TermCollectionTable termCollectionTable;
	private TermExpansionMatchesTable termExpansionMatchesTable;
	private JPanel expressionsPanel;
	private JComboBox<String> termFields;
	private JLabel lblScopeQuery;
	private JTextField txtScopequery;
	private JLabel lblFuzzyType;
	private JComboBox<String> comboFuzzyType;
	private JProgressBar expansionProgressBar;
	private JButton btnExpandterm;
	
	public MainPanel(Window nuixWindow, Case nuixCase) {
		setBorder(new EmptyBorder(100, 100, 100, 100));
		this.nuixCase = nuixCase;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 500, 30, 500, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		expressionsPanel = new JPanel();
		expressionsPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
		GridBagConstraints gbc_expressionsPanel = new GridBagConstraints();
		gbc_expressionsPanel.gridwidth = 3;
		gbc_expressionsPanel.insets = new Insets(0, 0, 5, 5);
		gbc_expressionsPanel.fill = GridBagConstraints.BOTH;
		gbc_expressionsPanel.gridx = 1;
		gbc_expressionsPanel.gridy = 0;
		add(expressionsPanel, gbc_expressionsPanel);
		GridBagLayout gbl_expressionsPanel = new GridBagLayout();
		gbl_expressionsPanel.columnWidths = new int[]{0, 250, 0, 0, 0, 0, 0, 0, 0};
		gbl_expressionsPanel.rowHeights = new int[]{0, 0, 30, 0};
		gbl_expressionsPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_expressionsPanel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		expressionsPanel.setLayout(gbl_expressionsPanel);
		
		JLabel lblSingleTermExpression = new JLabel("Single Term Expression:");
		GridBagConstraints gbc_lblSingleTermExpression = new GridBagConstraints();
		gbc_lblSingleTermExpression.anchor = GridBagConstraints.EAST;
		gbc_lblSingleTermExpression.insets = new Insets(0, 0, 5, 5);
		gbc_lblSingleTermExpression.gridx = 0;
		gbc_lblSingleTermExpression.gridy = 0;
		expressionsPanel.add(lblSingleTermExpression, gbc_lblSingleTermExpression);
		
		txtSingletermexpression = new JTextField();
		txtSingletermexpression.setToolTipText("<html>\r\nThis only accepts a single word as input.<br/>\r\nThis allows:\r\n<ol>\r\n<li><b>*</b> - Star wildcards</li>\r\n<li><b>?</b> - Single character wildcard</li>\r\n<li><b>WORD~0.0</b> - Fuzzy similarity (accepts values between greater than or equal to 0.0 and less than 1.0)</li>\r\n</ol></html>");
		txtSingletermexpression.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				expandTerm();
			}
		});
		GridBagConstraints gbc_txtSingletermexpression = new GridBagConstraints();
		gbc_txtSingletermexpression.insets = new Insets(0, 0, 5, 5);
		gbc_txtSingletermexpression.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSingletermexpression.gridx = 1;
		gbc_txtSingletermexpression.gridy = 0;
		expressionsPanel.add(txtSingletermexpression, gbc_txtSingletermexpression);
		txtSingletermexpression.setColumns(10);
		
		btnExpandterm = new JButton("");
		btnExpandterm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				expandTerm();
			}
		});
		btnExpandterm.setIcon(new ImageIcon(MainPanel.class.getResource("/com/nuix/termexplorer/control_play.png")));
		GridBagConstraints gbc_btnExpandterm = new GridBagConstraints();
		gbc_btnExpandterm.insets = new Insets(0, 0, 5, 5);
		gbc_btnExpandterm.gridx = 2;
		gbc_btnExpandterm.gridy = 0;
		expressionsPanel.add(btnExpandterm, gbc_btnExpandterm);
		
		termFields = new JComboBox<String>();
		termFields.setModel(new DefaultComboBoxModel<String>(new String[] {"Content and Properties", "Content Only", "Properties Only"}));
		GridBagConstraints gbc_termFields = new GridBagConstraints();
		gbc_termFields.insets = new Insets(0, 0, 5, 5);
		gbc_termFields.fill = GridBagConstraints.HORIZONTAL;
		gbc_termFields.gridx = 3;
		gbc_termFields.gridy = 0;
		expressionsPanel.add(termFields, gbc_termFields);
		
		lblFuzzyType = new JLabel("Fuzzy Type:");
		GridBagConstraints gbc_lblFuzzyType = new GridBagConstraints();
		gbc_lblFuzzyType.anchor = GridBagConstraints.EAST;
		gbc_lblFuzzyType.insets = new Insets(0, 0, 5, 5);
		gbc_lblFuzzyType.gridx = 5;
		gbc_lblFuzzyType.gridy = 0;
		expressionsPanel.add(lblFuzzyType, gbc_lblFuzzyType);
		
		comboFuzzyType = new JComboBox<String>();
		comboFuzzyType.setModel(new DefaultComboBoxModel<String>(new String[] {"Nuix", "Levenstein", "Lucene Levenshstein", "NGram"}));
		GridBagConstraints gbc_comboFuzzyType = new GridBagConstraints();
		gbc_comboFuzzyType.insets = new Insets(0, 0, 5, 5);
		gbc_comboFuzzyType.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboFuzzyType.gridx = 6;
		gbc_comboFuzzyType.gridy = 0;
		expressionsPanel.add(comboFuzzyType, gbc_comboFuzzyType);
		
		lblScopeQuery = new JLabel("Scope Query (blank is entire case):");
		GridBagConstraints gbc_lblScopeQuery = new GridBagConstraints();
		gbc_lblScopeQuery.anchor = GridBagConstraints.EAST;
		gbc_lblScopeQuery.insets = new Insets(0, 0, 5, 5);
		gbc_lblScopeQuery.gridx = 0;
		gbc_lblScopeQuery.gridy = 1;
		expressionsPanel.add(lblScopeQuery, gbc_lblScopeQuery);
		
		txtScopequery = new JTextField();
		txtScopequery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				expandTerm();
			}
		});
		GridBagConstraints gbc_txtScopequery = new GridBagConstraints();
		gbc_txtScopequery.insets = new Insets(0, 0, 5, 0);
		gbc_txtScopequery.gridwidth = 7;
		gbc_txtScopequery.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtScopequery.gridx = 1;
		gbc_txtScopequery.gridy = 1;
		expressionsPanel.add(txtScopequery, gbc_txtScopequery);
		txtScopequery.setColumns(10);
		
		expansionProgressBar = new JProgressBar();
		GridBagConstraints gbc_expansionProgressBar = new GridBagConstraints();
		gbc_expansionProgressBar.fill = GridBagConstraints.BOTH;
		gbc_expansionProgressBar.gridwidth = 8;
		gbc_expansionProgressBar.gridx = 0;
		gbc_expansionProgressBar.gridy = 2;
		expressionsPanel.add(expansionProgressBar, gbc_expansionProgressBar);
		
		termExpansionMatchesTable = new TermExpansionMatchesTable();
		GridBagConstraints gbc_termExpansionMatchesTable = new GridBagConstraints();
		gbc_termExpansionMatchesTable.insets = new Insets(0, 0, 0, 5);
		gbc_termExpansionMatchesTable.fill = GridBagConstraints.BOTH;
		gbc_termExpansionMatchesTable.gridx = 1;
		gbc_termExpansionMatchesTable.gridy = 1;
		add(termExpansionMatchesTable, gbc_termExpansionMatchesTable);
		
		termCollectionTable = new TermCollectionTable(nuixWindow);
		GridBagConstraints gbc_termCollectionTable = new GridBagConstraints();
		gbc_termCollectionTable.insets = new Insets(0, 0, 0, 5);
		gbc_termCollectionTable.fill = GridBagConstraints.BOTH;
		gbc_termCollectionTable.gridx = 3;
		gbc_termCollectionTable.gridy = 1;
		add(termCollectionTable, gbc_termCollectionTable);
		
		termExpansionMatchesTable.onSendToCollection((matchedTerms) -> {
			for(ExpandedTermInfo matchedTerm : matchedTerms) {
				termCollectionTable.addTerm(matchedTerm.getMatchedTerm());
			}
		});
	}
	
	private void expandTerm() {
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					TermExpander te = new TermExpander();
					switch(comboFuzzyType.getSelectedIndex()) {
					case 0:
						te.setFuzzyResolutionAlgorithm(SimilarityCalculation.Nuix);
						break;
					case 1:
						te.setFuzzyResolutionAlgorithm(SimilarityCalculation.Levenstein);
						break;
					case 2:
						te.setFuzzyResolutionAlgorithm(SimilarityCalculation.LuceneLevenshstein);
						break;
					case 3:
						te.setFuzzyResolutionAlgorithm(SimilarityCalculation.NGram);
						break;
					}
					
					te.whenProgressUpdated((current,total)->{
						expansionProgressBar.setMaximum(total);
						expansionProgressBar.setValue(current);
					});
					
					String scopeQuery = txtScopequery.getText();
					int termFieldsIndex = termFields.getSelectedIndex();
					boolean content = termFieldsIndex == 0 || termFieldsIndex == 1;
					boolean properties = termFieldsIndex == 0 || termFieldsIndex == 2;
					List<ExpandedTermInfo> matchedTerms = te.expandTerm(nuixCase, content, properties, txtSingletermexpression.getText(), scopeQuery);
					termExpansionMatchesTable.setMatchedTerms(matchedTerms);
				} catch (Exception e) {
					CommonDialogs.showError("Error expanding term: "+e.getMessage());
					logger.error("Error expanding term",e);
				}
			}
		});
		t.start();
	}
}
