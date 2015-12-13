/** rkmdBackEnd.java
 *  Main handler for back-end operations of RKMD; can be run
 * 	alone in console based systems if file inputs or hard-coded
 * 	array inputs (testing) are used
 * 
 * Ankit Rastogi
 * LICENSE: GPL v.3.0.
 * Started: Dec 19, 2013
 * */

package rkimp;
 
import java.io.*;
import java.util.*;

class oxFactor{
	int nh;
	int no;
	double DOX;
	String descriptor;
	
	oxFactor(int nhAux, int noAux, double DOXAux, String descriptorAux){
		nh = nhAux;
		no = noAux;
		DOX = DOXAux;
		descriptor = descriptorAux;
	}
	
	
	String printDesc(){
		return descriptor;
	}
	
	double correctionFactor(double mH, double mO, double mC){
		//System.out.println("OXCall (" + nh + "," + no + ") = " + (nh*mH + no*mO)/(mC+2*mH));
		return (nh*mH + no*mO)/(2*mH + mC);
	}
	
	double correctionFactorNum(double mH, double mO){
		return (nh*mH + no*mO);
	}
	
	double correctionFactorKMD(double kmdOriginalAux, double c3Aux, double c4Aux){
		return kmdOriginalAux + no*c3Aux + nh*c4Aux;
	}
	
	boolean checkDOX(double rkmdAux){
		if(DOX == -1) return true;
		else return (rkmdAux >= DOX);
	}
}

public class rkmdBackEnd{
	
	//input arrays + output array
	private ArrayList<Double> inputMObs;
	public ArrayList<String> inputMObsHold; 
	private Pair[] inputMZRef; //if CLI/file input is used, initially load results into ArrayList. 
	private Compound[] inputRefSpecies;
	private String[] matchResults; //should match number of lines in input/length of inputMObs
	private oxFactor oxTable[]; //oxidation table
	
	//file IOs:
	private File fInputMObs; //raw observational data
	private File fInputMZRef; // reference IUPAC M/Z's
	private File fInputRefSpecies; // reference KMDs/NChains
	private File fOutputMatchResults;
	private boolean checkFailure = false;
	
	
	// statistical tolerances for metrics
	public double rkmdTolerance;
	public double NTCTolerance;
	private int filterSelect = 0;
	
	public rkmdBackEnd(String fNameInputMObs, String fNameInputMZ, String fNameInputSpecies) throws Exception{
		//for testing/running headless
		//filename integrity check:
		
		int fileMarker = 0;
		
		try{
			this.fInputMObs = new File(fNameInputMObs); fileMarker++;
			this.fInputMZRef = new File(fNameInputMZ); fileMarker++;
			this.fInputRefSpecies = new File(fNameInputSpecies); fileMarker++;
			
		}catch(Exception e){
			System.out.println("VERBOSE: IO Error at fileMarker= " + fileMarker);
			checkFailure = true;
		}
		
		if(!this.fInputMObs.isFile() || !this.fInputMObs.exists()){
			System.out.println("-- VERBOSE: Input Observational File Does not Exist or is Not a File");
			checkFailure = true;
		}
		
		if(!this.fInputMZRef.isFile() || !this.fInputMZRef.exists()){
			System.out.println("-- VERBOSE: Input Atomic Masses File Does Not Exist or is Not a File");
			checkFailure = true;
		}
		
		if(!this.fInputRefSpecies.isFile() || !this.fInputRefSpecies.exists()){
			System.out.println("-- VERBOSE: Input Reference KMD File Does Not Exist or is Not a File");
			checkFailure = true;
		}
		
		if(checkFailure){
			throw new Exception("File Check Failure");
		}
	}
	
	public rkmdBackEnd(String fNameInputMZ, String fNameInputSpecies) throws Exception{
		//for testing/running with GUI - no fNameInputMObs required!!
		//filename integrity check:
		
		int fileMarker = 0;
		
		try{
			this.fInputMZRef = new File(fNameInputMZ); fileMarker++;
			this.fInputRefSpecies = new File(fNameInputSpecies); fileMarker++;
		}catch(Exception e){
			System.out.println("VERBOSE: IO Error at fileMarker= " + fileMarker);
			checkFailure = true;
		}
		
		if(!this.fInputMZRef.isFile() || !this.fInputMZRef.exists()){
			System.out.println("-- VERBOSE: Input Atomic Masses File Does Not Exist or is Not a File");
			checkFailure = true;
		}
		
		if(!this.fInputRefSpecies.isFile() || !this.fInputRefSpecies.exists()){
			System.out.println("-- VERBOSE: Input Reference KMD File Does Not Exist or is Not a File");
			checkFailure = true;
		}
		
		if(checkFailure){
			throw new Exception("File Check Failure");
		}
	}
	
	
	// REQUIRES: file formatted correctly
	private ArrayList<String> fileContents(File auxInputFile) throws Exception{
		ArrayList<String> input = new ArrayList<String>(); 
		String dataHold;
		BufferedReader bR = new BufferedReader(new FileReader(auxInputFile));
		dataHold = bR.readLine();
		while(dataHold != null){
			input.add((dataHold));
			dataHold = bR.readLine();
		}
		bR.close();
		return input;
	}
	
	
	public ArrayList<String> textContents(String auxInput) throws Exception{
		ArrayList<String> input = new ArrayList<String>(); 
		String[] dataHold = auxInput.split("\n");
		for(int i = 0; i < dataHold.length; i++){
			input.add(this.delimitLine(dataHold[i]));
		}
		return input;
	}
	
	
	private int colIndex(String auxKey, String[] auxHeader){
		for(int i = 0; i < auxHeader.length; i++){
			if(auxHeader[i].equalsIgnoreCase(auxKey)) return i;
		}
		
		return -1; //<0 if not found
	}
		
	
	public void loadFileInput(ArrayList<Double> auxMObs) throws Exception{
		String headerLine[];
		String tempHold[];
		
		//(1) load reference m/z
		
		// a) check input table should have 3 columns (UNIQUE SYMBOLS, mass):
		ArrayList<String> inputMZRefHold = this.fileContents(this.fInputMZRef);
		if(inputMZRefHold.size() <= 0){ //MZ file is empty or contains no data
			throw new Exception("No Input Data for Reference Atomic Masses File");
		}
		
		headerLine = this.delimitLine((String)inputMZRefHold.get(0)).split("\t");
			if(this.colIndex("Name", headerLine) < 0 || this.colIndex("Symbol", headerLine) < 0 || this.colIndex("Mass", headerLine) < 0){
			throw new Exception("Input Reference Atomic Masses Data Incorrectly Formatted: Check to ensure that data contains 3 properly named headers");
		}
		
		inputMZRef = new Pair[inputMZRefHold.size()-1];
		
		System.out.print("Loading Atomic Masses...");
		for(int i = 1; i < inputMZRefHold.size(); i++){ //start at the 2nd line (1st must be header)
			try{
				tempHold = this.delimitLine((String)inputMZRefHold.get(i)).split("\t");
				String tempName = tempHold[this.colIndex("Symbol", headerLine)];
				double tempMassValue = Double.parseDouble(tempHold[this.colIndex("Mass", headerLine)]);
				inputMZRef[i-1] = new Pair(tempName, tempMassValue); 
			}catch(Exception e){
				System.out.println("Error Adding Masses: Check to ensure that the input file is properly formatted/contains appropriate numeric data for masses.");
				e.printStackTrace();
				checkFailure = true;
			}
		}
		
		System.out.println("... Added: " + inputMZRef.length + " reference masses");
		
		//testing only:
		/*for(int i = 0; i < inputMZRef.length; i++){
			System.out.println(inputMZRef[i].getName() + "\t" + inputMZRef[i].getValue());
		}*/
		
		
		//load reference KMD:
		ArrayList<String> inputRefSpeciesHold = this.fileContents(this.fInputRefSpecies);
		if(inputRefSpeciesHold.size() <= 0){
			throw new Exception("No Input Data for Reference KMD File");
		}
		
		headerLine = this.delimitLine((String)inputRefSpeciesHold.get(0)).split("\t"); 
		
		if(this.colIndex("Class", headerLine) < 0 || this.colIndex("Subclass", headerLine) < 0 || this.colIndex("Adduct", headerLine) < 0 || this.colIndex("KMD", headerLine) < 0 || this.colIndex("Number_of_Chains", headerLine) < 0 
		|| this.colIndex("Headgroup_Mass", headerLine) < 0 || this.colIndex("MinC", headerLine) < 0 || this.colIndex("MaxC", headerLine) < 0 || this.colIndex("MaxDB", headerLine) < 0 || this.colIndex("FA_type", headerLine) < 0){
			throw new Exception("Input Reference KMD Data Incorrectly Formatted: Check to ensure that data contains properly named headers");
		}
		
		inputRefSpecies = new Compound[inputRefSpeciesHold.size()-1];
		
		System.out.print("Loading Literature KMD Values ...");
		
		for(int i = 1; i < inputRefSpeciesHold.size(); i++){
			try{
				tempHold = this.delimitLine((String)inputRefSpeciesHold.get(i)).split("\t");
				int NChainsTemp = Integer.parseInt(tempHold[this.colIndex("Number_of_Chains", headerLine)]);
				double RefKMDTemp = Double.parseDouble(tempHold[this.colIndex("KMD", headerLine)]);
				String fullNameTemp = tempHold[this.colIndex("Class", headerLine)] + ";" + tempHold[this.colIndex("Subclass", headerLine)] + ";" + tempHold[this.colIndex("Adduct", headerLine)];
				int minCTemp = Integer.parseInt(tempHold[this.colIndex("minC", headerLine)]);
				int maxCTemp = Integer.parseInt(tempHold[this.colIndex("maxC", headerLine)]);
				double hmTemp = Double.parseDouble(tempHold[this.colIndex("Headgroup_Mass", headerLine)]);
				int maxDBTemp = Integer.parseInt(tempHold[this.colIndex("MaxDB", headerLine)]);
				String strFAtype = tempHold[this.colIndex("FA_type",headerLine)];
				int auxFAtype = 0; 
				
				if(strFAtype.equalsIgnoreCase("carbonyl")){
					auxFAtype = 0;
					System.out.println("Lipid Load, Using Carbonyl Type");
				}else if(strFAtype.equalsIgnoreCase("aliphatic")){
					System.out.println("Lipid Load, Using Aliphatic Type");
					auxFAtype = -1;
				}else if(strFAtype.equalsIgnoreCase("plasmalogen")){
					System.out.println("Lipid Load, Using Plasmalogen Type");
					auxFAtype = 1;
				}else{
					try{
						auxFAtype = Integer.parseInt(strFAtype);
						System.out.println("Custom Lipid Load, NCC = " + auxFAtype);
					}catch(Exception e){
						auxFAtype = 0;
						System.out.println("Lipid Load, UNKNOWN FA type, defaulting to Carbonyl Type");
					}
				}
				
				
				
				
				
				inputRefSpecies[i-1] = new Compound(fullNameTemp, NChainsTemp, hmTemp, RefKMDTemp, minCTemp, maxCTemp, maxDBTemp, auxFAtype);
				
				//here, modify the compound for additional retention times, IF they exist
				/**
				 * index cases:
				 * TLOW, THIGH = -1, x: [t <= x]
				 * TLOW, THIGH = x -1: [t >= x]
				 * TLOW, THIGH = x,y: [x <= t <= y]
				 * TLOW, THIGH = -1, -1: no filtering, no columns definable
				 **/
				 
				 String[] tempHoldCSV = ((String)inputRefSpeciesHold.get(i)).split(",");
				 double auxLow = -1;
				 double auxHigh = -1;
				 
				 try{
					auxLow = Double.parseDouble(tempHoldCSV[this.colIndex("TLOW", headerLine)]);
				 }catch(Exception e){
					//e.printStackTrace();
					auxLow = 0;
				 }
				 
				 try{
					auxHigh = Double.parseDouble(tempHoldCSV[this.colIndex("THIGH", headerLine)]);
				 }catch(Exception e){
					//e.printStackTrace();
					auxHigh = Double.MAX_VALUE;
				 }
				 
				 inputRefSpecies[i-1].addRetFilter(auxLow, auxHigh);
				 
				 System.out.println(inputRefSpecies[i-1].getInfo()); //VERBOSE TESTING
			}catch(Exception e){
				System.out.println("Error Loading input Library KMD File. Check to make sure it is properly formatted.");
				e.printStackTrace();
				this.checkFailure = true;
			}
		}
		
		System.out.println("... added " + this.inputRefSpecies.length + " reference compounds");
		
		
		//testing only:
		/*for(int i = 0; i < inputRefSpecies.length; i++){
			System.out.println(inputRefSpecies[i].getInfo());
		}*/
		
		
		//load input raw experimental data (lines/string segments with # should be ignored) - no headers required
		//--> commented segments start with #
		
		if(auxMObs == null){
			this.inputMObsHold = this.fileContents(this.fInputMObs); //should contain
			if(inputMObsHold.size() <= 0){
				throw new Exception("File Error: Input Experimental Data File is Empty");
			}
			
			ArrayList<Double> inputMObsList = new ArrayList<Double>();
			
			System.out.print("Loading Input Experimental Data File ...");
			
			//first line: header file to contain all columns. we are working with CSV files here.
			for(int i = 1; i < inputMObsHold.size(); i++){
				int crunchIndex = ((String)inputMObsHold.get(i)).indexOf("#");
				if(crunchIndex < 0){ //line uncommented
					try{
						String auxHold[] = this.delimitLine((String)inputMObsHold.get(i)).split("\t");
						inputMObsList.add(Double.parseDouble(auxHold[0]));
					}catch(Exception e){
						System.out.println("Error Loading Input Experimental Data. Check to make sure that uncommented sections contain floating point numbers only.");
						//e.printStackTrace();
					}
				}
			}
			
			this.inputMObs = inputMObsList;
		}else{
			this.inputMObs = auxMObs;
		}
		
		if(inputMObs.size() <= 0){
			throw new Exception("File Error: Input Experimental Data File is Empty");
		}
		
		System.out.println(" .... added " + this.inputMObs.size() + " experimental data points.");
		
		//testing only
		/*for(int i = 0; i < inputMObs.size(); i++){
			System.out.println((Double)inputMObs.get(i));
		}*/
	
		
		if(checkFailure) throw new Exception("Error While Loading Input");
	}
	
	
	//REQUIRES: REFERENCE atomic masses to be loaded!
	public double exactMass(String formula) throws Exception{
		int strIndex = 0;
		String symbol;
		String numerical;
		double runningTotal = 0;
		while(strIndex < formula.length()){
			symbol = "";
			numerical = "";
			boolean incremented = false;
			//System.out.println(formula.charAt(strIndex));
			if(formula.charAt(strIndex) >= 65 && formula.charAt(strIndex) <= 90){
				symbol += formula.charAt(strIndex);				
				if(strIndex < formula.length()-1){
					incremented = true;
					strIndex++;
					
					while(strIndex < formula.length() && formula.charAt(strIndex) >= 97 && formula.charAt(strIndex) <= 122){
						//lowercase letter case to complete symbol
						//genericized to include non-typical elements (UUQ)
						symbol += formula.charAt(strIndex);
						strIndex++;
					}
					
					while(strIndex < formula.length() && formula.charAt(strIndex) >= 48 && formula.charAt(strIndex) <= 57){
						//numerical case
						numerical += formula.charAt(strIndex);
						strIndex++;
						
					}
				}
				
			}
			
			//System.out.println("Symbol: " + symbol + " Numerical = " + numerical + "StrIndex = " + strIndex);
			//compute masses, if applicable
			if(!symbol.equalsIgnoreCase("")){ //changed
				if(!numerical.equalsIgnoreCase("")){
					runningTotal += Integer.parseInt(numerical)*this.getAtomicMass(symbol);
				}else{
					runningTotal += this.getAtomicMass(symbol);
				}
			}
			
			if(!incremented) strIndex++;
		}
		
		return runningTotal;
	}
	
	public void setFilter(int filterType){
		this.filterSelect = filterType;
		//0 = NONE. 1 = positive mode, 2 = negative mode
	}
	
	private double getAtomicMass(String keySearch) throws Exception{
		int i = 0;
		while(i < this.inputMZRef.length){
			if(keySearch.equalsIgnoreCase(inputMZRef[i].getName())) return inputMZRef[i].getValue();
			i++;
		}
		throw new Exception("Calculation Error: Symbol: " + keySearch + " not Found in Atomic Masses Library. Check input files.");
		//return -1;
	}
	
	//does not require untrimmed methods!
	private String delimitLine(String auxInput){
		int i = 0;
		String input = auxInput.trim();
		String spaceSep = "";
		while(i < input.length()){
			if((int)input.charAt(i) != 9 && (int)input.charAt(i) != 32 && (int)input.charAt(i) != 44){
				if(i > 0 && ((int)input.charAt(i-1) == 9 || (int)input.charAt(i-1) == 32 || (int)input.charAt(i-1) == 44)){			
					spaceSep += "\t" + input.charAt(i);
				}else{
					spaceSep += input.charAt(i);
				}
			}
			i++;
		}
		return spaceSep;
	}
	
	//private boolean checkInput(int criteria, ArrayList<String> auxData){
	// return true;
	//}
	
	
	public void calculate(double rkmdTol, double NTCTol, boolean useRetFilter, boolean useOxFilter) throws Exception{
		final double c1 = 14/14.01565;
		final double c2 = 0.013399;
		final double c3 = -0.0229450649809451;
		final double c4 = 0.00669965360151537;
		
		this.rkmdTolerance = rkmdTol;
		this.NTCTolerance = NTCTol;
		this.matchResults = new String[this.inputMObs.size()+1];
		matchResults[this.inputMObs.size()] = (String)inputMObsHold.get(0);
		
		//oxidation table object initialization
		
		
		if(useOxFilter){
			oxTable = new oxFactor[5];
			oxTable[0] = new oxFactor(0,0,-1,""); //wildcard/uncorrected table entry
			oxTable[1] = new oxFactor(0,1,1,"ketone");
			oxTable[2] = new oxFactor(2,1,1,"hydroxyl");
			oxTable[3] = new oxFactor(1,2,1,"peroxide_radical");
			oxTable[4] = new oxFactor(2,2,1,"peracid");
		}else{
			oxTable = new oxFactor[1];
			oxTable[0] = new oxFactor(0,0,-1,""); //wildcard/uncorrected table entry
		}
	
		
		
		
		System.out.println("Retention Time Filtering: " + useRetFilter);
		
		for(int i = 0; i < inputMObs.size(); i++){
			matchResults[i] = "";
			for(int j = 0; j < inputRefSpecies.length; j++){
				
				
				for(int k = 0; k < oxTable.length; k++){ //k-loop start
				
				double refKMDCorrected = oxTable[k].correctionFactorKMD(inputRefSpecies[j].getRefKMD(), c3, c4);
				
				double rKMD = (1.0/c2)*((c1*(double)inputMObs.get(i))%1 - refKMDCorrected); //metric 1 (S1 - see implementation note, eq. 5)
				//System.out.println(inputRefSpecies[j].getName() + "\t\t" + rKMD); //test verbose
				
				
				boolean timeHit = true;
				if(useRetFilter){
					try{
						double compoundTime = Double.parseDouble(delimitLine((String)inputMObsHold.get(i+1)).split("\t")[1]);
						if(!inputRefSpecies[j].inRetRange(compoundTime)){
							timeHit = false;
						}
						
					}catch(Exception e){
						System.out.println("**Error: Unable to Use Retention Time Filter**");
					}
				}
				
				
				if(((Math.abs(Math.abs(Math.round(rKMD))- Math.abs(rKMD))) <= this.rkmdTolerance) && rKMD <= 0.5 && Math.abs(rKMD) <= inputRefSpecies[j].getMaxDB() && timeHit){
					
					String tabHold = this.delimitLine((String)inputMObsHold.get(i+1)) + "\tHIT:\t" + (inputRefSpecies[j].getName()).replace(";","\t") + "\t" + rKMD + "\t" + (Math.abs(Math.abs(Math.round(rKMD)) - Math.abs(rKMD)));
					
					matchResults[i] += tabHold;
					
					
					//only perform NTC calculations for admissable RKMDs:
					String adductExtract = (this.inputRefSpecies[j].getName()).split(";")[2];
					double mAdduct = this.exactMass(adductExtract.substring(3, adductExtract.indexOf("]")));
					if(adductExtract.charAt(2) == '-') mAdduct *= -1;
					//System.out.println(adductExtract + "\t" + mAdduct);
					
					//for(int k = 0; k < oxTable.length; k++){ //start k-loop
					
						double mIso = (double)this.inputMObs.get(i) - (inputRefSpecies[j].getmObs() + mAdduct); //mIso gives M_FA, experimental!
						double numHold = ((mIso + 2*exactMass("H")*Math.abs(Math.round(rKMD)))/inputRefSpecies[j].getNC()) + exactMass("H") - exactMass("O");
						double denomHold = (exactMass("C") + 2*exactMass("H"));
							//if((double)inputMObs.get(i) == 678.507838079542) System.out.println(adductExtract + inputRefSpecies[j].getmObs() + "\t" + numHold/denomHold);
						
						//add modifiers for aliphatic FA, if chosen
						if(inputRefSpecies[j].getFAtype() == -1){ //i.e, aliphatic
							numHold += (exactMass("O") - 2*exactMass("H"));
						}
												
						double nTC = inputRefSpecies[j].getNC()*((numHold)/denomHold);
						
						//modifiers for other getFAtype; semantically, getFAtype == NCC
						if(inputRefSpecies[j].getFAtype() > 0){
							numHold = mIso - inputRefSpecies[j].getFAtype()*exactMass("O") - 2*exactMass("H")*(0.5*inputRefSpecies[j].getNC() - inputRefSpecies[j].getFAtype() - Math.abs(Math.round(rKMD)));
							nTC = (numHold/denomHold);
						}
						
						//add correction factor generic to any equation
						//if((double)inputMObs.get(i) == 1048.984729) System.out.println(inputRefSpecies[j].getName() + " , BEFORE_NTC = " + nTC);
						
						nTC = nTC - (oxTable[k].correctionFactor(this.exactMass("H"),this.exactMass("O"),this.exactMass("C")));
						
						
						if((Math.abs(Math.round(nTC) - nTC) <= this.NTCTolerance) && inputRefSpecies[j].inRange(nTC) && oxTable[k].checkDOX(Math.abs(Math.round(rKMD)))){
							
							//if(k > 0) matchResults[i] += tabHold;
							
							matchResults[i] += "\tNTC_HIT\t" + Math.round(nTC) + "\t" + (Math.abs(Math.round(nTC) - nTC)) + "\t" + inputRefSpecies[j].getName().split(";")[1] + " [" + Math.round(nTC) + ":" + Math.abs(Math.round(rKMD)) + "]" + inputRefSpecies[j].getName().split(";")[2];
							
							if(!oxTable[k].printDesc().equalsIgnoreCase("")){
								matchResults[i] += ":" + oxTable[k].printDesc();
							}
							
							matchResults[i] += "\t";
							
							//PPM CALCULATION
							
							double mTFA = -1;
							double ppm = -1;
							
							if(inputRefSpecies[j].getFAtype() == 0){ //carbonyl MFA calculation								
								
								//mTFA = (Math.round(nTC)*denomHold) + inputRefSpecies[j].getFAtype()*exactMass("O") + 2*exactMass("H")*(0.5*inputRefSpecies[j].getNC() - inputRefSpecies[j].getNC() - Math.abs(Math.round(rKMD))) + oxTable[k].correctionFactorNum(this.exactMass("H"),this.exactMass("O"));
								
								mTFA = ((Math.round(nTC) + oxTable[k].correctionFactor(this.exactMass("H"),this.exactMass("O"),this.exactMass("C")))*(2*exactMass("H")+exactMass("C")) - 2*exactMass("H")*Math.abs(Math.round(rKMD)))/inputRefSpecies[j].getNC() - exactMass("H") + exactMass("O");
								
								mTFA *= inputRefSpecies[j].getNC();
								
								//System.out.print("NEW = " + mTFA + ",");
								
								//mTFA = ((inputRefSpecies[j].getNC())*(Math.round(numHold/denomHold)*exactMass("C") + (2*(Math.round(numHold/denomHold)) - 1)*(exactMass("H")) + exactMass("O"))) - (2*exactMass("H")*Math.abs(Math.round(rKMD)));
								
								//System.out.println("OLD = " + mTFA);
							}
							
							if(inputRefSpecies[j].getFAtype() == -1){ //aliphatic MFA calculation
								
								mTFA = ((Math.round(nTC) + oxTable[k].correctionFactor(this.exactMass("H"),this.exactMass("O"),this.exactMass("C")))*(2*exactMass("H")+exactMass("C")) - 2*exactMass("H")*Math.abs(Math.round(rKMD)))/inputRefSpecies[j].getNC() + exactMass("H");
								
								mTFA *= inputRefSpecies[j].getNC();
								
								//mTFA = ((inputRefSpecies[j].getNC())*(Math.round((numHold)/denomHold)*exactMass("C") + (2*(Math.round((numHold)/denomHold)) + 1)*exactMass("H"))) - (2*exactMass("H")*Math.abs(Math.round(rKMD)));
							}
							
							if(inputRefSpecies[j].getFAtype() > 0){ //plasmalogen or other NCC
								mTFA = (Math.round(nTC)*denomHold) + inputRefSpecies[j].getFAtype()*exactMass("O") + 2*exactMass("H")*(0.5*inputRefSpecies[j].getNC() - inputRefSpecies[j].getFAtype() - Math.abs(Math.round(rKMD))) + oxTable[k].correctionFactorNum(this.exactMass("H"),this.exactMass("O"));
							}
							
							double mTCalc = mTFA + inputRefSpecies[j].getmObs() + mAdduct;
							ppm = 1E6*(mTCalc - (double)this.inputMObs.get(i))/mTCalc;
							
							matchResults[i] += Math.abs(ppm) + "\n";
							
							
						}else{
							matchResults[i] += "\tNTC_MISS\t" + Math.round(nTC) + "\t" + (Math.abs(Math.round(nTC) - nTC)) + "\n";
						}
						
					
					//}//end k-loop here
					
				}else{
					matchResults[i] += this.delimitLine((String)inputMObsHold.get(i+1)) + "\tMISS:\t" + inputRefSpecies[j].getName() + "\t" + rKMD + "\t" + (Math.abs(Math.round(rKMD) - rKMD)) + "\n";
				}
			}
		}
		}
		
		/*verbose testing
		for(int i = 0; i < matchResults.length; i++){
			System.out.println("-----m/z = " + (double)this.inputMObs.get(i) + " ---------");
			System.out.println(this.matchResults[i]);
		}*/
	}
	
	public void writeToFile(String fOutput, int option) throws Exception{
		File output = new File(fOutput);
		PrintWriter pW = new PrintWriter(new FileWriter(fOutput, false));
		
		String header = this.delimitLine(matchResults[matchResults.length - 1]) + "\t MATCH_RESULT \t Compound_Name \t Family \t Adduct  \t RKMD \t RKMD_Tolerance \t NTC_RESULT \t Total_Carbons \t Carbon_Tolerance \t Matched_Lipid-ID \t Match_PPM";
		System.out.println("Filter Select = " + this.filterSelect);
		pW.println(header);
		for(int i = 0; i < (this.matchResults.length-1); i++){
			if(option == 1){ //display HITS and MISSES
				pW.println(matchResults[i]);
			}
			
			if(option == 2 && this.filterSelect == 0){ //display ONLY HITS
				String[] lineHold = matchResults[i].split("\n");
				for(int j = 0; j < lineHold.length; j++){
					if(!(lineHold[j].indexOf("HIT") < 0)){
						pW.println(lineHold[j]);
					} 
				}
			}
			
			if(option == 2 && this.filterSelect == 1){ //POSITIVE MODE ONLY
				String[] lineHold = matchResults[i].split("\n");
				for(int j = 0; j < lineHold.length; j++){
					if(!(lineHold[j].indexOf("]+") < 0) && !(lineHold[j].indexOf("HIT") < 0)){
						pW.println(lineHold[j]);
					}
				}
			}
			
			if(option == 2 && this.filterSelect == 2){ //NEGATIVE MODE ONLY
				String[] lineHold = matchResults[i].split("\n");
				for(int j = 0; j < lineHold.length; j++){
					if(!(lineHold[j].indexOf("]-") < 0) && !(lineHold[j].indexOf("HIT") < 0)){
						pW.println(lineHold[j]);
					} 
				}
			}
		}
		
		pW.close();
	}
	
	public String printMatchResults(){
		String output = "---MATCH RESULTS---\n";
		if(filterSelect == 1){
			output += "ION MODE: POSITIVE MODE\n";
		}else if (filterSelect == 2){
			output += "ION MODE: NEGATIVE MODE\n";
		}else{
			output += "ION MODE: NO FILTER\n";
		}
		output += "\nm/z, Match-Lipid-ID \n";
		
		
		int resultCount = 0;
		for(int i = 0; i < this.matchResults.length; i++){
			String[] lineHold = matchResults[i].split("\n");
			for(int j = 0; j < lineHold.length; j++){
				if(!(lineHold[j].indexOf("HIT") < 0) && !(lineHold[j].indexOf("NTC_HIT") < 0)){
					//apply filtering here
					if(filterSelect == 1 && !(lineHold[j].indexOf("]+") < 0) && !(lineHold[j].indexOf("HIT") < 0)){
						output += lineHold[j].split("\t")[0] + ", " + lineHold[j].split("\t")[lineHold[j].split("\t").length - 2] + "\n";
						resultCount++;
					}
					
					if(filterSelect == 2 && !(lineHold[j].indexOf("]-") < 0) && !(lineHold[j].indexOf("HIT") < 0)){
						output += lineHold[j].split("\t")[0] + ", " + lineHold[j].split("\t")[lineHold[j].split("\t").length - 2] + "\n";
						resultCount++;
					}
					
					if(filterSelect == 0){
						output += lineHold[j].split("\t")[0] + ", " + lineHold[j].split("\t")[lineHold[j].split("\t").length - 2] + "\n";
						resultCount++;
					}

				} 
			}
		}
		
		output += "\n ---> Number of Matches = " + resultCount + "\n";
		output += "\n ---> Experimental Masses Entered = " + inputMObs.size() +"\n";
		output += "------------END-------------";
		resultCount = 0;
		return output;
	}
	
	
	public static void main(String[] args) throws Exception{ //testing only!
		
		
		System.out.println("hello world 2");
		Pair y = new Pair("hello", 2);
		System.out.println(y.getName() + "\t\t" + y.getValue());
		
		Compound x[] = new Compound[2];
		
		/*x[0] = new Compound("SPECIES 1", 34, 340.039, -3);
		x[1] = new Compound("Species 1b", 45, -4);
		
		for(int i = 0; i < x.length; i++){
			if(x[i].isSpecies("Species 1")){
				System.out.println("Matched: " + x[i].getRefKMD());
				System.out.println("Matched: " + x[i].getNC());
			}else{
				System.out.println("Not Matched: " + x[i].getRefKMD());  
				System.out.println("Not Matched: " + x[i].getNC());
			}
		}*/
		
		final String path = "/home/ankit/Dropbox/RKMDProject_2013/GITHUB_checkout/rkmd/tests/";
		
		rkimp.rkmdBackEnd test = new rkimp.rkmdBackEnd(path+"masses_ox_test_UNOX.csv", path+"periodicMasses.csv", path+"ReferenceKMD.csv");
		test.loadFileInput(null);
		System.out.println(test.exactMass("Mn"));
		test.calculate(0.5, 0.001, true, true);
		
		
		test.writeToFile(path+"Results_KET_DEBUG.txt", 1);
		test.writeToFile(path+"Results_KET_ALLHITS.txt", 2);
		test.setFilter(1);
		test.writeToFile(path+"Results_KET_POSITIVE.txt", 2);
		test.setFilter(2);
		test.writeToFile(path+"Results_KET_NEGATIVE.txt", 2);
		test.setFilter(1);
		System.out.println(test.printMatchResults());
		
		
		//String test2 = "\t      quick brown \t fox       jumps over,lazy dog,!";
		//System.out.println(test.delimitLine(test2));
		
		/*ArrayList<String> tempHold = test.fileContents(new File(path+"1.txt")); 
		
		for(int i = 0; i < tempHold.size(); i++){
			System.out.println((String)tempHold.get(i));		
		}*/
		
		
		
		
	}
}
