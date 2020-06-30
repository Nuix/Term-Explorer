package com.nuix.termexplorer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
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
import java.awt.Font;

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
	private JLabel lblStatus;
	
	public MainPanel(Window nuixWindow, Case nuixCase) {
		setBorder(new EmptyBorder(100, 100, 100, 100));
		this.nuixCase = nuixCase;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 600, 30, 600, 0, 0};
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
		gbl_expressionsPanel.columnWidths = new int[]{0, 550, 0, 0, 0, 0, 0, 0};
		gbl_expressionsPanel.rowHeights = new int[]{0, 0, 0, 30, 0};
		gbl_expressionsPanel.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_expressionsPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		expressionsPanel.setLayout(gbl_expressionsPanel);
		
		JLabel lblSingleTermExpression = new JLabel("Single Term Expression(s):");
		GridBagConstraints gbc_lblSingleTermExpression = new GridBagConstraints();
		gbc_lblSingleTermExpression.anchor = GridBagConstraints.EAST;
		gbc_lblSingleTermExpression.insets = new Insets(0, 0, 5, 5);
		gbc_lblSingleTermExpression.gridx = 0;
		gbc_lblSingleTermExpression.gridy = 0;
		expressionsPanel.add(lblSingleTermExpression, gbc_lblSingleTermExpression);
		
		txtSingletermexpression = new JTextField();
		txtSingletermexpression.setToolTipText("<html>\r\nThis only accepts a single word as input.<br/>\r\nThis allows:\r\n<ol>\r\n<li><b>*</b> - Star wildcards</li>\r\n<li><b>?</b> - Single character wildcard</li>\r\n<li><b>WORD~0.0</b> - Fuzzy similarity (accepts values between greater than or equal to 0.0 and less than 1.0)</li>\r\n</ol>\r\n<b>Note:</b> Multiple single term words may be provided if they are separated by space characters.  The results of which will be merged into a final result in the table below.\r\n</html>");
		txtSingletermexpression.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				expandTerms();
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
				expandTerms();
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
		comboFuzzyType.setModel(new DefaultComboBoxModel<String>(new String[] {"Nuix", "LuceneLevenshstein", "JaroWinkler", "NGram"}));
		GridBagConstraints gbc_comboFuzzyType = new GridBagConstraints();
		gbc_comboFuzzyType.insets = new Insets(0, 0, 5, 0);
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
				expandTerms();
			}
		});
		GridBagConstraints gbc_txtScopequery = new GridBagConstraints();
		gbc_txtScopequery.insets = new Insets(0, 0, 5, 0);
		gbc_txtScopequery.gridwidth = 6;
		gbc_txtScopequery.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtScopequery.gridx = 1;
		gbc_txtScopequery.gridy = 1;
		expressionsPanel.add(txtScopequery, gbc_txtScopequery);
		txtScopequery.setColumns(10);
		
		lblStatus = new JLabel("....");
		lblStatus.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblStatus = new GridBagConstraints();
		gbc_lblStatus.gridwidth = 7;
		gbc_lblStatus.insets = new Insets(0, 0, 5, 5);
		gbc_lblStatus.gridx = 0;
		gbc_lblStatus.gridy = 2;
		expressionsPanel.add(lblStatus, gbc_lblStatus);
		
		expansionProgressBar = new JProgressBar();
		expansionProgressBar.setStringPainted(true);
		GridBagConstraints gbc_expansionProgressBar = new GridBagConstraints();
		gbc_expansionProgressBar.fill = GridBagConstraints.BOTH;
		gbc_expansionProgressBar.gridwidth = 7;
		gbc_expansionProgressBar.gridx = 0;
		gbc_expansionProgressBar.gridy = 3;
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
			List<String> terms = matchedTerms.stream().map(mt -> mt.getMatchedTerm()).collect(Collectors.toList());
			termCollectionTable.addTerms(terms);
//			for(ExpandedTermInfo matchedTerm : matchedTerms) {
//				termCollectionTable.addTerm(matchedTerm.getMatchedTerm());
//			}
		});
	}
	
	private void setStatusMessage(String message) {
		SwingUtilities.invokeLater(()->{
			lblStatus.setText(message);
		});
	}
	
	private void lockUI(boolean locked) {
		SwingUtilities.invokeLater(()->{
			btnExpandterm.setEnabled(!locked);
			txtSingletermexpression.setEnabled(!locked);
			txtScopequery.setEnabled(!locked);
			termFields.setEnabled(!locked);
			comboFuzzyType.setEnabled(!locked);
		});
	}
	
	private void expandTerms() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					lockUI(true);
					setStatusMessage("Please Wait, Preparing to Expand Terms...");
					
					TermExpander te = new TermExpander();
					switch(comboFuzzyType.getSelectedIndex()) {
					case 0:
						te.setFuzzyResolutionAlgorithm(SimilarityCalculation.Nuix);
						break;
					case 1:
						te.setFuzzyResolutionAlgorithm(SimilarityCalculation.LuceneLevenshstein);
						break;
					case 2:
						te.setFuzzyResolutionAlgorithm(SimilarityCalculation.JaroWinkler);
					case 3:
						te.setFuzzyResolutionAlgorithm(SimilarityCalculation.NGram);
						break;
					}
					
					te.whenProgressUpdated((current,total)->{
						expansionProgressBar.setMaximum(total);
						expansionProgressBar.setValue(current);
					});
					
					String termInput = txtSingletermexpression.getText();
					String[] terms = termInput.split("\\s+");
					String scopeQuery = txtScopequery.getText();
					
					int termFieldsIndex = termFields.getSelectedIndex();
					boolean content = termFieldsIndex == 0 || termFieldsIndex == 1;
					boolean properties = termFieldsIndex == 0 || termFieldsIndex == 2;
					
					// Accepting multiple input terms means we need to combine results when a given matched terms
					// comes up multiple times.  This is resolved here by keeping the occurrence count the first time
					// we encounter a given matched term (since it should be the same each time) and then regarding
					// similarity scores, we will report the highest we find
					Map<String,ExpandedTermInfo> termInfoByMatchedTerm = new HashMap<String,ExpandedTermInfo>();
					for(String term : terms) {
						setStatusMessage("Please Wait, Expanding Term: "+term);
						List<ExpandedTermInfo> termInfos = te.expandTerm(nuixCase, content, properties, term, scopeQuery);
						// Iterate each matching term info
						for(ExpandedTermInfo eti : termInfos) {
							// Are we already tracking this resulting term?
							if(termInfoByMatchedTerm.containsKey(eti.getMatchedTerm())) {
								// We are already tracking this term, so merge in results
								ExpandedTermInfo existingInfo = termInfoByMatchedTerm.get(eti.getMatchedTerm()); 
								if(eti.getSimilarity() > existingInfo.getSimilarity()) {
									existingInfo.setSimilarity(eti.getSimilarity());	
								}
							} else {
								// This is the first time weve seen this matched term, so we just stick
								// it into the listing
								termInfoByMatchedTerm.put(eti.getMatchedTerm(),eti);
							}
						}
					}
					
					// We just need to flatten out our results now
					List<ExpandedTermInfo> finalMatchedTerms = new ArrayList<ExpandedTermInfo>();
					finalMatchedTerms.addAll(termInfoByMatchedTerm.values());
					// Need to resort them descending
					Collections.sort(finalMatchedTerms, (v1,v2)->{ return Long.compare(-v1.getOcurrences(), -v2.getOcurrences()); });
					
					termExpansionMatchesTable.setMatchedTerms(finalMatchedTerms);
				} catch (Exception e) {
					CommonDialogs.showError("Error expanding term: "+e.getMessage());
					logger.error("Error expanding term",e);
				} finally {
					setStatusMessage("...");
					lockUI(false);
				}
			}
		});
		t.start();
	}
}
