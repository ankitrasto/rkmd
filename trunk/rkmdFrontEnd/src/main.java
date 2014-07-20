import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.TextArea;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.DropMode;
import javax.swing.JTextArea;
import javax.swing.JMenuBar;
import javax.swing.JScrollBar;
import java.awt.Scrollbar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import java.io.*;
import java.util.*;
import java.text.*;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class main {

	private JFrame frmRelativeKendrickMass;
	private JTextField txtRKMDTol;
	private JTextField txtNTCTol;
	private JTextArea txtMZInput;
	private JTextArea txtOutput;
	private final Action action = new SwingAction();
	private boolean fileInputSelected;
	private final Action action_1 = new SwingAction_1();
	private rkmdBackEnd engine;
	private JButton btnExport;
	private JComboBox<String> cbxFilter; 
	ArrayList<Double> mObsInput = new ArrayList<Double>();
	File fName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					main window = new main();
					window.frmRelativeKendrickMass.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public main() {
		initialize();
	}
	
	
	
	public void buttonPressed(ActionEvent e){
		String xy = txtRKMDTol.getText();
		JOptionPane.showMessageDialog(frmRelativeKendrickMass, xy);
	}
	
	public String checkInput(){
		String msg = "";
		
		//NTC Tolerance
		try{
			Double.parseDouble(this.txtNTCTol.getText());
			if(Double.parseDouble(this.txtNTCTol.getText()) <= 0){
				msg += "--NTC Tolerance Value must be greater than 0\n";
			}
		}catch(Exception e){
			msg += "--NTC Tolerance Value must be a floating point number\n";
		}
		
		//RKMD Tolerance
		try{
			Double.parseDouble(this.txtRKMDTol.getText());
			if(Double.parseDouble(this.txtRKMDTol.getText()) <= 0){
				msg += "--RKMD Tolerance Value must be greater than 0.0\n";
			}
		}catch(Exception e){
			msg += "--RKMD Tolerance value must be a floating point number\n";
		}
		
		//test MZ input/perform a conversion
		if(!this.fileInputSelected){
			if(this.txtMZInput.getText().length() <= 0){
				msg += "-- MZ Input Values are not Defined";
			}else{ //for efficiency, perform the conversion here.
				String[] dataHold = this.txtMZInput.getText().split("\n");
				boolean formatOK = true;
				for(int i = 0; i < dataHold.length; i++){
					try{
						this.mObsInput.add(Double.parseDouble(dataHold[i]));
					}catch(Exception e){formatOK = false;}
				}
				if(!formatOK){
					msg += "-- MZ Input Values are not correctly formatted. \n Floating point m/z values must be defined once per line with no trailing or line spaces.";
				}
				
				//testing only:
				for(int i = 0; i < mObsInput.size(); i++){
					System.out.println((Double)mObsInput.get(i));
				}
			}
		}
		
		return msg;
	}
	
	public String readMZInput(){
		//the M/Z Input is checked for conversion in the InputCheck method.
		return null;
	}
	
	public void readMZInputFromFile(){
		//file select dialog; this will be checked in rkmdBackEnd
		javax.swing.filechooser.FileNameExtensionFilter filter = new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv");		
		JFileChooser fcInput = new JFileChooser();
		fcInput.setFileFilter(filter);
		if(fcInput.showOpenDialog(frmRelativeKendrickMass) == JFileChooser.APPROVE_OPTION){
			fName = fcInput.getSelectedFile();
			this.txtMZInput.setText("M/Z File Input from" + fName.getAbsolutePath() + "\n\n NOTE: to use regular text input, go to File -> Clear All Input");
			this.txtMZInput.setEditable(false);
			this.fileInputSelected = true;
			return;
		}
		
		fileInputSelected = false;
	}
	
	public void calculate() throws Exception{
		//checkInput MUST be run before this!
		//may need to do in background.		
		if(!this.fileInputSelected){
			engine = new rkmdBackEnd("periodicMasses.csv", "ReferenceKMD.csv");
			engine.loadFileInput(this.mObsInput);
			engine.calculate(Double.parseDouble(this.txtRKMDTol.getText()), Double.parseDouble(this.txtNTCTol.getText()));
			this.txtOutput.setText(engine.printMatchResults());
			this.btnExport.setEnabled(true);
		}else{
			engine = new rkmdBackEnd(fName.getAbsolutePath(), "periodicMasses.csv", "ReferenceKMD.csv");
			engine.loadFileInput(null);
			engine.calculate(Double.parseDouble(this.txtRKMDTol.getText()), Double.parseDouble(this.txtNTCTol.getText()));
			this.txtOutput.setText(engine.printMatchResults());
			this.btnExport.setEnabled(true);
		}
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRelativeKendrickMass = new JFrame();
		frmRelativeKendrickMass.setTitle("Relative Kendrick Mass Defect Calculation Tool");
		frmRelativeKendrickMass.setBounds(100, 100, 727, 470);
		frmRelativeKendrickMass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRelativeKendrickMass.getContentPane().setLayout(null);
		
		JLabel lblLogoimg = new JLabel("");
		lblLogoimg.setBounds(12, 12, 300, 168);
		lblLogoimg.setIcon(new ImageIcon(main.class.getResource("/img/rkmd_title_2.png")));
		frmRelativeKendrickMass.getContentPane().add(lblLogoimg);
		
		txtRKMDTol = new JTextField();
		txtRKMDTol.setBounds(464, 61, 97, 19);
		frmRelativeKendrickMass.getContentPane().add(txtRKMDTol);
		txtRKMDTol.setColumns(10);
		
		txtNTCTol = new JTextField();
		txtNTCTol.setColumns(10);
		txtNTCTol.setBounds(464, 93, 97, 19);
		frmRelativeKendrickMass.getContentPane().add(txtNTCTol);
		
		JLabel lblNtcTolerance = new JLabel("RKMD Tolerance:");
		lblNtcTolerance.setBounds(329, 63, 121, 15);
		frmRelativeKendrickMass.getContentPane().add(lblNtcTolerance);
		
		JLabel label = new JLabel("NTC Tolerance:");
		label.setBounds(339, 95, 121, 15);
		frmRelativeKendrickMass.getContentPane().add(label);
		
		JScrollPane scxMZInput = new JScrollPane();
		scxMZInput.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scxMZInput.setBounds(25, 208, 324, 168);
		frmRelativeKendrickMass.getContentPane().add(scxMZInput);
		
		txtMZInput = new JTextArea();
		txtMZInput.setLineWrap(true);
		scxMZInput.setViewportView(txtMZInput);
		
		JScrollPane scxOutput = new JScrollPane();
		scxOutput.setBounds(387, 208, 324, 168);
		frmRelativeKendrickMass.getContentPane().add(scxOutput);
		
		txtOutput = new JTextArea();
		txtOutput.setEditable(false);
		scxOutput.setViewportView(txtOutput);
		
		btnExport = new JButton("Export Output");
		btnExport.setEnabled(false);
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					Date dNow = new Date();
					SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd_hhmmss");
					System.out.println(ft.format(dNow));
					engine.setFilter(cbxFilter.getSelectedIndex());
					if(cbxFilter.getSelectedIndex() == 1){
						engine.writeToFile("Results_Positive_" + ft.format(dNow) + ".txt", 2);
					}else if(cbxFilter.getSelectedIndex() == 2){
						engine.writeToFile("Results_Negative_" + ft.format(dNow) + ".txt", 2);
					}else{
						engine.writeToFile("Results" + ft.format(dNow) + ".txt", 2);
					}
					
					JOptionPane.showMessageDialog(frmRelativeKendrickMass, "Output File Written to:" + System.getProperty("user.dir"));
				}catch(Exception x){
					JOptionPane.showMessageDialog(frmRelativeKendrickMass, "I/O File Error", "Error", JOptionPane.ERROR_MESSAGE);
					x.printStackTrace();
				}
			}
		});
		btnExport.setBounds(558, 385, 153, 25);
		frmRelativeKendrickMass.getContentPane().add(btnExport);
		
		JLabel lblMzInput = new JLabel("M/Z Input:");
		lblMzInput.setBounds(22, 192, 144, 15);
		frmRelativeKendrickMass.getContentPane().add(lblMzInput);
		
		JLabel lblOutput = new JLabel("Output:");
		lblOutput.setBounds(387, 192, 144, 15);
		frmRelativeKendrickMass.getContentPane().add(lblOutput);
		
		JMenuBar menuBar = new JMenuBar();
		frmRelativeKendrickMass.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmInputFromFile = new JMenuItem("Input From File ...");
		mntmInputFromFile.setAction(action_1);
		mntmInputFromFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		mntmInputFromFile.setIcon(new ImageIcon(main.class.getResource("/javax/swing/plaf/metal/icons/ocean/hardDrive.gif")));
		mnFile.add(mntmInputFromFile);
		
		JMenuItem mntmClearAllInput = new JMenuItem("Clear All Input");
		mntmClearAllInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fileInputSelected = false;
				btnExport.setEnabled(false);
				txtRKMDTol.setText("");
				txtNTCTol.setText("");
				txtMZInput.setText(""); txtMZInput.setEditable(true); txtMZInput.setEnabled(true);
			}
		});
		mntmClearAllInput.setIcon(new ImageIcon(main.class.getResource("/com/sun/java/swing/plaf/windows/icons/File.gif")));
		mnFile.add(mntmClearAllInput);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mntmExit.setIcon(new ImageIcon(main.class.getResource("/javax/swing/plaf/metal/icons/ocean/close.gif")));
		mnFile.add(mntmExit);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About ...");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String aboutText = "RKMD: Relative Kendric Mass Defect Calculation Tool \n Developed by Ankit Rastogi \n rkmd.googlecode.com \n License: GPL v.3.0";
				JOptionPane.showMessageDialog(frmRelativeKendrickMass, aboutText, "ABOUT RKMD", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mntmAbout.setIcon(new ImageIcon(main.class.getResource("/javax/swing/plaf/metal/icons/ocean/info.png")));
		mnHelp.add(mntmAbout);
		
		JButton btnCalculate = new JButton("Screen/Calculate");
		btnCalculate.setAction(action);
		btnCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnCalculate.setBounds(387, 385, 153, 25);
		frmRelativeKendrickMass.getContentPane().add(btnCalculate);
		
		JLabel lblAdductFiler = new JLabel("Adduct Filter:");
		lblAdductFiler.setBounds(349, 123, 121, 15);
		frmRelativeKendrickMass.getContentPane().add(lblAdductFiler);
		
		cbxFilter = new JComboBox<String>();
		cbxFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//System.out.println("SelectIndex = " + cbxFilter.getSelectedIndex());
			}
		});
		cbxFilter.setModel(new DefaultComboBoxModel<String>(new String[] {"None", "Positive Mode", "Negative Mode"}));
		cbxFilter.setBounds(464, 118, 153, 24);
		frmRelativeKendrickMass.getContentPane().add(cbxFilter);
		
	}
	
	private class SwingAction extends AbstractAction { //calculate!
		public SwingAction() {
			putValue(NAME, "Screen/Calculate");
		}
		
		public void actionPerformed(ActionEvent e) {
			String errMsg = checkInput();
			if(!errMsg.equalsIgnoreCase("")){
				System.out.println("GUI Input Contains Errors");
				JOptionPane.showMessageDialog(frmRelativeKendrickMass, errMsg);
			}else{
				System.out.println("GUI Input OK ...");
				try{
					calculate();
				}catch(Exception x){
					x.printStackTrace();
					System.out.println("Error in Calculating ...");
				}
				
			}
		}
	}
	
	
	private class SwingAction_1 extends AbstractAction {
		public SwingAction_1() {
			putValue(NAME, "Input From File ...");
		}
		public void actionPerformed(ActionEvent e) {
			readMZInputFromFile();
		}
	}
}
