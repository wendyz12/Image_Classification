package org.deeplearning4j;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * User Interface by swing
 */

public class UserInterface implements Runnable {
	String inputPath = null;
	String outputImagePath = null;
	ImageIcon imageDisplay;
	JLabel labelName;
	JLabel labelIntro;
	JLabel picLabel;
	Classifier model;
	String[] labels;
	boolean imageReady = false;

	JTable j;
	String[][] data;
	JButton button1;

	public UserInterface() throws FileNotFoundException {
		// load labels
		File labelFile = new File("labels.json");
		FileReader r = new FileReader(labelFile);
		Scanner s = new Scanner(r);
		labels = s.nextLine().split(",");
	}

	@Override
	/**
	 * Create frame, panel and buttons run()
	 */
	public void run() {
		JFrame frame = new JFrame("Dog Breed Classification");
		frame.setBackground(Color.CYAN);

		frame.setResizable(false);
		frame.setSize(1000, 850);
		JPanel LeftPanel = new JPanel(new BorderLayout());
		JPanel RightPanel = new JPanel(new BorderLayout());
		JPanel LeftMidPanel = new JPanel(new GridLayout(4, 1));
		JPanel LeftMidPanel2 = new JPanel();
		JPanel LeftMidPanel3 = new JPanel();
		JPanel LeftMidPanel4 = new JPanel();
		JPanel RightMidPanel = new JPanel(new GridLayout(4, 1));
		JPanel RightMidPanel2 = new JPanel();
		JPanel RightMidPanel3 = new JPanel();
		JPanel RightMidPanel4 = new JPanel();
		JPanel ButtonLayout = new JPanel(new FlowLayout());
		button1 = new JButton("Choose a picture");
		JButton button2 = new JButton("Loading models");
		LeftMidPanel3.add(ButtonLayout);
		ButtonLayout.add(button1);
		ButtonLayout.add(button2);
		button1.setBounds(10, 10, 100, 60);
		button2.setEnabled(false);
		LeftPanel.add(LeftMidPanel, BorderLayout.CENTER);
		RightPanel.add(RightMidPanel, BorderLayout.CENTER);
		JLabel titlelabel = new JLabel("Which type of dog am I?", SwingConstants.CENTER);
		titlelabel.setFont(new Font("Serif", Font.BOLD, 26));
		LeftMidPanel.add(titlelabel);
		LeftMidPanel.add(LeftMidPanel2);
		LeftMidPanel.add(LeftMidPanel3);
		frame.setLayout(new GridLayout(1, 2));
		frame.add(LeftPanel);
		frame.add(RightPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		button1.addActionListener(new ActionListener() {
			/**
			 * action event for button 1
			 */
			public void actionPerformed(ActionEvent e) {

				System.out.println("button1 pressed");
				JFileChooser j = new JFileChooser();
				if (j.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
					return;
				}
				inputPath = j.getSelectedFile().getAbsolutePath();
				System.out.println(j.getSelectedFile().getAbsolutePath());
				ImageHandler ih = new ImageHandler();
				String[] InputImagePathArray = inputPath.split("/");
				outputImagePath = InputImagePathArray[InputImagePathArray.length - 1];
				System.out.println(outputImagePath);
				BufferedImage inputImage = ih.read(inputPath, outputImagePath);
				ImageIcon imageDisplay = new ImageIcon(outputImagePath);
				JLabel imageLabel = new JLabel(imageDisplay);
				RightMidPanel.removeAll();
				RightMidPanel.updateUI();
				RightMidPanel2.removeAll();
				RightMidPanel2.updateUI();
				RightMidPanel3.removeAll();
				RightMidPanel3.updateUI();
				// LeftMidPanel.removeAll();
				LeftMidPanel2.removeAll();
				LeftMidPanel2.updateUI();
				LeftMidPanel2.add(imageLabel, BorderLayout.CENTER);
				// button1.setEnabled(false);
				frame.setVisible(true);
				imageReady = true;
			}
		});
		button2.addActionListener(new ActionListener() {
			/**
			 * action event for button 2
			 */
			public void actionPerformed(ActionEvent e) {
				System.out.println("button2 pressed");
				// JLabel labelName;
				// JLabel labelIntro;
				try {
					if (!imageReady) {
						JOptionPane.showMessageDialog(null, "please load image", "", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					JOptionPane.showMessageDialog(null, "Magic in preparaion", "", JOptionPane.INFORMATION_MESSAGE);
					int labelId = model.classify(outputImagePath);
					String breedName = labelIDToName(labelId);

					// Data to be displayed in the JTable
					data = model.probabilityCalc();
					// Column Names
					String[] columnNames = { "Rank", "Our Top 5 Prediction Results" };
					int rank = 1;
					for (String[] iter : data) {
						double idx = Double.parseDouble(iter[0]);
						iter[1] = labelIDToName((int) idx);
						String strRank = Integer.toString(rank);
						iter[0] = strRank;
						rank++;
					}

					/**
					 * Initializing the JTable
					 */
					j = new JTable(data, columnNames) {
						// Determines if data can be entered by users
						public boolean isCellEditable(int data, int columns) {
							return false;
						}

						// Creates cells for the table
						public Component prepareRenderer(TableCellRenderer r, int data, int columns) {
							Component c = super.prepareRenderer(r, data, columns);
							// Every even numbers
							if (data % 2 == 0)
								c.setBackground(Color.WHITE);
							else
								c.setBackground(Color.LIGHT_GRAY);
							if (isCellSelected(data, columns))
								c.setBackground(Color.CYAN);
							return c;
						}
					};

					j.setPreferredScrollableViewportSize(new Dimension(400,80));

					TableColumnModel columnModel = j.getColumnModel();
					columnModel.getColumn(0).setPreferredWidth(90);
					columnModel.getColumn(1).setPreferredWidth(310);

					JTableHeader header = j.getTableHeader();
					header.setBackground(Color.black);
					header.setForeground(Color.yellow);
					// Table header font
					j.getTableHeader().setFont(new Font("Serif", 1, 22));
					j.setBounds(30, 40, 200, 50);

					// adding it to JScrollPane
					JScrollPane sp = new JScrollPane(j);
					RightMidPanel4.removeAll();
					RightMidPanel4.add(sp);

					WikiScraper introduction = new WikiScraper();
					introduction.scrapeTopicWiki("/wiki/" + nameToURL(breedName));
					String outputText = introduction.contentText_formatted.toString();
					JTextArea dogIntro = new JTextArea(outputText);
					JLabel picLabel = new JLabel(new ImageIcon(introduction.resizedImage));
					JLabel breedNameLabel = new JLabel(breedName, SwingConstants.CENTER);
					breedNameLabel.setFont(new Font("Serif", Font.BOLD, 26));
					RightMidPanel.removeAll();
					RightMidPanel2.removeAll();
					RightMidPanel3.removeAll();
					RightMidPanel.add(breedNameLabel);
					RightMidPanel2.add(picLabel);
					RightMidPanel3.add(dogIntro);
					RightMidPanel.add(RightMidPanel2);
					RightMidPanel.add(RightMidPanel3);
					RightMidPanel.add(RightMidPanel4);
					frame.setVisible(true);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

			}

		});

		// Since loading model can take a while (especially for the first time, as DL4J
		// needs to download the 500MB vgg16 network parameters), we want to do it in
		// the
		// background while the user is selecting images.
		SwingWorker worker = new SwingWorker<String, Void>() {
			@Override
			public String doInBackground() throws IOException, ClassNotFoundException {
				model = new Classifier();
				return "AllDone"; // doesn't matter
			}

			@Override
			protected void done() {
				try {
					button2.setText("Predict");
					button2.setEnabled(true);
				} catch (Exception ignore) {
				}
			}
		};
		worker.execute();
	}

	/**
	 * Convert labelID obtained from Classifier class into a string for fetching the breed name
	 * @param labelId
	 * @return
	 */

	public String labelIDToName(int labelId) {
		String[] breedNameArray = labels[labelId].split("\\.")[1].split("\"")[0].split("_");
		String breedName = "";
		for (String i : breedNameArray) {
			String firstLetter = i.substring(0, 1).toUpperCase();
			String capitalizedname = firstLetter + i.substring(1);
			breedName = breedName + capitalizedname;
			breedName = breedName + " ";
		}
		breedName = breedName.substring(0, breedName.length() - 1);
		return breedName;
	}

	/**
	 * Convert the breed name into a string which is recognizable by WikiScraper class
	 * @param name
	 * @return
	 */

	public String nameToURL(String name) {
		return name.replaceAll(" ", "_");
	}

	/**
	 * Main function
	 *
	 * @param args
	 */
	public static void main(String[] args) throws FileNotFoundException {
		SwingUtilities.invokeLater(new UserInterface());
	}
}
