package antiSpamFilter;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JTextArea;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class Interface {

	private JFrame frame;
	private JTextField rulesPath;
	private JTextField hamPath;
	private JTextField spamPath;
	private JTextField mFalsePosField;
	private JTextField mFalseNegField;
	private JTextField aFalsePositiveField;
	private JTextField aFalseNegativeField;
	private JTextArea mRulesTextArea;
	private JTextArea mWeightTextArea;
	private JTextArea aRulesTextArea;
	private JTextArea aWeightTextArea;
	protected boolean spamPathOk;
	protected boolean hamPathOk;
	
	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interface window = new Interface();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Interface() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		/**
		 * Frame creation.
		 */
		frame = new JFrame();
		frame.setBounds(100, 100, 750, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(3, 0, 5, 2));
		
		/**
		 * Panel with all paths and change buttons (Rules, Ham and Spam).
		 * Each line as a Label, Field and correspondent button.
		 */	
		JPanel pathPanel = new JPanel();
		frame.getContentPane().add(pathPanel);
		GridBagLayout gbl_pathPanel = new GridBagLayout();
		gbl_pathPanel.columnWidths = new int[]{120, 528, 86, 0};
		gbl_pathPanel.rowHeights = new int[]{34, 34, 34, 0};
		gbl_pathPanel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_pathPanel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		pathPanel.setLayout(gbl_pathPanel);
		
		/**
		 * Rules label.
		 */
		JLabel rulesLabel = new JLabel("Path Rules.cf");
		rulesLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_rulesLabel = new GridBagConstraints();
		gbc_rulesLabel.fill = GridBagConstraints.BOTH;
		gbc_rulesLabel.insets = new Insets(0, 0, 5, 5);
		gbc_rulesLabel.gridx = 0;
		gbc_rulesLabel.gridy = 0;
		pathPanel.add(rulesLabel, gbc_rulesLabel);
		
		/**
		 * Rules path Field.
		 */
		rulesPath = new JTextField();
		rulesPath.setEditable(false);
		GridBagConstraints gbc_rulesPath = new GridBagConstraints();
		gbc_rulesPath.fill = GridBagConstraints.BOTH;
		gbc_rulesPath.insets = new Insets(0, 0, 5, 5);
		gbc_rulesPath.gridx = 1;
		gbc_rulesPath.gridy = 0;
		pathPanel.add(rulesPath, gbc_rulesPath);
		rulesPath.setColumns(10);
		
		/**
		 * Rules Change Button.
		 * Checks if Rules Path is valid before writing all rules and weights in textAreas, 
		 * if not returns an error message and clears the path and textAreas.
		 */
		JButton rulesButton = new JButton("Change");
		rulesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jc = new JFileChooser();
				int returnVal = jc.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					mWeightTextArea.setEditable(false);
					try {
						Scanner sc = new Scanner(new File(jc.getSelectedFile().getAbsolutePath()));
						String line;
						while (sc.hasNextLine()) {
							line = sc.nextLine();
							String [] temp = line.split(" ");
							if(!(temp.length == 2 || temp.length == 1)) {
								JOptionPane.showMessageDialog(frame,
										"The selected file doesn't have 2 columns\nKeep in mind that columns must be separated by a Space",
										"Invalid File!", JOptionPane.ERROR_MESSAGE);
								rulesPath.setText("");
								mRulesTextArea.setText("");
								mWeightTextArea.setText("");
								aRulesTextArea.setText("");
								aWeightTextArea.setText("");
								sc.close();
								return;
							}
						}
						sc.close();
						rulesPath.setText(jc.getSelectedFile().getAbsolutePath());
						ArrayList<String> rules = Functions.get_rules(jc.getSelectedFile().getAbsolutePath());
						ArrayList<String> weights = Functions.get_weights(jc.getSelectedFile().getAbsolutePath());

						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								for (String r : rules) {
									mRulesTextArea.setText(mRulesTextArea.getText() + r + "\n");
									aRulesTextArea.setText(aRulesTextArea.getText() + r + "\n");
								}
								for (String w : weights) {
									mWeightTextArea.setText(mWeightTextArea.getText() + w + "\n");
									aWeightTextArea.setText(aWeightTextArea.getText() + w + "\n");
								}
							}
						});
						mWeightTextArea.setEditable(true);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		GridBagConstraints gbc_rulesButton = new GridBagConstraints();
		gbc_rulesButton.fill = GridBagConstraints.BOTH;
		gbc_rulesButton.insets = new Insets(0, 0, 5, 0);
		gbc_rulesButton.gridx = 2;
		gbc_rulesButton.gridy = 0;
		pathPanel.add(rulesButton, gbc_rulesButton);
		
		
		/**
		 * Ham Label.
		 */
		JLabel hamLabel = new JLabel("Path Ham.log");
		hamLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_hamLabel = new GridBagConstraints();
		gbc_hamLabel.fill = GridBagConstraints.BOTH;
		gbc_hamLabel.insets = new Insets(0, 0, 5, 5);
		gbc_hamLabel.gridx = 0;
		gbc_hamLabel.gridy = 1;
		pathPanel.add(hamLabel, gbc_hamLabel);
		
		/**
		 * Ham path Field.
		 */
		hamPath = new JTextField();
		hamPath.setEditable(false);
		GridBagConstraints gbc_hamPath = new GridBagConstraints();
		gbc_hamPath.fill = GridBagConstraints.BOTH;
		gbc_hamPath.insets = new Insets(0, 0, 5, 5);
		gbc_hamPath.gridx = 1;
		gbc_hamPath.gridy = 1;
		pathPanel.add(hamPath, gbc_hamPath);
		hamPath.setColumns(10);
		
		/**
		 * Ham change path Button.
		 */
		JButton hamButton = new JButton("Change");
		hamButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jc = new JFileChooser();
				int returnVal = jc.showOpenDialog(frame);

			       if (returnVal == JFileChooser.APPROVE_OPTION) {
			    	   hamPath.setText(jc.getSelectedFile().getAbsolutePath());
			    	   hamPathOk = true;
			       }
			}
		});
		GridBagConstraints gbc_hamButton = new GridBagConstraints();
		gbc_hamButton.fill = GridBagConstraints.BOTH;
		gbc_hamButton.insets = new Insets(0, 0, 5, 0);
		gbc_hamButton.gridx = 2;
		gbc_hamButton.gridy = 1;
		pathPanel.add(hamButton, gbc_hamButton);
		
		/**
		 * Spam Label.
		 */
		JLabel spamLabel = new JLabel("Path Spam.log");
		spamLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_spamLabel = new GridBagConstraints();
		gbc_spamLabel.fill = GridBagConstraints.BOTH;
		gbc_spamLabel.insets = new Insets(0, 0, 0, 5);
		gbc_spamLabel.gridx = 0;
		gbc_spamLabel.gridy = 2;
		pathPanel.add(spamLabel, gbc_spamLabel);
		
		/**
		 * Spam path Field.
		 */
		spamPath = new JTextField();
		spamPath.setEditable(false);
		GridBagConstraints gbc_spamPath = new GridBagConstraints();
		gbc_spamPath.fill = GridBagConstraints.BOTH;
		gbc_spamPath.insets = new Insets(0, 0, 0, 5);
		gbc_spamPath.gridx = 1;
		gbc_spamPath.gridy = 2;
		pathPanel.add(spamPath, gbc_spamPath);
		spamPath.setColumns(10);
		
		/**
		 * Spam change path Button.
		 */
		JButton spamButton = new JButton("Change");
		spamButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jc = new JFileChooser();
				int returnVal = jc.showOpenDialog(frame);

			       if (returnVal == JFileChooser.APPROVE_OPTION) {
			    	   spamPath.setText(jc.getSelectedFile().getAbsolutePath());
			    	   spamPathOk = true;
			       }
			}
		});
		GridBagConstraints gbc_spamButton = new GridBagConstraints();
		gbc_spamButton.fill = GridBagConstraints.BOTH;
		gbc_spamButton.gridx = 2;
		gbc_spamButton.gridy = 2;
		pathPanel.add(spamButton, gbc_spamButton);
		
		/**
		 * Panel for manual interface.
		 * Has the result panel, 2 textAreas (for rules names & his weights), and buttons.
		 * The Weights textArea it's editable
		 */
		JPanel manualPanel = new JPanel();
		frame.getContentPane().add(manualPanel);
		GridBagLayout gbl_manualPanel = new GridBagLayout();
		gbl_manualPanel.columnWidths = new int[]{119, 527, 86, 0};
		gbl_manualPanel.rowHeights = new int[]{102, 0};
		gbl_manualPanel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_manualPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		manualPanel.setLayout(gbl_manualPanel);
		
		/**
		 * [Manual] Results Panel.
		 */
		JPanel manResultsPanel = new JPanel();
		GridBagConstraints gbc_manResultsPanel = new GridBagConstraints();
		gbc_manResultsPanel.fill = GridBagConstraints.BOTH;
		gbc_manResultsPanel.insets = new Insets(0, 0, 0, 5);
		gbc_manResultsPanel.gridx = 0;
		gbc_manResultsPanel.gridy = 0;
		manualPanel.add(manResultsPanel, gbc_manResultsPanel);
		GridBagLayout gbl_manResultsPanel = new GridBagLayout();
		gbl_manResultsPanel.columnWidths = new int[]{60, 56, 0};
		gbl_manResultsPanel.rowHeights = new int[]{51, 51, 0};
		gbl_manResultsPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_manResultsPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		manResultsPanel.setLayout(gbl_manResultsPanel);
		
		/**
		 * [Manual] False Positive Label
		 */
		JLabel mFalsePosLabel = new JLabel("FP:");
		mFalsePosLabel.setToolTipText("False Positives");
		mFalsePosLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_mFalsePosLabel = new GridBagConstraints();
		gbc_mFalsePosLabel.fill = GridBagConstraints.BOTH;
		gbc_mFalsePosLabel.insets = new Insets(0, 0, 5, 5);
		gbc_mFalsePosLabel.gridx = 0;
		gbc_mFalsePosLabel.gridy = 0;
		manResultsPanel.add(mFalsePosLabel, gbc_mFalsePosLabel);
		
		/**
		 * [Manual] False Positive values
		 */
		mFalsePosField = new JTextField();
		mFalsePosField.setEditable(false);
		mFalsePosField.setHorizontalAlignment(SwingConstants.CENTER);
		mFalsePosField.setText("0");
		GridBagConstraints gbc_mFalsePosField = new GridBagConstraints();
		gbc_mFalsePosField.fill = GridBagConstraints.BOTH;
		gbc_mFalsePosField.insets = new Insets(0, 0, 5, 0);
		gbc_mFalsePosField.gridx = 1;
		gbc_mFalsePosField.gridy = 0;
		manResultsPanel.add(mFalsePosField, gbc_mFalsePosField);
		mFalsePosField.setColumns(10);
		
		/**
		 * [Manual] False Negative Label
		 */
		JLabel mFalseNegLabel = new JLabel("FN:");
		mFalseNegLabel.setToolTipText("False Negatives");
		mFalseNegLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_mFalseNegLabel = new GridBagConstraints();
		gbc_mFalseNegLabel.fill = GridBagConstraints.BOTH;
		gbc_mFalseNegLabel.insets = new Insets(0, 0, 0, 5);
		gbc_mFalseNegLabel.gridx = 0;
		gbc_mFalseNegLabel.gridy = 1;
		manResultsPanel.add(mFalseNegLabel, gbc_mFalseNegLabel);
		
		/**
		 * [Manual] False Negative values
		 */
		mFalseNegField = new JTextField();
		mFalseNegField.setEditable(false);
		mFalseNegField.setHorizontalAlignment(SwingConstants.CENTER);
		mFalseNegField.setText("0");
		GridBagConstraints gbc_mFalseNegField = new GridBagConstraints();
		gbc_mFalseNegField.fill = GridBagConstraints.BOTH;
		gbc_mFalseNegField.gridx = 1;
		gbc_mFalseNegField.gridy = 1;
		manResultsPanel.add(mFalseNegField, gbc_mFalseNegField);
		mFalseNegField.setColumns(10);
		
		/**
		 * [Manual] Panel for the 2 text areas(Rules name & respective weights)
		 */
		JPanel manRulesPanel = new JPanel();
		GridBagConstraints gbc_manRulesPanel = new GridBagConstraints();
		gbc_manRulesPanel.fill = GridBagConstraints.BOTH;
		gbc_manRulesPanel.insets = new Insets(0, 0, 0, 5);
		gbc_manRulesPanel.gridx = 1;
		gbc_manRulesPanel.gridy = 0;
		manualPanel.add(manRulesPanel, gbc_manRulesPanel);
		manRulesPanel.setLayout(new GridLayout(0, 2, 2, 0));
		
		/**
		 * [Manual] Rules name textArea
		 */
		mRulesTextArea = new JTextArea();
		mRulesTextArea.setEditable(false);
		
		/**
		 * [Manual] Rules weights textArea (editable)
		 */
		mWeightTextArea = new JTextArea();
		mWeightTextArea.setEditable(false);
		
		/**
		 * [Manual] Scroll for BOTH manual textAreas
		 * Redirects Wheels events from Rules textArea to Weight textArea.
		 * Keep in mind that both text areas must be the same Height to scrolls to work
		 */
		JScrollPane mRuleScrollPane = new JScrollPane(mRulesTextArea, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JScrollPane mWeightScrollPane = new JScrollPane(mWeightTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mRuleScrollPane.getHorizontalScrollBar().setModel(mWeightScrollPane.getHorizontalScrollBar().getModel());
		mRuleScrollPane.getVerticalScrollBar().setModel(mWeightScrollPane.getVerticalScrollBar().getModel());
		mRuleScrollPane.setWheelScrollingEnabled(false);
		mRuleScrollPane.addMouseWheelListener(new MouseWheelListener() {
		    public void mouseWheelMoved(MouseWheelEvent e) {
		        mWeightScrollPane.dispatchEvent(e);
		    }
		});
		manRulesPanel.add(mRuleScrollPane);
		manRulesPanel.add(mWeightScrollPane);
		
		/**
		 * [Manual] Buttons Panel
		 */
		JPanel manButtonsPanel = new JPanel();
		GridBagConstraints gbc_manButtonsPanel = new GridBagConstraints();
		gbc_manButtonsPanel.fill = GridBagConstraints.BOTH;
		gbc_manButtonsPanel.gridx = 2;
		gbc_manButtonsPanel.gridy = 0;
		manualPanel.add(manButtonsPanel, gbc_manButtonsPanel);
		manButtonsPanel.setLayout(new GridLayout(2, 0, 0, 0));
		
		/**
		 * Test Button 
		 */
		JButton testButton = new JButton("Test");
		manButtonsPanel.add(testButton);
		testButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (checkPaths()) {
					try {
						String[] allRules = mRulesTextArea.getText().split("\n");
						ArrayList<String> rules = new ArrayList<String>(Arrays.asList(allRules));

						String[] allWeights = mWeightTextArea.getText().split("\n");
						ArrayList<String> weights = new ArrayList<String>(Arrays.asList(allWeights));
						ArrayList<Double> weightsD = toDoubleValidWeights(weights, rules.size());
						if (weightsD != null) {
							mFalsePosField.setText((Functions.evaluate_solution(0, rules, weightsD,
									Functions.file_to_array(hamPath.getText()))).toString());
							mFalseNegField.setText((Functions.evaluate_solution(1, rules, weightsD,
									Functions.file_to_array(spamPath.getText()))).toString());
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		/**
		 * Save Button
		 */
		JButton mSaveButton = new JButton("Save");
		manButtonsPanel.add(mSaveButton);
		
		mSaveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					String[] allWeights = mWeightTextArea.getText().split("\n");
					ArrayList<String> weights = new ArrayList<String>(Arrays.asList(allWeights));
					ArrayList<Double> weightsD = toDoubleValidWeights(weights, mRulesTextArea.getText().split("\n").length);
					if (!weightsD.equals(null))
						Functions.write_weights(rulesPath.getText(), weightsD);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		/**
		 * Panel for Auto interface.
		 * Has the result panel, 2 text areas (for rules names & his weights), and buttons.
		 */
		JPanel autoPanel = new JPanel();
		frame.getContentPane().add(autoPanel);
		GridBagLayout gbl_autoPanel = new GridBagLayout();
		gbl_autoPanel.columnWidths = new int[]{116, 527, 75, 0};
		gbl_autoPanel.rowHeights = new int[]{102, 0};
		gbl_autoPanel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_autoPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		autoPanel.setLayout(gbl_autoPanel);
		
		/**
		 * [Auto] Results panel.
		 */
		JPanel autoResultsPanel = new JPanel();
		GridBagConstraints gbc_autoResultsPanel = new GridBagConstraints();
		gbc_autoResultsPanel.fill = GridBagConstraints.BOTH;
		gbc_autoResultsPanel.insets = new Insets(0, 0, 0, 5);
		gbc_autoResultsPanel.gridx = 0;
		gbc_autoResultsPanel.gridy = 0;
		autoPanel.add(autoResultsPanel, gbc_autoResultsPanel);
		GridBagLayout gbl_autoResultsPanel = new GridBagLayout();
		gbl_autoResultsPanel.columnWidths = new int[]{60, 56, 0};
		gbl_autoResultsPanel.rowHeights = new int[]{51, 51, 0};
		gbl_autoResultsPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_autoResultsPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		autoResultsPanel.setLayout(gbl_autoResultsPanel);
		
		/**
		 * [Auto] False Positive label.
		 */
		JLabel aFalsePositiveLabel = new JLabel("FP:");
		aFalsePositiveLabel.setHorizontalAlignment(SwingConstants.CENTER);
		aFalsePositiveLabel.setToolTipText("False Positives");
		GridBagConstraints gbc_aFalsePositiveLabel = new GridBagConstraints();
		gbc_aFalsePositiveLabel.fill = GridBagConstraints.BOTH;
		gbc_aFalsePositiveLabel.insets = new Insets(0, 0, 5, 5);
		gbc_aFalsePositiveLabel.gridx = 0;
		gbc_aFalsePositiveLabel.gridy = 0;
		autoResultsPanel.add(aFalsePositiveLabel, gbc_aFalsePositiveLabel);
		
		/**
		 * [Auto] False Positive values.
		 */
		aFalsePositiveField = new JTextField();
		aFalsePositiveField.setEditable(false);
		aFalsePositiveField.setText("0");
		aFalsePositiveField.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_aFalsePositiveField = new GridBagConstraints();
		gbc_aFalsePositiveField.fill = GridBagConstraints.BOTH;
		gbc_aFalsePositiveField.insets = new Insets(0, 0, 5, 0);
		gbc_aFalsePositiveField.gridx = 1;
		gbc_aFalsePositiveField.gridy = 0;
		autoResultsPanel.add(aFalsePositiveField, gbc_aFalsePositiveField);
		aFalsePositiveField.setColumns(10);
		
		/**
		 * [Auto] False Negative Label.
		 */
		JLabel aFalseNegativeLabel = new JLabel("FN:");
		aFalseNegativeLabel.setToolTipText("False Negatives");
		aFalseNegativeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_aFalseNegativeLabel = new GridBagConstraints();
		gbc_aFalseNegativeLabel.fill = GridBagConstraints.BOTH;
		gbc_aFalseNegativeLabel.insets = new Insets(0, 0, 0, 5);
		gbc_aFalseNegativeLabel.gridx = 0;
		gbc_aFalseNegativeLabel.gridy = 1;
		autoResultsPanel.add(aFalseNegativeLabel, gbc_aFalseNegativeLabel);
		
		/**
		 * [Auto] False Negative values.
		 */
		aFalseNegativeField = new JTextField();
		aFalseNegativeField.setEditable(false);
		aFalseNegativeField.setText("0");
		aFalseNegativeField.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_aFalseNegativeField = new GridBagConstraints();
		gbc_aFalseNegativeField.fill = GridBagConstraints.BOTH;
		gbc_aFalseNegativeField.gridx = 1;
		gbc_aFalseNegativeField.gridy = 1;
		autoResultsPanel.add(aFalseNegativeField, gbc_aFalseNegativeField);
		aFalseNegativeField.setColumns(10);
		
		/**
		 * [Auto] Panel for the 2 text areas(Rules name & respective weights).
		 */
		JPanel autoRulesPanel = new JPanel();
		GridBagConstraints gbc_autoRulesPanel = new GridBagConstraints();
		gbc_autoRulesPanel.fill = GridBagConstraints.BOTH;
		gbc_autoRulesPanel.insets = new Insets(0, 0, 0, 5);
		gbc_autoRulesPanel.gridx = 1;
		gbc_autoRulesPanel.gridy = 0;
		autoPanel.add(autoRulesPanel, gbc_autoRulesPanel);
		autoRulesPanel.setLayout(new GridLayout(0, 2, 2, 0));
		
		/**
		 * [Auto] Rules name textArea.
		 */
		aRulesTextArea = new JTextArea();
		aRulesTextArea.setEditable(false);
		
		/** 
		 * [Auto] Rules weights textArea.
		 */
		aWeightTextArea = new JTextArea();
		aWeightTextArea.setEditable(false);

		/**
		 * [Auto] Scroll for BOTH auto text areas.
		 * Keep in mind that both text areas must be the same Height to scrolls to work
		 */
		JScrollPane aRuleScrollPane = new JScrollPane(aRulesTextArea, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JScrollPane aWeightScrollPane = new JScrollPane(aWeightTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		aRuleScrollPane.getHorizontalScrollBar().setModel(aWeightScrollPane.getHorizontalScrollBar().getModel());
		aRuleScrollPane.getVerticalScrollBar().setModel(aWeightScrollPane.getVerticalScrollBar().getModel());
		autoRulesPanel.add(aRuleScrollPane);
		autoRulesPanel.add(aWeightScrollPane);
		
		/**
		 * [Auto] Buttons panel.
		 */
		JPanel autoButtonsPanel = new JPanel();
		GridBagConstraints gbc_autoButtonsPanel = new GridBagConstraints();
		gbc_autoButtonsPanel.fill = GridBagConstraints.BOTH;
		gbc_autoButtonsPanel.gridx = 2;
		gbc_autoButtonsPanel.gridy = 0;
		autoPanel.add(autoButtonsPanel, gbc_autoButtonsPanel);
		autoButtonsPanel.setLayout(new GridLayout(2, 0, 0, 0));
		
		/**
		 * [Auto] Generate Button, [Not implemented]!
		 */
		JButton generateButton = new JButton("Generate");
		autoButtonsPanel.add(generateButton);
		
		/**
		 * [Auto] Save Button, [Not implemented]!
		 */
		JButton aSaveButton = new JButton("Save");
		autoButtonsPanel.add(aSaveButton);
	}
	
	protected boolean checkPaths() {
		if (!spamPathOk && !hamPathOk) {
			JOptionPane.showMessageDialog(frame, "The spam.log and ham.log path are missing", "File not found!",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (!spamPathOk) {
			JOptionPane.showMessageDialog(frame, "The spam.log path are missing", "File not found!",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (!hamPathOk) {
			JOptionPane.showMessageDialog(frame, "The ham.log path are missing", "File not found!",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * After giving an ArrayList<String> as an argument,
	 * checks if all strings are numbers between -5 and +5 and 
	 * if the number of rules equals the number of weights.
	 * if that's the case, return the same arrayList but as Double
	 * if not, return a MessageDialog with the correspondent error.
	 * 
	 * @param weights
	 * @return weightsD
	 */
	
	private ArrayList<Double> toDoubleValidWeights(ArrayList<String> weights, int numberRules) {
		if (weights.size() != numberRules) {
			JOptionPane.showMessageDialog(frame,
					"The number of rules are different from the number of Weights\n Check for blank weights in the end of the text area.",
					"Invalid weights error!", JOptionPane.ERROR_MESSAGE);
			return null;
		} else {
			ArrayList<Double> weightsD = new ArrayList<Double>();
			for (String w : weights) {
				try {
					Double d = Double.parseDouble(w);
					if (d >= -5 && d <= 5)
						weightsD.add(d);
					else {
						JOptionPane.showMessageDialog(frame,
								"One or more weight inputs is not valid\nWeights must be between -5 and +5.",
								"Invalid number error!", JOptionPane.ERROR_MESSAGE);
						return null;
					}
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(frame,
							"One or more weight inputs is not a number\nCheck for letters, symbols, blank weights and \",\".",
							"NaN error!", JOptionPane.ERROR_MESSAGE);
					return null;
				}
			}
			return weightsD;
		}
	}

}
