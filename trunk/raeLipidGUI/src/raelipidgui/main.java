/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raelipidgui;

import javax.swing.JOptionPane;
import java.util.*;
import java.io.*;
import javax.swing.JFileChooser;
import java.text.*;
import rkimp.*;

/**
 *
 * @author ankit
 */
public class main extends javax.swing.JFrame {

    /**
     * Creates new form main
     */
    public main() {
        initComponents();
    }
    
    class Task extends javax.swing.SwingWorker<Void, Void>{
        private Throwable theException = null;
        
        public Task(){
            super();
        }
        
        @Override
        public Void doInBackground() throws Exception{
             
            
            //resolve components and initialized back-end appropriately\
            pgStatus.setString("Do In Background Started ...");
            pgStatus.setValue(15);
            rkimp.rkmdBackEnd engine;
            
            //process input and calculate, write output to textbox
            
            pgStatus.setString("Processing Input ...");
            pgStatus.setValue(25);
            if((Integer)components.get(0) == 0){ //0 = using file input, NO MANUAL ENTRY
                engine = new rkimp.rkmdBackEnd((String)components.get(1), "periodicMasses.csv", "ReferenceKMD.csv");
                engine.loadFileInput(null);
                
                pgStatus.setString("Calculating ...");
                pgStatus.setValue(50);
                
                
                engine.calculate((Double)components.get(5), (Double)components.get(6), (Boolean)components.get(9));              
            }else{ //1 = using manual text entry formatted as a CSV file. 
               //temporarily generate a CSV file from the manual text field box
               System.out.println("Using Manual Entry");
               File tempCSV = new File("temp/manualTemp.csv");
               try{
                if(!tempCSV.getParentFile().exists()) tempCSV.getParentFile().mkdir();
                java.io.PrintWriter pW = new java.io.PrintWriter(new FileWriter(tempCSV, false));
                pW.print((String)components.get(2));
                pW.close();
                System.out.println("Temporary File Created at: " + tempCSV.getAbsoluteFile());
               }catch(Exception e){
                   JOptionPane.showMessageDialog(main.this, "Error Creating Temporary Files. Ensure that proper folder permissions are set or that there is enough free space.", "File IO Error", JOptionPane.ERROR_MESSAGE);
                   throw new Exception("TEMPORARY FILES IO ERROR");
               }
               
               engine = new rkimp.rkmdBackEnd(tempCSV.getAbsolutePath(), "periodicMasses.csv", "ReferenceKMD.csv");
               engine.loadFileInput(null);
               
               pgStatus.setString("Calculating ...");
               pgStatus.setValue(50);
               
               
               engine.calculate((Double)components.get(5), (Double)components.get(6), (Boolean)components.get(9));
            }
            
            
            pgStatus.setString("Filtering ...");
            pgStatus.setValue(75);
            //apply filtering, output to GUI
            int ionFilter = (Integer)(components.get(4));
            engine.setFilter(ionFilter);
            
            pgStatus.setString("Generating Output ...");
            pgStatus.setValue(85);
            
            txtOutputGUI.setText(""); txtOutputGUI.setText(engine.printMatchResults());
            
            //output to file (if specified)
            if((Boolean)(components.get(7)) == true){
                Date dNow = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd_hhmmss");
                String dest = (String)components.get(8);
                if(ionFilter == 1){
                    engine.writeToFile(dest + "/Results_Positive_" + ft.format(dNow) + ".txt", 2);
                }else if(ionFilter == 2){
                    engine.writeToFile(dest + "/Results_Negative_" + ft.format(dNow) + ".txt", 2);
                }else{
                    engine.writeToFile(dest + "/Results_NoFilter_" + ft.format(dNow) + ".txt", 2);
                }
            }
            
            pgStatus.setString("Done!");
            pgStatus.setValue(100);
            
            return null;
        }
        
        @Override
        public void done(){
            if(theException != null){
                theException.printStackTrace();
            }
            System.gc();
            main.this.btnCalculate.setEnabled(true);
            main.this.pgStatus.setString("Complete."); pgStatus.setValue(0);
        }
    }
    
    
    
    /**
     * Variables for Do-In-Background
     */
    
    public ArrayList components = new ArrayList();
    public ArrayList<Double> mObsAux;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtManualInput = new javax.swing.JTextArea();
        txtFileInputName = new javax.swing.JTextField();
        lblInputFileName = new javax.swing.JLabel();
        btnBrowse = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        cbxInputMethod = new javax.swing.JComboBox();
        btnClearManual = new javax.swing.JButton();
        lblManualInstruct = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cbxFilter = new javax.swing.JComboBox();
        txtRKMDTol = new javax.swing.JTextField();
        txtNTCTol = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        chxResultsToFile = new javax.swing.JCheckBox();
        btnBrowseOut = new javax.swing.JButton();
        txtOutputDest = new javax.swing.JTextField();
        chkRetFilter = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        btnCalculate = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtOutputGUI = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pgStatus = new javax.swing.JProgressBar();
        jLabel13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        jTabbedPane1.setBackground(new java.awt.Color(238, 238, 238));
        jTabbedPane1.setName(""); // NOI18N

        txtManualInput.setBackground(new java.awt.Color(254, 254, 254));
        txtManualInput.setColumns(20);
        txtManualInput.setRows(5);
        txtManualInput.setText("<use this area to enter data manually>");
        txtManualInput.setEnabled(false);
        jScrollPane1.setViewportView(txtManualInput);

        txtFileInputName.setBackground(new java.awt.Color(254, 254, 254));
        txtFileInputName.setText("<specify full path of filename>");

        lblInputFileName.setText("Filename:");

        btnBrowse.setText("Browse...");
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });

        jLabel6.setText("Input m/z Data:");

        cbxInputMethod.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "From File ", "Manually" }));
        cbxInputMethod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxInputMethodActionPerformed(evt);
            }
        });

        btnClearManual.setText("Clear Manual Input");
        btnClearManual.setEnabled(false);
        btnClearManual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearManualActionPerformed(evt);
            }
        });

        lblManualInstruct.setText("<html> Enter \"Mass\" followed by one m/z value per line in the field below. <br> For multiple columns, use CSV formatting. See manual for help</html>");
        lblManualInstruct.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnClearManual))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(lblInputFileName)
                                .addGap(3, 3, 3)
                                .addComponent(txtFileInputName, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBrowse))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(3, 3, 3)
                                .addComponent(cbxInputMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblManualInstruct, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 66, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cbxInputMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFileInputName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblInputFileName, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBrowse))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblManualInstruct, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClearManual)
                .addGap(46, 46, 46))
        );

        jTabbedPane1.addTab("(1) Input Data", jPanel2);

        jLabel7.setText("Adduct Filtering:");

        jLabel8.setText("RKMD Tolerance:");

        jLabel9.setText("NTC Tolerance:");

        cbxFilter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No Filtering", "Positive Mode", "Negative Mode" }));

        txtRKMDTol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRKMDTolActionPerformed(evt);
            }
        });

        jLabel11.setText("(Floating point number between 0 and 1)");

        jLabel12.setText("(Floating point number between 0 and 1)");

        chxResultsToFile.setText("Results to File:");
        chxResultsToFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chxResultsToFileActionPerformed(evt);
            }
        });

        btnBrowseOut.setText("Browse...");
        btnBrowseOut.setEnabled(false);
        btnBrowseOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseOutActionPerformed(evt);
            }
        });

        txtOutputDest.setText("<select destination folder>");
        txtOutputDest.setEnabled(false);
        txtOutputDest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOutputDestActionPerformed(evt);
            }
        });

        chkRetFilter.setText("Retention Time Filtering");
        chkRetFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkRetFilterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel7)
                                    .addGap(16, 16, 16))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addGap(18, 18, 18)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(26, 26, 26)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbxFilter, 0, 158, Short.MAX_VALUE)
                            .addComponent(txtRKMDTol)
                            .addComponent(txtNTCTol))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(chxResultsToFile, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtOutputDest, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBrowseOut))
                    .addComponent(chkRetFilter))
                .addContainerGap(77, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cbxFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtRKMDTol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(24, 24, 24)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtNTCTol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBrowseOut)
                    .addComponent(txtOutputDest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chxResultsToFile, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkRetFilter)
                .addContainerGap(141, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("(2) Calculation Parameters", jPanel3);

        btnCalculate.setText("Calculate");
        btnCalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculateActionPerformed(evt);
            }
        });

        txtOutputGUI.setEditable(false);
        txtOutputGUI.setColumns(20);
        txtOutputGUI.setRows(5);
        jScrollPane2.setViewportView(txtOutputGUI);

        jLabel10.setText("Calculation Results:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(34, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE)
                        .addComponent(btnCalculate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(36, 36, 36))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCalculate)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("(3) Calculate", jPanel4);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logo_5.png"))); // NOI18N

        jLabel3.setForeground(new java.awt.Color(255, 255, 0));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/gpl-v3-logo.png"))); // NOI18N
        jLabel3.setText("<html>  Front-End Version: 0.9 (beta) <br> Back-End Version: RKMD revision 14 <br> http://rkmd.googlecode.com <br> License: GPL v3 <br> </html>");

        jLabel4.setForeground(new java.awt.Color(255, 255, 0));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("<html>\nrae-LipID is a graphical front-end for the <br>\nconsole-based RKMD program, which <br>\ncalculates the Reference Kendrick Mass <br> \nDefect and the total number of carbon <br>\natoms (NCT) from a raw, experimental <br>\narray of observed m/z values in order to <br>\nidentify m/z values which may represent <br>\ncompounds of a known lipid species. <br>\nInput consists of the raw array of m/z <br>\nions as well as literature-derived RKMDs <br>\nand NCTs through which to iterate and <br>\nmatch with experimental data. \n</html>");
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("About", jPanel1);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logo_5_banner.png"))); // NOI18N

        pgStatus.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel13.setText("Status");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addGap(0, 5, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 654, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pgStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pgStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private String checkManualFormatting(){
        /*
        Formatting specifications: should be formatted as a CSV-file!!
        Silmultaneously Collect Mass Data As Well.
        */
        
        String auxMsg = "";
        
        mObsAux = new ArrayList<Double>();
        
        String[] lines = this.txtManualInput.getText().split("\n");
        
        //first line as a header; must have a "mass" heading
        String headerLine[] = lines[0].split(",");
        int MassIndex = -1;
        int TotalColumns = headerLine.length;
        for(int j = 0; j < headerLine.length; j++){
            if(headerLine[j].equalsIgnoreCase("Mass")){
                MassIndex = j;
            }
        }
        if(MassIndex == -1){
            auxMsg += "--No \"Mass\" Column in Manual Entry Field\n";
            return auxMsg;
        }
        
        //check to make sure that (1) masses are numerical and (2) each line has the same columns
        boolean colEqual = true;
        
        for(int i = 1; i < lines.length; i++){
           
         //column length check, must be exhaustive
           if(TotalColumns != lines[i].split(",").length){
               colEqual = false;
               auxMsg += "--Improperly Formatted Manual Entry Field: Columns Inconsistent\n";
               return auxMsg;
           }
           
           //numerical conversion
           try{
               mObsAux.add(Double.parseDouble(lines[i].split(",")[MassIndex]));
           }catch(Exception e){
               auxMsg += "--The mass column of the manual entry field does not contain numerical data\n";
               return auxMsg;
           }
        }
        
        return "";
    }
    
    private boolean checkInput(){
        
        String msg = "";
        
        //Input MZ Tab
        
        if(this.cbxInputMethod.getSelectedIndex() == 0){ //file existence check
            try{
              File auxFInput = new File(this.txtFileInputName.getText());
              if(!auxFInput.exists()) msg += "--Input File Does not Exist\n";
              if(auxFInput.exists() && !auxFInput.isFile()) msg += "-- Input m/z File is not a file\n";
            }catch(Exception e){
                msg += "-- Error detecting Input File.\n";
            }
        }
        
        if(this.cbxInputMethod.getSelectedIndex() == 1){
            if(this.txtManualInput.getText().equalsIgnoreCase("") || this.txtManualInput.getText().equalsIgnoreCase("<use this area to enter data manually>")) msg += "--Manual m/z Entries are Empty.\n";
            msg += this.checkManualFormatting();
        }
        
        //Calculation Parameters Tab
        
        //RKMD
        try{
            double rkmdTolTemp = Double.parseDouble(this.txtRKMDTol.getText());
            if(rkmdTolTemp <= 0){
                msg += "--RKMD Tolerance must be greater than 0\n";
            }
        }catch(Exception e){
            msg += "--RKMD Tolerance Value must be a floating point number\n";
        }
        
        //NTC
        try{
             double NTCTolTemp = Double.parseDouble(this.txtNTCTol.getText());
             if(NTCTolTemp <= 0){
                 msg += "--NTC Tolerance must be greater than 0\n";
             }
         }catch(Exception e){
             msg += "--NTC Tolerance Value must be a floating point number\n";
         }
        
        //Output Directory Check
        if(this.chxResultsToFile.isSelected()){
            try{
                File x = new File(this.txtOutputDest.getText());
                if(!x.exists()) msg += "--Output File Destination Does not Exist. This must be a directory\n";
                if(x.exists() && !x.isDirectory()) msg += "--Output File Destination is Not a Directory\n";
            }catch(Exception e){
                msg += "--Output File Destination does not exist. This must be a folder\n";
            }
        }
        
        if(!msg.equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, msg, "Form Input Errors", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    
    private void btnCalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculateActionPerformed
        if(this.checkInput()){
            
            //RESET components arraylist first
            
            components = new ArrayList();
            
            //collect GUI components, proceed to calculation - do in background?
            
            //load GUI components
            components.add(this.cbxInputMethod.getSelectedIndex()); //0, int
            components.add(this.txtFileInputName.getText()); //1, String
            components.add(this.txtManualInput.getText()); //2, String - save as temp CSV file in working directory
            components.add(this.mObsAux); //3, ArrayList
            components.add(this.cbxFilter.getSelectedIndex()); //4, int
            components.add(Double.parseDouble(this.txtRKMDTol.getText())); //5, Double
            components.add(Double.parseDouble(this.txtNTCTol.getText())); //6, Double
            components.add(this.chxResultsToFile.isSelected()); //7, Boolean
            components.add(this.txtOutputDest.getText()); //8, String
            components.add(this.chkRetFilter.isSelected()); //9, Boolean
            
            //execute!
            this.pgStatus.setStringPainted(true);
            Task calcJob;
            this.btnCalculate.setEnabled(false);
            try{
               calcJob = new Task();
               calcJob.execute();
            }catch(Exception e){} 
            
            
        }
    }//GEN-LAST:event_btnCalculateActionPerformed

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
        javax.swing.JFileChooser fcInput = new javax.swing.JFileChooser();
        javax.swing.filechooser.FileNameExtensionFilter csvFilter = new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv");
        fcInput.setFileFilter(csvFilter);
        if(fcInput.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            this.txtFileInputName.setText(fcInput.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_btnBrowseActionPerformed

    private void txtRKMDTolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRKMDTolActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRKMDTolActionPerformed

    private void btnBrowseOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseOutActionPerformed
        javax.swing.JFileChooser fcOutput = new javax.swing.JFileChooser();
        fcOutput.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if(fcOutput.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            this.txtOutputDest.setText(fcOutput.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_btnBrowseOutActionPerformed

    private void txtOutputDestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOutputDestActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOutputDestActionPerformed

    private void chxResultsToFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chxResultsToFileActionPerformed
            if(this.chxResultsToFile.isSelected()){
                this.btnBrowseOut.setEnabled(true);
                this.txtOutputDest.setEnabled(true);
            }
            
            if(this.chxResultsToFile.isSelected() == false){
                this.btnBrowseOut.setEnabled(false);
                //this.txtOutputDest.setText("<Select Destination Folder>");
                this.txtOutputDest.setEnabled(false);
            }
            
    }//GEN-LAST:event_chxResultsToFileActionPerformed

    private void cbxInputMethodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxInputMethodActionPerformed
        if(cbxInputMethod.getSelectedIndex() == 1){ //manual entry
            this.txtFileInputName.setEnabled(false);
            this.txtManualInput.setEnabled(true);
            //this.txtFileInputName.setText("<specify full path of filename>");
            this.btnBrowse.setEnabled(false);
            this.lblInputFileName.setEnabled(false);
            this.btnClearManual.setEnabled(true);
            this.lblManualInstruct.setEnabled(true);
        }
        
        if(cbxInputMethod.getSelectedIndex() == 0){ //file input
            this.txtFileInputName.setEnabled(true);
            this.txtManualInput.setEnabled(false);
            //this.txtManualInput.setText("<use this area to enter data manually>");
            //this.txtFileInputName.setText("<specify full path of filename>");
            this.btnBrowse.setEnabled(true);
            this.lblInputFileName.setEnabled(true);
            this.btnClearManual.setEnabled(false);
            this.lblManualInstruct.setEnabled(false);
        }
    }//GEN-LAST:event_cbxInputMethodActionPerformed

    private void btnClearManualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearManualActionPerformed
        this.txtManualInput.setText("");
    }//GEN-LAST:event_btnClearManualActionPerformed

    private void chkRetFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRetFilterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkRetFilterActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnBrowseOut;
    private javax.swing.JButton btnCalculate;
    private javax.swing.JButton btnClearManual;
    private javax.swing.JComboBox cbxFilter;
    private javax.swing.JComboBox cbxInputMethod;
    private javax.swing.JCheckBox chkRetFilter;
    private javax.swing.JCheckBox chxResultsToFile;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblInputFileName;
    private javax.swing.JLabel lblManualInstruct;
    public javax.swing.JProgressBar pgStatus;
    private javax.swing.JTextField txtFileInputName;
    private javax.swing.JTextArea txtManualInput;
    private javax.swing.JTextField txtNTCTol;
    private javax.swing.JTextField txtOutputDest;
    private javax.swing.JTextArea txtOutputGUI;
    private javax.swing.JTextField txtRKMDTol;
    // End of variables declaration//GEN-END:variables
}
