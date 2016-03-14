import java.io.*;
import javax.swing.*;
import java.util.*;
import java.net.*;

import java.awt.Color;
import java.awt.Font;
import javax.swing.text.DefaultCaret;
import java.awt.event.*;

public class ConceptualSummaryInterface
{
	private JFrame conceptualSummaryFrame;
	private JPanel guiPanel;
	private JLabel headerLabel, queryLabel, paragraphLabel, summaryLabel;
	private JTextField queryField;
	private JTextArea paragraphArea, summaryArea;
	private JButton clearButton, resetButton, addFileButton, summarizeButton, saveFileButton, infoButton;
	private JScrollPane summaryScrollPane, paragraphScrollPane;
	private String openDirectory, saveDirectory;
	private SentenceSplitter splitter;
	private SentenceMerger merger;

	protected ConceptualSummaryInterface()
	{
		ConceptualSummaryInterface gui = this;
		splitter = new SentenceSplitter();
		merger = new SentenceMerger();
		openDirectory = null;
		saveDirectory = null;

		conceptualSummaryFrame = new JFrame();
		conceptualSummaryFrame.setBounds(30,30,700,725);
		conceptualSummaryFrame.setTitle("Conceptual Summary");
		conceptualSummaryFrame.setResizable(false);
		conceptualSummaryFrame.setVisible(true);
		conceptualSummaryFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		conceptualSummaryFrame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent we)
			{
				if( JOptionPane.showConfirmDialog(conceptualSummaryFrame, "Are you sure?", "Exit?",
					JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION )
				{
					conceptualSummaryFrame.dispose();
					System.exit(0);
				}
			}
		});

		guiPanel = new JPanel();
		headerLabel = new JLabel("Conceptual Summary");
		queryLabel = new JLabel("Enter Query :");
		paragraphLabel = new JLabel("Paragraph :");
		summaryLabel = new JLabel("Summary :");

		queryField = new JTextField();
		paragraphArea = new JTextArea();
		summaryArea = new JTextArea();
		clearButton = new JButton("Clear");
		infoButton = new JButton("i");
		resetButton = new JButton("Reset");
		addFileButton = new JButton("Open file");
		summarizeButton = new JButton("Generate Summary");
		saveFileButton = new JButton("Save as file");


		guiPanel.setBounds(0,0,700,725);
		headerLabel.setBounds(130,2,500,50);
		queryLabel.setBounds(15,365,150,25);
		paragraphLabel.setBounds(15,65,150,25);
		summaryLabel.setBounds(15,415,100,25);

		queryField.setBounds(10,385,500,30);
		paragraphArea.setBounds(10,90,680,270);
		summaryArea.setBounds(10,440,680,220);

		clearButton.setBounds(590,60,100,25);
		infoButton.setBounds(623,13,35,35);
		addFileButton.setBounds(480,60,100,25);
		summarizeButton.setBounds(520,390,170,25);

		resetButton.setBounds(10,665,100,25);		
		saveFileButton.setBounds(570,665,120,25);



		headerLabel.setForeground(Color.WHITE);
		queryLabel.setForeground(Color.WHITE);
		paragraphLabel.setForeground(Color.WHITE);
		summaryLabel.setForeground(Color.WHITE);
		
		guiPanel.setBackground(Color.green.darker().darker().darker());
		
		headerLabel.setFont(new Font("Serif",Font.BOLD,34));
		queryLabel.setFont(new Font("Serif",Font.BOLD,12));
		paragraphLabel.setFont(new Font("Serif",Font.BOLD,12));
		summaryLabel.setFont(new Font("Serif",Font.BOLD,12));
		queryField.setFont(new Font("Serif",Font.ITALIC,15));
		clearButton.setFont(new Font("Serif",Font.BOLD,12));
		infoButton.setFont(new Font("Serif",Font.BOLD,20));
		resetButton.setFont(new Font("Serif",Font.BOLD,12));
		addFileButton.setFont(new Font("Serif",Font.BOLD,12));
		summarizeButton.setFont(new Font("Serif",Font.BOLD,12));
		saveFileButton.setFont(new Font("Serif",Font.BOLD,12));

		// queryField.setHorizontalAlignment(JTextField.CENTER);

		paragraphArea.setLineWrap(true);
		summaryArea.setLineWrap(true);
		paragraphArea.setWrapStyleWord(true);
		summaryArea.setWrapStyleWord(true);


		guiPanel.setBorder(BorderFactory.createRaisedBevelBorder());

		summaryArea.setEditable(false);



		addFileButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				JFileChooser fileChooser = new JFileChooser(openDirectory);
				fileChooser.setMultiSelectionEnabled(false);
				int returnValue = fileChooser.showOpenDialog(conceptualSummaryFrame);
				if(returnValue == JFileChooser.APPROVE_OPTION)
				{
					paragraphArea.setText("");
					File file = fileChooser.getSelectedFile();
					openDirectory = file.getParentFile().getAbsolutePath();
					try
					{
						FileReader fReader = new FileReader(file);
						BufferedReader bReader = new BufferedReader(fReader);

						String line = null;
						String totalText = "";
						if(file.getName().endsWith(".tsv"))
						{
							String inputKey = JOptionPane.showInputDialog(conceptualSummaryFrame, "Enter the Key:",
							 "Key required", JOptionPane.QUESTION_MESSAGE);
							while( (line = bReader.readLine()) != null )
							{
								String data[] = line.split("\t");
								if(data[0].equalsIgnoreCase(inputKey))
								{
									if(!data[2].endsWith("."))
										data[2]+=".";
									totalText+=data[2];
									
									totalText+="\n";
								}
							}
							paragraphArea.setText(totalText);
						}
						else
						{
							while( (line = bReader.readLine()) != null )
								totalText+=line+"\n";
							paragraphArea.setText(totalText);
						}
						paragraphArea.setText(paragraphArea.getText().trim());

						bReader.close();
						fReader.close();
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		});

		clearButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				paragraphArea.setText("");
			}
		});

		infoButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				JOptionPane.showMessageDialog(conceptualSummaryFrame, "Generates Conceptual Summary Of A Paragraph\n"
						+ "------------------------------------------------------------------------------\n"
						+ "\n"
						+ "1. Write a paragraph in \"Paragraph\" section or\n"
						+ "use \"Open File\" button to add paragraph from \n"
						+ "a file.\n"
						+ "If the file is .tsv format, provide the key of the\n"
						+ "paragraph. For example- \"MC2-E-0001\".\n"
						+ "\n"
						+ "2. Use \"Clear\" button to clear the paragraph section.\n"
						+ "\n"
						+ "3. Write a query in the the \"Enter Query\" field.\n"
						+ "\n"
						+ "4. Click on \"Generate Summary\" button to generate\n"
						+ "Summary of the paragraph based on the query.\n"
						+ "Wait few seconds....\n"
						+ "\n"
						+ "5. If a message appears with \"OutOfMemoryError\",\n"
						+ "please provide more memory to the JVM using-\n"
						+ "\"java -Xmx2048m -jar <jar-file-name>\" command.\n"
						+ "\n"
						+ "6. Your summary should be generated by now.\n"
						+ "\n"
						+ "7. Use \"Save as File\" button to save the summary.\n"
						+ "\n"
						+ "8. Use \"Reset\" button to reset all the fields.", "INFO", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		resetButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				paragraphArea.setText("");
				summaryArea.setText("");
				queryField.setText("");
			}
		});

		summarizeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if( !queryField.getText().trim().equals("") && !paragraphArea.getText().trim().equals("") )
				{
					try
					{
						ConceptualSummarizer summarizer = new ConceptualSummarizer(paragraphArea.getText(), queryField.getText());
						List<String> output = summarizer.getSummary();
						summarizer = null;
						List<List<String> > splittedOutput = splitter.splitSentence(output);
						List<String> mergedOutput = merger.createSentenceDiagram(splittedOutput);
						JOptionPane.showMessageDialog(conceptualSummaryFrame, "Summary is ready!");
						summaryArea.setText("");
						for( String line : mergedOutput )
							summaryArea.setText(summaryArea.getText() + "\n\n" + line);
						summaryArea.setText(summaryArea.getText().trim());
					}
					catch(OutOfMemoryError ofme)
					{
						JOptionPane.showMessageDialog(conceptualSummaryFrame, "Out of memory error!\nPlease provide more memory to the JVM\n" 
							+ "Use: \"java -Xmx2048m -jar <jar-file-name>\"\nSystem is exiting!", "OutOfMemoryError", JOptionPane.ERROR_MESSAGE);
						System.exit(0);
					}
				}
				else if( paragraphArea.getText().trim().equals("") )
					JOptionPane.showMessageDialog(conceptualSummaryFrame, "Please provide a paragraph!", "Paragraph field is blank",
					 JOptionPane.ERROR_MESSAGE);
				else
					JOptionPane.showMessageDialog(conceptualSummaryFrame, "Please provide a query!", "Query field is blank",
					 JOptionPane.ERROR_MESSAGE);
			}
		});

		saveFileButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if( !summaryArea.getText().trim().equals("") )
				{
					JFileChooser fileChooser = new JFileChooser(saveDirectory);
					int returnValue = fileChooser.showSaveDialog(conceptualSummaryFrame);
					if(returnValue == JFileChooser.APPROVE_OPTION)
					{
						try
						{
							File file = fileChooser.getSelectedFile();
							saveDirectory = file.getParentFile().getAbsolutePath();
							if( !file.exists() )
								file.createNewFile();
							FileWriter fWriter = new FileWriter(file);
							PrintWriter pWriter = new PrintWriter(fWriter, true);
							pWriter.print(summaryArea.getText());
							pWriter.close();
							fWriter.close();
							JOptionPane.showMessageDialog(conceptualSummaryFrame, "Summary saved as " + file.getName() + "!",
								"File saved", JOptionPane.INFORMATION_MESSAGE);
						}
						catch(IOException ioe)
						{
							ioe.printStackTrace();
						}
					}
				}
				else
					JOptionPane.showMessageDialog(conceptualSummaryFrame, "No summary to save!", "Summary field is blank",
					 JOptionPane.ERROR_MESSAGE);
			}
		});


		conceptualSummaryFrame.add(headerLabel);
		conceptualSummaryFrame.add(queryLabel);
		conceptualSummaryFrame.add(paragraphLabel);
		conceptualSummaryFrame.add(summaryLabel);
		conceptualSummaryFrame.add(queryField);
		conceptualSummaryFrame.add(paragraphArea);
		conceptualSummaryFrame.add(summaryArea);
		conceptualSummaryFrame.add(clearButton);
		conceptualSummaryFrame.add(infoButton);
		conceptualSummaryFrame.add(resetButton);
		conceptualSummaryFrame.add(addFileButton);
		conceptualSummaryFrame.add(summarizeButton);
		conceptualSummaryFrame.add(saveFileButton);


		summaryScrollPane = new JScrollPane(summaryArea);
		paragraphScrollPane = new JScrollPane(paragraphArea);

		summaryScrollPane.setBounds(10,440,680,220);
		paragraphScrollPane.setBounds(10,90,680,270);

		conceptualSummaryFrame.add(summaryScrollPane);
		conceptualSummaryFrame.add(paragraphScrollPane);

		conceptualSummaryFrame.add(guiPanel);
	}



	public static void main(String args[])
	{
		/**
		* Create gui
		*/
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
            		for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            		{
                		if ("Nimbus".equals(info.getName()))
                		{
							javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    		break;
                		}
            		}
        		}
        		catch (ClassNotFoundException ex)
        		{
            		ex.printStackTrace();
        		}
        		catch (InstantiationException ex)
        		{
            		ex.printStackTrace();
        		}
        		catch (IllegalAccessException ex)
        		{
            		ex.printStackTrace();
        		}
        		catch (javax.swing.UnsupportedLookAndFeelException ex)
        		{
            		ex.printStackTrace();
        		}
				new ConceptualSummaryInterface();
			}
		});
	}
}