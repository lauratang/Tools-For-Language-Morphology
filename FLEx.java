import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class FLEx extends JFrame {

	private static final String LEXICON_ENTRIES_FILE = "lexicon.txt";
	private MyTableModel model;
	private static int rowHeight;

	public FLEx() {

		// initialize all java swing components
		initComponents();
		
		// set table model and sorting
		model = new MyTableModel();
		table.setModel(model);
		table.setRowSorter(new TableRowSorter<MyTableModel>(model));
		table.getColumnModel().getColumn(0).setCellRenderer(new MultiLineCellRenderer());
		table.getColumnModel().getColumn(2).setCellRenderer(new MultiLineCellRenderer());
		table.getColumnModel().getColumn(3).setCellRenderer(new MultiLineCellRenderer());
		table.getColumnModel().getColumn(1).setCellRenderer(new MultiLineCellRenderer());
		rowHeight = table.getRowHeight();

		// read in file
		try (BufferedReader reader = Files.newBufferedReader(
				Paths.get(LEXICON_ENTRIES_FILE), Charset.forName("UTF-8"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				
				// create Lexicon entry
				String[] split = line.split("\t");
				String arr = split[0];
				String arripa = split[1];
				String morph = split[2];
				String complex = split[3];
				String gloss = split[4];
				String category = split[5];
				LexiconObject entry = new LexiconObject(arr, arripa, morph, complex, gloss, category);
				
				// add multiple senses
				int start = 6;
				int stop = split.length;
				while (start < stop) {
					entry.addSense(split[start], split[start + 1]);
					start = (start + 2);
				}
				
				// insert into table
				int row = table.getRowCount();
				((MyTableModel) table.getModel()).insertRow(row, 
						new Object[] { entry, entry.getLexemeFormIPA(),
								entry.getGloss(), entry.getCategory() });
				table.setRowHeight(row, (rowHeight * entry.getGlosses().size()) + 2);
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void initComponents() {
		
		/** ================================================================================
		 * 								Variable Initialization
		 *  ================================================================================
		 */ 
		panel1 = new JPanel();
		panel1d = new JPanel();
		panel1dsenses = new JPanel();
		table = new JTable();
		tableScrollPane = new JScrollPane();
		entryDisplay = new JTextField();
		saveFile = new JButton();
		retrieveFile = new JButton();
		addEntry = new JButton();
		newSense = new JButton();
		entriesLabel = new JLabel();
		entryLabel = new JLabel();
		entryLexemeForm = new JLabel();
		dialogNewEntry = new JDialog();
		senseNewEntry = new JDialog();
		newEntryPanel = new JPanel();
		entryProperties = new JLabel();
		lexemeForm = new JLabel();
		lexemeIPALabel = new JLabel();
		lexemeFormText = new JTextField();
		lexemeIPA = new JTextField();
		morphemeType = new JLabel();
		String[] morphemeItems = { "*bound root", "*bound stem", "circumfix",
				"clitic", "discontinugous phrase", "=enclitic", "-infix-",
				"-infixing interfix-", "particle", "phrase", "prefix-",
				"prefixing interfix-", "proclitic=", "root", "=simulfix=",
				"stem", "-suffix", "-suffixing interfix", "~suprafix~" };
		morphemeComboBox = new JComboBox(morphemeItems);
		morphemeComboBox.setSelectedIndex(15);
		morphemeComboDisplay = new JComboBox(morphemeItems);
		morphemeComboDisplay.setSelectedIndex(-1);
		complexFormLabel = new JLabel();
		String[] complexFormItems = { "<Not Applicable>",
				"<Unknown Complex Form>", "Compound", "Contraction",
				"Derivative", "Idiom", "Phrasal Verb", "Saying" };
		complexFormComboBox = new JComboBox(complexFormItems);
		separator = new JSeparator();
		glossLabel = new JLabel();
		Gloss = new JTextField();
		inflectionalLabel = new JLabel();
		gramInfoLabel = new JLabel();
		categoryLabel = new JLabel();
		String[] categoryItems = { "Adjective", "Adposition", "Adverb",
				"Classifier", "Connective", "Determiner", "Enclitic",
				"English numbers", "Interjection", "Noun", "Numeral",
				"Particle", "Interrogative pro-form", "Pro-adverb", "Pronoun",
				"Intransitive verb", "Transitive verb", "<Not Sure>"};
		categoryComboBox = new JComboBox(categoryItems);
		categoryComboBox.setSelectedIndex(17);
		similarEntriesLabel = new JLabel();
		createButton = new JButton();
		closeButton = new JButton();
		currentMorph = new JLabel();
		currentMorph.setText("Morph Type: ");
		newSensePanel = new JPanel();
		Glossb = new JTextField();
		glossLabel2 = new JLabel();
		categoryLabel2 = new JLabel();
		categoryComboBox2 = new JComboBox(categoryItems);
		categoryComboBox2.setSelectedIndex(17);
		createButtonb = new JButton();
		
		/** ================================================================================
		 * 						Setting Up Functionality and Formatting
		 *  ================================================================================
		 */ 
		Container contentPane = getContentPane();
		panel1.setBorder(new javax.swing.border.CompoundBorder(
				new javax.swing.border.TitledBorder(
						new javax.swing.border.EmptyBorder(0, 0, 0, 0),
						"",
						javax.swing.border.TitledBorder.CENTER,
						javax.swing.border.TitledBorder.BOTTOM,
						new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
						java.awt.Color.red), panel1.getBorder()));
		panel1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
			public void propertyChange(java.beans.PropertyChangeEvent e) {
				if ("border".equals(e.getPropertyName()))
					throw new RuntimeException();
			}
		});
		
		/** ================================================================================
		 * 								  ToolBar Buttons
		 *  ================================================================================
		 */ 
		{	// ---- entriesLabel ----
			entriesLabel.setText("Entries");
			entriesLabel.setFont(entriesLabel.getFont().deriveFont(
					entriesLabel.getFont().getSize() - 2f));

			// ---- Entry ----
			entryLabel.setText("Entry");
			entryLabel.setFont(entryLabel.getFont().deriveFont(
					entryLabel.getFont().getSize() - 2f));
			
			// ---- saveFile ----
			saveFile.setText("Save File");
			saveFile.setActionCommand("Save File");
			saveFile.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					saveFileActionPerformed(e);
				}
			});

			// ---- retrieveFile ----
			retrieveFile.setText("Open File");
			retrieveFile.setActionCommand("Open File");
			retrieveFile.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					retrieveFileActionPerformed(e);
				}
			});

			// ---- addEntry ----
			addEntry.setText("New Entry");
			addEntry.setActionCommand("New Entry");
			addEntry.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					addEntryActionPerformed(e);
				}
			});
			
			// ---- newSense ----
			newSense.setText("Add Sense to Current Word");
			newSense.setActionCommand("Add Sense");
			newSense.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					newSenseActionPerformed(e);
				}
			});
		}
		
		/** ================================================================================
		 * 									Table Listeners
		 *  ================================================================================
		 */
		{	
			table.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					if (SwingUtilities.isLeftMouseButton(e)) {
						
						JTable target = (JTable) e.getSource();
						int row = target.convertRowIndexToModel(target.getSelectedRow());
						LexiconObject selected = (LexiconObject) table.getModel().getValueAt(row, 0);
						
						entryDisplay.setText(selected.getLexemeForm());
						morphemeComboDisplay.setSelectedItem(selected
								.getMorphemeType());
						
						panel1d.remove(panel1dsenses);
						panel1dsenses.removeAll();
						
						int start = 1;
						int stop = selected.getSenseCount();
						while (start <= stop) {
							
							// set up
							JLabel sense = new JLabel();
							JButton delete = new JButton();
							JLabel gloss = new JLabel();
							JLabel gram = new JLabel();
							JTextField glossText = new JTextField();
							glossText.setMaximumSize(new Dimension(1000,100));
							String[] categoryItems = { "Adjective", "Adposition", "Adverb",
									"Classifier", "Connective", "Determiner", "Enclitic",
									"English numbers", "Interjection", "Noun", "Numeral",
									"Particle", "Interrogative pro-form", "Pro-adverb", "Pronoun",
									"Intransitive verb", "Transitive verb", "<Not Sure>"};
							JComboBox gramComboBox = new JComboBox(categoryItems);
							
							JPanel sensePanel = new JPanel();
							sensePanel.setLayout(new BoxLayout(sensePanel, BoxLayout.X_AXIS));
							JPanel glossPanel = new JPanel();
							glossPanel.setLayout(new BoxLayout(glossPanel, BoxLayout.X_AXIS));
							JPanel gramPanel = new JPanel();
							gramPanel.setLayout(new BoxLayout(gramPanel, BoxLayout.X_AXIS));
							
							// fill
							sense.setText("Sense " + String.valueOf(start));
							delete.setText("Delete");
							gloss.setText("Gloss: ");
							gram.setText("Grammatical Info.: ");
							glossText.setText(selected.getGloss(start));
							int index = -1;
							for (int i = 0; i<categoryItems.length; i++) {
								if (categoryItems[i].equals(selected.getCategory(start))) {
									index = i;
								}
							}
							gramComboBox.setSelectedIndex(index);
							
							// add Delete, Gloss and ComboBox Listeners
							final int s = start;
							
							delete.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									deleteSenseActionListener(e, s);
								}
							});
							
							final JTextField g = glossText;
							glossText.addMouseListener(new MouseAdapter() {
								public void mousePressed(MouseEvent e) {
									g.getDocument().addDocumentListener(new DocumentListener() {

										@Override
										public void insertUpdate(DocumentEvent e) {
											Document doc = (Document) e.getDocument();
											String text;
											try {
												text = doc.getText(0, doc.getLength());
												int row = table.convertRowIndexToModel(table.getSelectedRow());
												LexiconObject selected = (LexiconObject) table.getModel().getValueAt(row, 0);
												selected.setGloss(s, text);
												((MyTableModel) table.getModel()).fireTableRowsUpdated(row,row);
											} catch (BadLocationException e1) {e1.printStackTrace();}
										}

										@Override
										public void removeUpdate(DocumentEvent e) {
											Document doc = (Document) e.getDocument();
											String text;
											try {
												text = doc.getText(0, doc.getLength());
												int row = table.convertRowIndexToModel(table.getSelectedRow());
												LexiconObject selected = (LexiconObject) table.getModel().getValueAt(row, 0);
												selected.setGloss(s, text);
												((MyTableModel) table.getModel()).fireTableRowsUpdated(row,row);
											} catch (BadLocationException e1) {e1.printStackTrace();}
										}

										@Override
										public void changedUpdate(DocumentEvent e) {
											Document doc = (Document) e.getDocument();
											String text;
											try {
												text = doc.getText(0, doc.getLength());
												int row = table.convertRowIndexToModel(table.getSelectedRow());
												LexiconObject selected = (LexiconObject) table.getModel().getValueAt(row, 0);
												selected.setGloss(s, text);
												((MyTableModel) table.getModel()).fireTableRowsUpdated(row,row);
											} catch (BadLocationException e1) {e1.printStackTrace();}
										}

									});
								}
							});
							
							final JComboBox b = gramComboBox;
							gramComboBox.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									gramChangeActionPerformed(e, s, b);
								}
							});
			
							// format
							sensePanel.add(sense);
							if (start != 1) {
								sensePanel.add(delete);
							}
							glossPanel.add(gloss);
							glossPanel.add(glossText);
							gramPanel.add(gram);
							gramPanel.add(gramComboBox);
							
							panel1dsenses.add(sensePanel);
							panel1dsenses.add(glossPanel);
							panel1dsenses.add(gramPanel);
							panel1dsenses.add(new JSeparator(SwingConstants.HORIZONTAL));
							
							start++;
						}

						panel1d.add(panel1dsenses);
						panel1d.updateUI();
						
					}

					if (SwingUtilities.isRightMouseButton(e)) {
						JTable target = (JTable) e.getSource();
						int row = target.convertRowIndexToModel(target
								.getSelectedRow());
						((MyTableModel) table.getModel()).removeRow(row);
					}

				}
			});
		}
		
		/** ================================================================================
		 * 									Entry Display Listeners
		 *  ================================================================================
		 */ 
		{
			entryLexemeForm.setText("Lexeme Form:");
			panel1dsenses.setLayout(new BoxLayout(panel1dsenses, BoxLayout.Y_AXIS));
			morphemeComboDisplay.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					morphChangeActionPerformed(e);
				}
			});
			
			entryDisplay.setMaximumSize(new Dimension(1000,100));
			entryDisplay.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					entryDisplay.getDocument().addDocumentListener(new DocumentListener() {

						@Override
						public void insertUpdate(DocumentEvent e) {
							Document doc = (Document) e.getDocument();
							String text;
							try {
								text = doc.getText(0, doc.getLength());
								int row = table.convertRowIndexToModel(table.getSelectedRow());
								LexiconObject selected = (LexiconObject) table.getModel().getValueAt(row, 0);
								selected.setLexemeForm(text);
								((MyTableModel) table.getModel()).fireTableRowsUpdated(row,row);
							} catch (BadLocationException e1) {e1.printStackTrace();}
						}

						@Override
						public void removeUpdate(DocumentEvent e) {
							Document doc = (Document) e.getDocument();
							String text;
							try {
								text = doc.getText(0, doc.getLength());
								int row = table.convertRowIndexToModel(table.getSelectedRow());
								LexiconObject selected = (LexiconObject) table.getModel().getValueAt(row, 0);
								selected.setLexemeForm(text);
								((MyTableModel) table.getModel()).fireTableRowsUpdated(row,row);
							} catch (BadLocationException e1) {e1.printStackTrace();}
						}

						@Override
						public void changedUpdate(DocumentEvent e) {
							Document doc = (Document) e.getDocument();
							String text;
							try {
								text = doc.getText(0, doc.getLength());
								int row = table.convertRowIndexToModel(table.getSelectedRow());
								LexiconObject selected = (LexiconObject) table.getModel().getValueAt(row, 0);
								selected.setLexemeForm(text);
								((MyTableModel) table.getModel()).fireTableRowsUpdated(row,row);
							} catch (BadLocationException e1) {e1.printStackTrace();}
						}

					});
				}
			});
		
		}
		
		/** ================================================================================
		 * 									Format Main Window
		 *  ================================================================================
		 */ 
		
		BoxLayout panel1layout = new BoxLayout(panel1, BoxLayout.Y_AXIS);
		panel1.setLayout(panel1layout);
		
		JPanel toolbarPanel = new JPanel();
		toolbarPanel.setLayout(new BoxLayout(toolbarPanel, BoxLayout.X_AXIS));
		toolbarPanel.add(saveFile);
		toolbarPanel.add(retrieveFile);
		toolbarPanel.add(addEntry);
		toolbarPanel.add(newSense);
		
		JPanel panel1b = new JPanel();
		panel1b.setLayout(new BoxLayout(panel1b, BoxLayout.X_AXIS));
		
		JPanel panel1c = new JPanel();
		panel1c.setLayout(new BoxLayout(panel1c, BoxLayout.Y_AXIS));
		panel1c.add(entriesLabel);
		tableScrollPane.setViewportView(table);
		panel1c.add(tableScrollPane);
		
		panel1d.setLayout(new BoxLayout(panel1d, BoxLayout.Y_AXIS));
		panel1d.add(entryLabel);
		
		JPanel panel1dlex = new JPanel();
		panel1dlex.setLayout(new BoxLayout(panel1dlex, BoxLayout.X_AXIS));
		panel1dlex.add(entryLexemeForm);
		panel1dlex.add(entryDisplay);
		
		JPanel panel1dmorph = new JPanel();
		panel1dmorph.setLayout(new BoxLayout(panel1dmorph, BoxLayout.X_AXIS));
		panel1dmorph.add(currentMorph);
		panel1dmorph.add(morphemeComboDisplay);
		
		panel1d.add(panel1dlex);
		panel1d.add(panel1dmorph);
		panel1d.add(new JSeparator(SwingConstants.HORIZONTAL));
		panel1d.add(panel1dsenses);
		
		panel1b.add(panel1c);
		panel1b.add(panel1d);
		panel1.add(toolbarPanel);
		panel1.add(panel1b);
		
		contentPane.add(panel1);
			
		/** ================================================================================
		 * 							Format New Entry/New Sense Windows
		 *  ================================================================================
		 */ 
		{
			Container dialogNewEntryContentPane = dialogNewEntry
					.getContentPane();

			Container senseNewEntryContentPane = senseNewEntry.getContentPane();

			// ======== newEntryPanel / newSensePanel ========
			{
				newEntryPanel.setBorder(new javax.swing.border.CompoundBorder(
						new javax.swing.border.TitledBorder(
								new javax.swing.border.EmptyBorder(0, 0, 0, 0),
								"", javax.swing.border.TitledBorder.CENTER,
								javax.swing.border.TitledBorder.BOTTOM,
								new java.awt.Font("Dialog", java.awt.Font.BOLD,
										12), java.awt.Color.red), newEntryPanel
								.getBorder()));
				newEntryPanel.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
					public void propertyChange(java.beans.PropertyChangeEvent e) {
						if ("border".equals(e.getPropertyName()))
							throw new RuntimeException();
					}
				});

				newSensePanel.setBorder(new javax.swing.border.CompoundBorder(
						new javax.swing.border.TitledBorder(
								new javax.swing.border.EmptyBorder(0, 0, 0, 0),
								"", javax.swing.border.TitledBorder.CENTER,
								javax.swing.border.TitledBorder.BOTTOM,
								new java.awt.Font("Dialog", java.awt.Font.BOLD,
										12), java.awt.Color.red), newSensePanel
								.getBorder()));
				newSensePanel.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
					public void propertyChange(java.beans.PropertyChangeEvent e) {
						if ("border".equals(e.getPropertyName()))
							throw new RuntimeException();
					}
				});

				// ---- entryProperties ----
				entryProperties.setText("Entry Properties");
				entryProperties.setFont(entryProperties.getFont().deriveFont(
						entryProperties.getFont().getSize() - 2f));

				// ---- lexemeForm ----
				lexemeForm.setText("Lexeme Form");
				lexemeForm.setFont(lexemeForm.getFont().deriveFont(
						lexemeForm.getFont().getSize() - 2f));

				// ---- lexemeIPALabel ----
				lexemeIPALabel.setText("Lexeme Form IPA");
				lexemeIPALabel.setFont(lexemeIPALabel.getFont().deriveFont(
						lexemeIPALabel.getFont().getSize() - 2f));

				// ---- lexemeFormText ----
				lexemeFormText.setFont(lexemeFormText.getFont().deriveFont(
						lexemeFormText.getFont().getSize() - 2f));

				// ---- lexemeIPA ----
				lexemeIPA.setFont(lexemeIPA.getFont().deriveFont(
						lexemeIPA.getFont().getSize() - 2f));

				// ---- morphemeType ----
				morphemeType.setText("Morpheme Type:");
				morphemeType.setFont(morphemeType.getFont().deriveFont(
						morphemeType.getFont().getSize() - 2f));

				// ---- morphemeComboBox ----
				morphemeComboBox.setFont(morphemeComboBox.getFont().deriveFont(
						morphemeComboBox.getFont().getSize() - 2f));

				// ---- complexFormLabel ----
				complexFormLabel.setText("Complex Form Type:");
				complexFormLabel.setFont(complexFormLabel.getFont().deriveFont(
						complexFormLabel.getFont().getSize() - 2f));

				// ---- complexFormComboBox ----
				complexFormComboBox.setFont(complexFormComboBox.getFont()
						.deriveFont(
								complexFormComboBox.getFont().getSize() - 2f));

				// ---- glossLabel ----
				glossLabel.setText("Gloss");
				glossLabel.setFont(glossLabel.getFont().deriveFont(
						glossLabel.getFont().getSize() - 2f));

				glossLabel2.setText("Gloss");
				glossLabel2.setFont(glossLabel2.getFont().deriveFont(
						glossLabel2.getFont().getSize() - 2f));

				// ---- Gloss ----
				Gloss.setForeground(Color.black);
				Gloss.setFont(Gloss.getFont().deriveFont(
						Gloss.getFont().getSize() - 2f));

				Glossb.setForeground(Color.black);
				Glossb.setFont(Glossb.getFont().deriveFont(
						Glossb.getFont().getSize() - 2f));
				Glossb.setPreferredSize(new Dimension(200, 20));

				// ---- inflectionalLabel ----
				inflectionalLabel.setText("Inflectional Affix Gloss Builder");
				inflectionalLabel.setFont(inflectionalLabel.getFont().deriveFont(
						inflectionalLabel.getFont().getSize() - 2f));

				// ---- gramInfoLabel ----
				gramInfoLabel.setText("Grammatical Info.");
				gramInfoLabel.setFont(gramInfoLabel.getFont().deriveFont(
						gramInfoLabel.getFont().getSize() - 2f));

				// ---- categoryLabel ----
				categoryLabel.setText("Category:");
				categoryLabel.setFont(categoryLabel.getFont().deriveFont(
						categoryLabel.getFont().getSize() - 2f));

				categoryLabel2.setText("Category:");
				categoryLabel2.setFont(categoryLabel2.getFont().deriveFont(
						categoryLabel2.getFont().getSize() - 2f));

				// ---- categoryComboBox ----
				categoryComboBox
						.setFont(categoryComboBox.getFont().deriveFont(
								categoryComboBox.getFont().getSize() - 2f));

				categoryComboBox2
						.setFont(categoryComboBox2.getFont().deriveFont(
								categoryComboBox2.getFont().getSize() - 2f));

				// ---- similarEntriesLabel ----
				similarEntriesLabel.setText("Similar Entries");
				similarEntriesLabel.setFont(similarEntriesLabel.getFont().deriveFont(
						similarEntriesLabel.getFont().getSize() - 2f));

				// ---- createButton ----
				createButton.setText("Create");
				createButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						createEntryActionPerformed(e, model);
					}
				});

				createButtonb.setText("Create");
				createButtonb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						createSenseActionPerformed(e, model);
					}
				});

				// ---- closeButton ----
				closeButton.setText("Cancel");
				closeButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						closeActionPerformed(e);
					}
				});

				GroupLayout newEntryLayout = new GroupLayout(newEntryPanel);
				newEntryPanel.setLayout(newEntryLayout);
				newEntryLayout
						.setHorizontalGroup(newEntryLayout
								.createParallelGroup()
								.addGroup(
										newEntryLayout
												.createSequentialGroup()
												.addGroup(
														newEntryLayout
																.createParallelGroup()
																.addGroup(
																		GroupLayout.Alignment.TRAILING,
																		newEntryLayout
																				.createSequentialGroup()
																				.addGap(10,
																						10,
																						10)
																				.addComponent(
																						entryProperties)
																				.addPreferredGap(
																						LayoutStyle.ComponentPlacement.RELATED)
																				.addComponent(
																						separator))
																.addGroup(
																		newEntryLayout
																				.createSequentialGroup()
																				.addContainerGap()
																				.addGroup(
																						newEntryLayout
																								.createParallelGroup()
																								.addComponent(
																										morphemeType,
																										GroupLayout.PREFERRED_SIZE,
																										102,
																										GroupLayout.PREFERRED_SIZE)
																								.addComponent(
																										morphemeComboBox,
																										GroupLayout.PREFERRED_SIZE,
																										GroupLayout.DEFAULT_SIZE,
																										GroupLayout.PREFERRED_SIZE))
																				.addPreferredGap(
																						LayoutStyle.ComponentPlacement.RELATED)
																				.addGroup(
																						newEntryLayout
																								.createParallelGroup()
																								.addComponent(
																										complexFormLabel,
																										GroupLayout.DEFAULT_SIZE,
																										GroupLayout.DEFAULT_SIZE,
																										Short.MAX_VALUE)
																								.addGroup(
																										newEntryLayout
																												.createSequentialGroup()
																												.addComponent(
																														complexFormComboBox,
																														GroupLayout.PREFERRED_SIZE,
																														213,
																														GroupLayout.PREFERRED_SIZE)
																												.addGap(0,
																														0,
																														Short.MAX_VALUE))))
																.addGroup(
																		newEntryLayout
																				.createSequentialGroup()
																				.addContainerGap()
																				.addGroup(
																						newEntryLayout
																								.createParallelGroup(
																										GroupLayout.Alignment.TRAILING)
																								.addComponent(
																										Gloss,
																										GroupLayout.PREFERRED_SIZE,
																										323,
																										GroupLayout.PREFERRED_SIZE)
																								.addGroup(
																										newEntryLayout
																												.createParallelGroup()
																												.addComponent(
																														categoryLabel,
																														GroupLayout.PREFERRED_SIZE,
																														52,
																														GroupLayout.PREFERRED_SIZE)
																												.addComponent(
																														categoryComboBox,
																														GroupLayout.PREFERRED_SIZE,
																														213,
																														GroupLayout.PREFERRED_SIZE)))
																				.addPreferredGap(
																						LayoutStyle.ComponentPlacement.RELATED)
																				.addComponent(
																						inflectionalLabel,
																						GroupLayout.DEFAULT_SIZE,
																						GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE))
																.addGroup(
																		newEntryLayout
																				.createSequentialGroup()
																				.addContainerGap()
																				.addGroup(
																						newEntryLayout
																								.createParallelGroup()
																								.addGroup(
																										newEntryLayout
																												.createParallelGroup(
																														GroupLayout.Alignment.TRAILING,
																														false)
																												.addComponent(
																														lexemeFormText,
																														GroupLayout.Alignment.LEADING)
																												.addComponent(
																														lexemeIPA,
																														GroupLayout.Alignment.LEADING,
																														GroupLayout.DEFAULT_SIZE,
																														317,
																														Short.MAX_VALUE))
																								.addComponent(
																										lexemeForm)
																								.addComponent(
																										lexemeIPALabel))
																				.addGap(0,
																						0,
																						Short.MAX_VALUE))
																.addGroup(
																		newEntryLayout
																				.createSequentialGroup()
																				.addContainerGap()
																				.addComponent(
																						glossLabel)
																				.addPreferredGap(
																						LayoutStyle.ComponentPlacement.RELATED)
																				.addComponent(
																						separator)))
												.addContainerGap())
								.addGroup(
										newEntryLayout
												.createSequentialGroup()
												.addContainerGap()
												.addGroup(
														newEntryLayout
																.createParallelGroup()
																.addGroup(
																		newEntryLayout
																				.createSequentialGroup()
																				.addGroup(
																						newEntryLayout
																								.createParallelGroup(
																										GroupLayout.Alignment.TRAILING)
																								.addGroup(
																										newEntryLayout
																												.createSequentialGroup()
																												.addGap(0,
																														0,
																														Short.MAX_VALUE)
																												.addComponent(
																														createButton)
																												.addPreferredGap(
																														LayoutStyle.ComponentPlacement.UNRELATED)
																												.addComponent(
																														closeButton)))
																				.addGap(49,
																						49,
																						49))
																.addGroup(
																		newEntryLayout
																				.createSequentialGroup()
																				.addGroup(
																						newEntryLayout
																								.createParallelGroup()
																								.addGroup(
																										newEntryLayout
																												.createSequentialGroup()
																												.addComponent(
																														similarEntriesLabel)
																												.addPreferredGap(
																														LayoutStyle.ComponentPlacement.RELATED)
																												.addComponent(
																														separator,
																														GroupLayout.PREFERRED_SIZE,
																														398,
																														GroupLayout.PREFERRED_SIZE)
																												.addGap(0,
																														0,
																														Short.MAX_VALUE))
																								.addGroup(
																										newEntryLayout
																												.createSequentialGroup()
																												.addComponent(
																														gramInfoLabel)
																												.addPreferredGap(
																														LayoutStyle.ComponentPlacement.RELATED)
																												.addComponent(
																														separator)))
																				.addGap(20,
																						20,
																						20)))));
				newEntryLayout
						.setVerticalGroup(newEntryLayout
								.createParallelGroup()
								.addGroup(
										newEntryLayout
												.createSequentialGroup()
												.addContainerGap()
												.addGroup(
														newEntryLayout
																.createParallelGroup(
																		GroupLayout.Alignment.TRAILING)
																.addComponent(
																		separator,
																		GroupLayout.PREFERRED_SIZE,
																		5,
																		GroupLayout.PREFERRED_SIZE)
																.addComponent(
																		entryProperties))
												.addPreferredGap(
														LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(lexemeForm)
												.addGap(7, 7, 7)
												.addGroup(
														newEntryLayout
																.createParallelGroup(
																		GroupLayout.Alignment.TRAILING)
																.addGroup(
																		newEntryLayout
																				.createSequentialGroup()
																				.addComponent(
																						lexemeFormText,
																						GroupLayout.PREFERRED_SIZE,
																						GroupLayout.DEFAULT_SIZE,
																						GroupLayout.PREFERRED_SIZE)
																				.addPreferredGap(
																						LayoutStyle.ComponentPlacement.RELATED)
																				.addComponent(
																						lexemeIPALabel)
																				.addComponent(
																						lexemeIPA,
																						GroupLayout.PREFERRED_SIZE,
																						GroupLayout.DEFAULT_SIZE,
																						GroupLayout.PREFERRED_SIZE)
																				.addPreferredGap(
																						LayoutStyle.ComponentPlacement.RELATED)
																				.addGroup(
																						newEntryLayout
																								.createParallelGroup()
																								.addComponent(
																										complexFormLabel)
																								.addComponent(
																										morphemeType,
																										GroupLayout.Alignment.TRAILING))
																				.addPreferredGap(
																						LayoutStyle.ComponentPlacement.RELATED)
																				.addGroup(
																						newEntryLayout
																								.createParallelGroup()
																								.addComponent(
																										morphemeComboBox,
																										GroupLayout.PREFERRED_SIZE,
																										GroupLayout.DEFAULT_SIZE,
																										GroupLayout.PREFERRED_SIZE)
																								.addComponent(
																										complexFormComboBox,
																										GroupLayout.PREFERRED_SIZE,
																										GroupLayout.DEFAULT_SIZE,
																										GroupLayout.PREFERRED_SIZE))
																				.addPreferredGap(
																						LayoutStyle.ComponentPlacement.RELATED)
																				.addGroup(
																						newEntryLayout
																								.createParallelGroup()
																								.addComponent(
																										glossLabel)
																								.addComponent(
																										separator,
																										GroupLayout.PREFERRED_SIZE,
																										10,
																										GroupLayout.PREFERRED_SIZE))
																				.addPreferredGap(
																						LayoutStyle.ComponentPlacement.RELATED)
																				.addGroup(
																						newEntryLayout
																								.createParallelGroup(
																										GroupLayout.Alignment.BASELINE)
																								.addComponent(
																										Gloss,
																										GroupLayout.PREFERRED_SIZE,
																										GroupLayout.DEFAULT_SIZE,
																										GroupLayout.PREFERRED_SIZE)
																								.addComponent(
																										inflectionalLabel))
																				.addPreferredGap(
																						LayoutStyle.ComponentPlacement.RELATED)
																				.addGroup(
																						newEntryLayout
																								.createParallelGroup()
																								.addGroup(
																										newEntryLayout
																												.createSequentialGroup()
																												.addComponent(
																														gramInfoLabel)
																												.addGap(14,
																														14,
																														14))
																								.addGroup(
																										GroupLayout.Alignment.TRAILING,
																										newEntryLayout
																												.createSequentialGroup()
																												.addComponent(
																														separator,
																														GroupLayout.PREFERRED_SIZE,
																														GroupLayout.DEFAULT_SIZE,
																														GroupLayout.PREFERRED_SIZE)
																												.addPreferredGap(
																														LayoutStyle.ComponentPlacement.RELATED)))
																				.addComponent(
																						categoryLabel,
																						GroupLayout.PREFERRED_SIZE,
																						14,
																						GroupLayout.PREFERRED_SIZE)
																				.addPreferredGap(
																						LayoutStyle.ComponentPlacement.RELATED)
																				.addComponent(
																						categoryComboBox,
																						GroupLayout.PREFERRED_SIZE,
																						GroupLayout.DEFAULT_SIZE,
																						GroupLayout.PREFERRED_SIZE)
																				.addPreferredGap(
																						LayoutStyle.ComponentPlacement.RELATED)
																				.addComponent(
																						similarEntriesLabel))
																.addComponent(
																		separator,
																		GroupLayout.PREFERRED_SIZE,
																		7,
																		GroupLayout.PREFERRED_SIZE))
												.addGap(14, 14, 14)
												.addPreferredGap(
														LayoutStyle.ComponentPlacement.RELATED,
														GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addGroup(
														newEntryLayout
																.createParallelGroup(
																		GroupLayout.Alignment.BASELINE)
																.addComponent(
																		closeButton)
																.addComponent(
																		createButton))
												.addGap(58, 58, 58)));
			}

			GroupLayout dialogNewEntryContentPaneLayout = new GroupLayout(
					dialogNewEntryContentPane);
			dialogNewEntryContentPane
					.setLayout(dialogNewEntryContentPaneLayout);
			dialogNewEntryContentPaneLayout
					.setHorizontalGroup(dialogNewEntryContentPaneLayout
							.createParallelGroup().addGroup(
									dialogNewEntryContentPaneLayout
											.createSequentialGroup()
											.addComponent(newEntryPanel,
													GroupLayout.PREFERRED_SIZE,
													GroupLayout.DEFAULT_SIZE,
													GroupLayout.PREFERRED_SIZE)
											.addGap(0, 0, Short.MAX_VALUE)));
			dialogNewEntryContentPaneLayout
					.setVerticalGroup(dialogNewEntryContentPaneLayout
							.createParallelGroup().addComponent(newEntryPanel,
									GroupLayout.Alignment.TRAILING,
									GroupLayout.DEFAULT_SIZE,
									GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
			dialogNewEntry.pack();
			dialogNewEntry.setLocationRelativeTo(dialogNewEntry.getOwner());

			
			
			newSensePanel.setLayout(new FlowLayout());
			newSensePanel.add(glossLabel2);
			newSensePanel.add(Glossb);
			newSensePanel.add(categoryLabel2);
			newSensePanel.add(categoryComboBox2);
			newSensePanel.add(createButtonb);
			
			GroupLayout senseNewEntryContentPaneLayout = new GroupLayout(
					senseNewEntryContentPane);
			senseNewEntryContentPane.setLayout(senseNewEntryContentPaneLayout);
			senseNewEntryContentPaneLayout
					.setHorizontalGroup(senseNewEntryContentPaneLayout
							.createParallelGroup().addGroup(
									senseNewEntryContentPaneLayout
											.createSequentialGroup()
											.addComponent(newSensePanel,
													GroupLayout.PREFERRED_SIZE,
													GroupLayout.DEFAULT_SIZE,
													GroupLayout.PREFERRED_SIZE)
											.addGap(0, 0, Short.MAX_VALUE)));
			senseNewEntryContentPaneLayout
					.setVerticalGroup(senseNewEntryContentPaneLayout
							.createParallelGroup().addComponent(newSensePanel,
									GroupLayout.Alignment.TRAILING,
									GroupLayout.DEFAULT_SIZE,
									GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
			senseNewEntry.pack();
			senseNewEntry.setLocationRelativeTo(senseNewEntry.getOwner());

		}
	}

	/** ================================================================================
	 * 								Variable declaration
	 *  ================================================================================
	 */ 
	private JPanel panel1;
	private JPanel panel1d;
	private JPanel panel1dsenses;
	private JScrollPane tableScrollPane;
	private JTable table;
	private JTextField entryDisplay;
	private JButton saveFile;
	private JButton retrieveFile;
	private JButton addEntry;
	private JButton newSense;
	private JLabel entriesLabel;
	private JLabel entryLabel;
	private JLabel entryLexemeForm;
	private JDialog dialogNewEntry;
	private JDialog senseNewEntry;
	private JPanel newEntryPanel;
	private JLabel entryProperties;
	private JLabel lexemeIPALabel;
	private JLabel lexemeForm;
	private JTextField lexemeFormText;
	private JTextField lexemeIPA;
	private JLabel morphemeType;
	private JComboBox morphemeComboBox;
	private JLabel complexFormLabel;
	private JComboBox complexFormComboBox;
	private JSeparator separator;
	private JLabel glossLabel;
	private JTextField Gloss;
	private JLabel inflectionalLabel;
	private JLabel gramInfoLabel;
	private JLabel categoryLabel;
	private JComboBox categoryComboBox;
	private JLabel similarEntriesLabel;
	private JButton createButton;
	private JButton closeButton;
	private JPanel newSensePanel;
	private JComboBox morphemeComboDisplay;
	private JLabel currentMorph;
	private JTextField Glossb;
	private JLabel glossLabel2;
	private JLabel categoryLabel2;
	private JComboBox categoryComboBox2;
	private JButton createButtonb;

	/** ================================================================================
	 * 							Table Model and ActionListeners
	 * 
	 *  MyTableModel, MultiLine Cell Renderer,
	 *  New Entry Action Listener, New Sense Action Listener,
	 *  New Entry: Create Action Listener, New Sense Create Action Listener,
	 *  Delete Sense Action Listener, New Entry: Close Action Listener,
	 *  Change Morph ComboBox Listener, Change Gram. Info ComboBox Listener,
	 *  Manually Save File Action Listener, Manually Open File Action Listener
	 *  ================================================================================
	 */ 
	
	// ======= MyTableModel =======
	class MyTableModel extends DefaultTableModel implements TableModel {

		public MyTableModel() {
			super();
			addColumn("Headword", new Vector<LexiconObject>());
			addColumn("Lexeme Form", new Vector<String>());
			addColumn("Glosses", new Vector<String>());
			addColumn("Grammatical Info.", new Vector<String>());
		}

		@Override
		public Object getValueAt(int row, int column) {
			if (column == 0) {
				return super.getValueAt(row, column);
			} else if (column == 1) {
				return ((LexiconObject) super.getValueAt(row, 0)).getLexemeFormIPA();
			} else if (column == 2) {
				String glosses = "";
				for (String gloss : ((LexiconObject) super.getValueAt(row, 0)).getGlosses()) {
					glosses += gloss + "\n";
				}
				return glosses;
			} else if (column == 3) {
				String categories = "";
				for (String cat : ((LexiconObject) super.getValueAt(row, 0)).getCategories()) {
					categories += cat + "\n";
				}
				return categories;
			} else
				return null;
		}

	}
	
	public JTable getTable() {
		return this.table;
	}

	// ======= MultiLine Cell Renderer =======
	class MultiLineCellRenderer extends JTextArea implements TableCellRenderer {

		  public MultiLineCellRenderer() {
		    setLineWrap(true);
		    setWrapStyleWord(true);
		    setOpaque(true);
		  }

		  public Component getTableCellRendererComponent(JTable table, Object value,
		      boolean isSelected, boolean hasFocus, int row, int column) {
		    if (isSelected) {
		      setForeground(SystemColor.textHighlightText);
		      setBackground(SystemColor.textHighlight);
		    } else {
		      setForeground(table.getForeground());
		      setBackground(table.getBackground());
		    }
		    setFont(table.getFont());
		    setText((value == null) ? "" : value.toString());
		    setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		    
		    return this;
		  }
	}
	
	// ======= New Entry ActionListener =======
	private void addEntryActionPerformed(ActionEvent e) {
		if (e.getSource() == addEntry) {
			dialogNewEntry.setVisible(true);
		}
	}

	// ======= New Sense ActionListener =======
	private void newSenseActionPerformed(ActionEvent e) {
		if (e.getSource() == newSense) {
			senseNewEntry.setVisible(true);
		}
	}

	// ======= New Entry: Create ActionListener =======
	private void createEntryActionPerformed(ActionEvent e,
			DefaultTableModel model) {

		LexiconObject entry = new LexiconObject(lexemeFormText.getText(),
				lexemeIPA.getText(),
				(String) morphemeComboBox.getSelectedItem(),
				(String) complexFormComboBox.getSelectedItem(),
				Gloss.getText(), (String) categoryComboBox.getSelectedItem());
		((MyTableModel) table.getModel()).insertRow(table.getModel()
				.getRowCount(),
				new Object[] { entry, entry.getLexemeFormIPA(), entry.getGloss(),
						entry.getCategory() });

		// reset fields
		lexemeFormText.setText("");
		lexemeIPA.setText("");
		Gloss.setText("");
		categoryComboBox.setSelectedIndex(17);
		dialogNewEntry.dispose();
	}

	// ======= New Sense: Create ActionListener =======
	private void createSenseActionPerformed(ActionEvent e,
			DefaultTableModel model) {
		int row = table.convertRowIndexToModel(table.getSelectedRow());
		LexiconObject selected = (LexiconObject) table.getModel().getValueAt(row, 0);
		String newSense = Glossb.getText();
		String newCategory = (String) categoryComboBox2.getSelectedItem();
		selected.addSense(newSense, newCategory);
		int senseNumber = selected.getSenseCount();
		
		// set-up
		JLabel sense = new JLabel();
		JButton delete = new JButton();
		JLabel gloss = new JLabel();
		JLabel gram = new JLabel();
		JTextField glossText = new JTextField();
		glossText.setMaximumSize(new Dimension(1000,100));
		String[] categoryItems = { "Adjective", "Adposition", "Adverb",
				"Classifier", "Connective", "Determiner", "Enclitic",
				"English numbers", "Interjection", "Noun", "Numeral",
				"Particle", "Interrogative pro-form", "Pro-adverb", "Pronoun",
				"Intransitive verb", "Transitive verb", "<Not Sure>"};
		JComboBox gramComboBox = new JComboBox(categoryItems);
		
		JPanel sensePanel = new JPanel();
		sensePanel.setLayout(new BoxLayout(sensePanel, BoxLayout.X_AXIS));
		JPanel glossPanel = new JPanel();
		glossPanel.setLayout(new BoxLayout(glossPanel, BoxLayout.X_AXIS));
		JPanel gramPanel = new JPanel();
		gramPanel.setLayout(new BoxLayout(gramPanel, BoxLayout.X_AXIS));
			
		// fill
		sense.setText("Sense " + String.valueOf(senseNumber));
		delete.setText("Delete");
		gloss.setText("Gloss: ");
		gram.setText("Grammatical Info.: ");
		glossText.setText(selected.getGloss(senseNumber));
		int index = -1;
		for (int i = 0; i<categoryItems.length; i++) {
			if (categoryItems[i].equals(selected.getCategory(senseNumber))) {
				index = i;
			}
		}
		gramComboBox.setSelectedIndex(index);
			
		// add Delete, Gloss and ComboBox Change Listeners
		final int s = senseNumber;
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteSenseActionListener(e, s);
			}
		});
		
		final JTextField g = glossText;
		glossText.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				g.getDocument().addDocumentListener(new DocumentListener() {

					@Override
					public void insertUpdate(DocumentEvent e) {
						Document doc = (Document) e.getDocument();
						String text;
						try {
							text = doc.getText(0, doc.getLength());
							int row = table.convertRowIndexToModel(table.getSelectedRow());
							LexiconObject selected = (LexiconObject) table.getModel().getValueAt(row, 0);
							selected.setGloss(s, text);
							((MyTableModel) table.getModel()).fireTableRowsUpdated(row,row);
						} catch (BadLocationException e1) {e1.printStackTrace();}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						Document doc = (Document) e.getDocument();
						String text;
						try {
							text = doc.getText(0, doc.getLength());
							int row = table.convertRowIndexToModel(table.getSelectedRow());
							LexiconObject selected = (LexiconObject) table.getModel().getValueAt(row, 0);
							selected.setGloss(s, text);
							((MyTableModel) table.getModel()).fireTableRowsUpdated(row,row);
						} catch (BadLocationException e1) {e1.printStackTrace();}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						Document doc = (Document) e.getDocument();
						String text;
						try {
							text = doc.getText(0, doc.getLength());
							int row = table.convertRowIndexToModel(table.getSelectedRow());
							LexiconObject selected = (LexiconObject) table.getModel().getValueAt(row, 0);
							selected.setGloss(s, text);
							((MyTableModel) table.getModel()).fireTableRowsUpdated(row,row);
						} catch (BadLocationException e1) {e1.printStackTrace();}
					}

				});
			}
		});
		
		final JComboBox b = gramComboBox;
		gramComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gramChangeActionPerformed(e, s, b);
			}
		});

		// format
		sensePanel.add(sense);
		sensePanel.add(delete);
		glossPanel.add(gloss);
		glossPanel.add(glossText);
		gramPanel.add(gram);
		gramPanel.add(gramComboBox);
		
		panel1dsenses.add(sensePanel);
		panel1dsenses.add(glossPanel);
		panel1dsenses.add(gramPanel);
		panel1dsenses.add(new JSeparator(SwingConstants.HORIZONTAL));
		panel1d.updateUI();
		
		table.setRowHeight(row, (rowHeight * selected.getSenseCount()) + 2);
		((MyTableModel) table.getModel()).fireTableRowsUpdated(row,row);
		
		// reset fields
		Glossb.setText("");
		categoryComboBox2.setSelectedIndex(17);
		
		senseNewEntry.dispose();
	}

	// ======= Delete Sense ActionListener =======
	private void deleteSenseActionListener(ActionEvent e, int index) {
		int row = table.convertRowIndexToModel(table.getSelectedRow());
		LexiconObject selected = (LexiconObject) table.getModel().getValueAt(
				row, 0);
		selected.deleteSense(index);
		
		((MyTableModel) table.getModel()).fireTableRowsUpdated(row,row);
		
		panel1d.remove(panel1dsenses);
		panel1dsenses.removeAll();
		
		int start = 1;
		int stop = selected.getSenseCount();
		while (start <= stop) {
			
			// Set up
			JLabel sense = new JLabel();
			JButton delete = new JButton();
			JLabel gloss = new JLabel();
			JLabel gram = new JLabel();
			JTextField glossText = new JTextField();
			glossText.setMaximumSize(new Dimension(1000,100));
			String[] categoryItems = { "Adjective", "Adposition", "Adverb",
					"Classifier", "Connective", "Determiner", "Enclitic",
					"English numbers", "Interjection", "Noun", "Numeral",
					"Particle", "Interrogative pro-form", "Pro-adverb", "Pronoun",
					"Intransitive verb", "Transitive verb", "<Not Sure>"};
			JComboBox gramComboBox = new JComboBox(categoryItems);
			
			JPanel sensePanel = new JPanel();
			sensePanel.setLayout(new BoxLayout(sensePanel, BoxLayout.X_AXIS));
			JPanel glossPanel = new JPanel();
			glossPanel.setLayout(new BoxLayout(glossPanel, BoxLayout.X_AXIS));
			JPanel gramPanel = new JPanel();
			gramPanel.setLayout(new BoxLayout(gramPanel, BoxLayout.X_AXIS));
			
			// Fill
			sense.setText("Sense " + String.valueOf(start));
			delete.setText("Delete");
			gloss.setText("Gloss: ");
			gram.setText("Grammatical Info.: ");
			glossText.setText(selected.getGloss(start));
			int temp = -1;
			for (int i = 0; i<categoryItems.length; i++) {
				if (categoryItems[i].equals(selected.getCategory(start))) {
					temp = i;
				}
			}
			gramComboBox.setSelectedIndex(temp);
			
			// Add Delete, Gloss and ComboBox Change Listeners
			final int s = start;
			
			delete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					deleteSenseActionListener(e, s);
				}
			});
			
			final JTextField g = glossText;
			glossText.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					g.getDocument().addDocumentListener(new DocumentListener() {

						@Override
						public void insertUpdate(DocumentEvent e) {
							Document doc = (Document) e.getDocument();
							String text;
							try {
								text = doc.getText(0, doc.getLength());
								int row = table.convertRowIndexToModel(table.getSelectedRow());
								LexiconObject selected = (LexiconObject) table.getModel().getValueAt(row, 0);
								selected.setGloss(s, text);
								((MyTableModel) table.getModel()).fireTableRowsUpdated(row,row);
							} catch (BadLocationException e1) {e1.printStackTrace();}
						}

						@Override
						public void removeUpdate(DocumentEvent e) {
							Document doc = (Document) e.getDocument();
							String text;
							try {
								text = doc.getText(0, doc.getLength());
								int row = table.convertRowIndexToModel(table.getSelectedRow());
								LexiconObject selected = (LexiconObject) table.getModel().getValueAt(row, 0);
								selected.setGloss(s, text);
								((MyTableModel) table.getModel()).fireTableRowsUpdated(row,row);
							} catch (BadLocationException e1) {e1.printStackTrace();}
						}

						@Override
						public void changedUpdate(DocumentEvent e) {
							Document doc = (Document) e.getDocument();
							String text;
							try {
								text = doc.getText(0, doc.getLength());
								int row = table.convertRowIndexToModel(table.getSelectedRow());
								LexiconObject selected = (LexiconObject) table.getModel().getValueAt(row, 0);
								selected.setGloss(s, text);
								((MyTableModel) table.getModel()).fireTableRowsUpdated(row,row);
							} catch (BadLocationException e1) {e1.printStackTrace();}
						}

					});
				}
			});
			
			final JComboBox b = gramComboBox;
			gramComboBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gramChangeActionPerformed(e, s, b);
				}
			});

			// format
			sensePanel.add(sense);
			sensePanel.add(delete);
			glossPanel.add(gloss);
			glossPanel.add(glossText);
			gramPanel.add(gram);
			gramPanel.add(gramComboBox);
			
			panel1dsenses.add(sensePanel);
			panel1dsenses.add(glossPanel);
			panel1dsenses.add(gramPanel);
			panel1dsenses.add(new JSeparator(SwingConstants.HORIZONTAL));
			
			start++;
		}

		panel1d.add(panel1dsenses);
		panel1d.updateUI();
		
	}
	
	// ======= New Entry: Close ActionListener =======
	private void closeActionPerformed(ActionEvent e) {
		lexemeFormText.setText("");
		lexemeIPA.setText("");
		Gloss.setText("");
		categoryComboBox.setSelectedIndex(17);
		dialogNewEntry.dispose();
	}

	// ======= Change Morph ActionListener =======
	private void morphChangeActionPerformed(ActionEvent e) {
		int row = table.convertRowIndexToModel(table.getSelectedRow());
		LexiconObject selected = (LexiconObject) table.getModel().getValueAt(
				row, 0);
		selected.setMorphemeType((String) morphemeComboDisplay
				.getSelectedItem());
	}
	
	// ======= Change Grammatical Info. ActionListener =======
	private void gramChangeActionPerformed(ActionEvent e, int sense, JComboBox gram) {
		int row = table.convertRowIndexToModel(table.getSelectedRow());
		LexiconObject selected = (LexiconObject) table.getModel().getValueAt(
				row, 0);
		selected.setCategory(sense, (String) gram.getSelectedItem());
		((MyTableModel) table.getModel()).fireTableRowsUpdated(row,row);
	}

	// ======= Manually Save File Action Listener =======
	private void saveFileActionPerformed(ActionEvent e) {

		// writing file
		try (BufferedWriter writer = Files.newBufferedWriter(
				Paths.get(LEXICON_ENTRIES_FILE), Charset.forName("UTF-8"))) {

			for (int i = 0; i < table.getRowCount(); i++) {
				LexiconObject x = (LexiconObject) table.getModel().getValueAt(
						i, 0);
				writer.write(x.getLexemeForm() + "\t" + x.getLexemeFormIPA() + "\t"
						+ x.getMorphemeType() + "\t" + x.getComplexFormType()
						+ "\t" + x.getGloss() + "\t" + x.getCategory());
				// write multiple senses
				int start = 2;
				int stop = x.getSenseCount();
				while (start <= stop) {
					writer.write("\t" + x.getGloss(start) + "\t"
							+ x.getCategory(start));
					start++;
				}
				writer.newLine();
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	// ======= Manually Open File ActionListener =======
	private void retrieveFileActionPerformed(ActionEvent e) {

		// reading file
		String name = "";
		String date = "";
		String lastSceneStr = "";
		// read info file
		try (BufferedReader reader = Files.newBufferedReader(
				Paths.get(LEXICON_ENTRIES_FILE), Charset.forName("UTF-8"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] split = line.split("\t");
				String arr = split[0];
				String arripa = split[1];
				String morph = split[2];
				String complex = split[3];
				String gloss = split[4];
				String category = split[5];
				LexiconObject entry = new LexiconObject(arr, arripa, morph,
						complex, gloss, category);
				int start = 6;
				int stop = split.length;
				while (start < stop) {
					entry.addSense(split[start], split[start + 1]);
					start = (start + 2);
				}
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
