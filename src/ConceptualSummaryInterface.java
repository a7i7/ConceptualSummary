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
	private JLabel headerLabel, titleLabel, paragraphLabel, summaryLabel;
	private JTextField titleField;
	private JTextArea paragraphArea, summaryArea;
	private JButton clearButton, resetButton, addFileButton, summarizeButton, saveFileButton;
	private JScrollPane summaryScrollPane, paragraphScrollPane;
	private String openDirectory, saveDirectory;

	protected ConceptualSummaryInterface()
	{
		ConceptualSummaryInterface gui = this;
		openDirectory = null;
		saveDirectory = null;

		conceptualSummaryFrame = new JFrame();
		conceptualSummaryFrame.setBounds(30,30,700,700);
		conceptualSummaryFrame.setTitle("Conceptual Summary");
		conceptualSummaryFrame.setResizable(false);
		conceptualSummaryFrame.setVisible(true);
		conceptualSummaryFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		conceptualSummaryFrame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent we)
			{
				if( JOptionPane.showConfirmDialog(conceptualSummaryFrame, "Are you sure?") == JOptionPane.YES_OPTION )
				{
					conceptualSummaryFrame.dispose();
					System.exit(0);
				}
			}
		});

		guiPanel = new JPanel();
		headerLabel = new JLabel("Conceptual Summary");
		titleLabel = new JLabel("Title :");
		paragraphLabel = new JLabel("Paragraph :");
		summaryLabel = new JLabel("Summary :");

		titleField = new JTextField();
		paragraphArea = new JTextArea();
		summaryArea = new JTextArea();
		clearButton = new JButton("Clear");
		resetButton = new JButton("Reset");
		addFileButton = new JButton("Open file");
		summarizeButton = new JButton("Generate Summary");
		saveFileButton = new JButton("Save as file");


		guiPanel.setBounds(0,0,700,700);
		headerLabel.setBounds(130,2,500,50);
		titleLabel.setBounds(15,60,150,25);
		paragraphLabel.setBounds(15,135,150,25);
		summaryLabel.setBounds(15,415,100,25);

		titleField.setBounds(10,85,680,50);
		paragraphArea.setBounds(10,160,680,220);
		summaryArea.setBounds(10,440,680,220);

		clearButton.setBounds(120,385,100,25);
		addFileButton.setBounds(10,385,100,25);
		summarizeButton.setBounds(520,385,170,25);

		resetButton.setBounds(10,665,100,25);		
		saveFileButton.setBounds(570,665,120,25);



		headerLabel.setForeground(Color.WHITE);
		titleLabel.setForeground(Color.WHITE);
		paragraphLabel.setForeground(Color.WHITE);
		summaryLabel.setForeground(Color.WHITE);

		guiPanel.setBackground(Color.RED/*.darker().darker()*/.brighter().brighter());

		headerLabel.setFont(new Font("Serif",Font.BOLD,34));
		titleLabel.setFont(new Font("Serif",Font.BOLD,12));
		paragraphLabel.setFont(new Font("Serif",Font.BOLD,12));
		summaryLabel.setFont(new Font("Serif",Font.BOLD,12));
		titleField.setFont(new Font("Serif",Font.BOLD,22));
		clearButton.setFont(new Font("Serif",Font.BOLD,12));
		resetButton.setFont(new Font("Serif",Font.BOLD,12));
		addFileButton.setFont(new Font("Serif",Font.BOLD,12));
		summarizeButton.setFont(new Font("Serif",Font.BOLD,12));
		saveFileButton.setFont(new Font("Serif",Font.BOLD,12));

		titleField.setHorizontalAlignment(JTextField.CENTER);

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
						while( (line = bReader.readLine()) != null )
							paragraphArea.setText(paragraphArea.getText() + "\n" + line);

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

		resetButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				paragraphArea.setText("");
				summaryArea.setText("");
				titleField.setText("");
			}
		});

		summarizeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				ConceptualSummarizer summarizer = new ConceptualSummarizer(paragraphArea.getText(), titleField.getText());
				List<String> output = summarizer.getSummary();
				summaryArea.setText("");
				for( String line : output )
					summaryArea.setText(summaryArea.getText() + "\n" + line);
				summaryArea.setText(summaryArea.getText().trim());
			}
		});

		saveFileButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
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
						JOptionPane.showMessageDialog(conceptualSummaryFrame, "Summary saved as " + file.getName() + "!");
					}
					catch(IOException ioe)
					{
						ioe.printStackTrace();
					}
				}
			}
		});


		conceptualSummaryFrame.add(headerLabel);
		conceptualSummaryFrame.add(titleLabel);
		conceptualSummaryFrame.add(paragraphLabel);
		conceptualSummaryFrame.add(summaryLabel);
		conceptualSummaryFrame.add(titleField);
		conceptualSummaryFrame.add(paragraphArea);
		conceptualSummaryFrame.add(summaryArea);
		conceptualSummaryFrame.add(clearButton);
		conceptualSummaryFrame.add(resetButton);
		conceptualSummaryFrame.add(addFileButton);
		conceptualSummaryFrame.add(summarizeButton);
		conceptualSummaryFrame.add(saveFileButton);


		summaryScrollPane = new JScrollPane(summaryArea);
		paragraphScrollPane = new JScrollPane(paragraphArea);

		summaryScrollPane.setBounds(10,440,680,220);
		paragraphScrollPane.setBounds(10,160,680,220);

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