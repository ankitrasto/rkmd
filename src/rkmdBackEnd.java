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

public class rkmdBackEnd{
	
	//input arrays + output array
	private ArrayList<Double> inputMObs;
	private Pair[] inputMZRef; //if CLI/file input is used, initially load results into ArrayList. 
	private Compound[] inputRefSpecies;
	private String[] matchResults; //should match number of lines in input/length of inputMObs
	
	//file IOs:
	private File fInputMObs; //raw observational data
	private File fInputMZRef; // reference IUPAC M/Z's
	private File fInputRefSpecies; // reference KMDs/NChains
	private File fOutputMatchResults;
	private boolean checkFailure = false;
	
	
	// statistical tolerances for metrics
	public double rkmdTolerance;
	public double NTCTolerance;
	
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
	
	
	// REQUIRES: file formatted correctly
	private ArrayList<String> fileContents(File auxInputFile) throws Exception{
		ArrayList<String> input = new ArrayList<String>(); 
		String dataHold;
		BufferedReader bR = new BufferedReader(new FileReader(auxInputFile));
		dataHold = bR.readLine();
		while(dataHold != null){
			input.add(this.delimitLine(dataHold));
			dataHold = bR.readLine();
		}
		bR.close();
		return input;
	}
	
	private int colIndex(String auxKey, String[] auxHeader){
		for(int i = 0; i < auxHeader.length; i++){
			if(auxHeader[i].equalsIgnoreCase(auxKey)) return i;
		}
		
		return -1;
	}
	
	
	public void loadFileInput() throws Exception{
		String headerLine[];
		String tempHold[];
		
		//(1) load reference m/z
		
		// a) check input table should have 3 columns (UNIQUE SYMBOLS, masss):
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
		|| this.colIndex("Headgroup_Mass", headerLine) < 0 || this.colIndex("MinC", headerLine) < 0 || this.colIndex("MaxC", headerLine) < 0){
			throw new Exception("Input Reference KMD Data Incorrectly Formatted: Check to ensure that data contains 3 properly named headers");
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
				inputRefSpecies[i-1] = new Compound(fullNameTemp, NChainsTemp, hmTemp, RefKMDTemp, minCTemp, maxCTemp);
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
		ArrayList<String> inputMObsHold = this.fileContents(this.fInputMObs);
		if(inputMObsHold.size() <= 0){
			throw new Exception("File Error: Input Experimental Data File is Empty");
		}
		
		ArrayList<Double> inputMObsList = new ArrayList<Double>();
		
		System.out.print("Loading Input Experimental Data File ...");
		
		for(int i = 0; i < inputMObsHold.size(); i++){
			int crunchIndex = ((String)inputMObsHold.get(i)).indexOf("#");
			if(crunchIndex < 0){ //line uncommented
				try{
					inputMObsList.add(Double.parseDouble((String)inputMObsHold.get(i)));
				}catch(Exception e){
					System.out.println("Error Loading Input Experimental Data. Check to make sure that uncommented sections contain floating point numbers only.");
					e.printStackTrace();
				}
			}
			
			if(crunchIndex > 0){ //line commented mid-position
				try{
					inputMObsList.add(Double.parseDouble((((String)inputMObsHold.get(i)).substring(0, crunchIndex)).trim()));
				}catch(Exception e){
					System.out.println("Error Loading Input Experimental Data. Check to make sure that uncommented sections contain floating point numbers only.");
					e.printStackTrace();
				}
			}
		}
		
		this.inputMObs = inputMObsList;
		
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
	
	
	//REQUIRES: REFERENCE atomic masses be loaded!
	public double exactMass(String formula) throws Exception{
		int strIndex = 0;
		String symbol;
		String numerical;
		double runningTotal = 0;
		while(strIndex < formula.length()){
			symbol = "";
			numerical = "";
			boolean incremented = false;
			System.out.println(formula.charAt(strIndex));
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
			
			System.out.println("Symbol: " + symbol + " Numerical = " + numerical + "StrIndex = " + strIndex);
			//compute masses, if applicable
			if(!symbol.equalsIgnoreCase(""));{
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
	
	private boolean checkInput(int criteria, ArrayList<String> auxData){
	 return true;
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
		
		final String path = "/home/ankit/Dropbox/RKMDProject_2013/SVN_checkout/tests/";
		
		rkimp.rkmdBackEnd test = new rkimp.rkmdBackEnd(path+"masses.txt", path+"periodicMasses.csv", path+"ReferenceKMD.csv");
		test.loadFileInput();
		System.out.println(test.exactMass("Mn"));
		
		
		//String test2 = "\t      quick brown \t fox       jumps over,lazy dog,!";
		//System.out.println(test.delimitLine(test2));
		
		/*ArrayList<String> tempHold = test.fileContents(new File(path+"1.txt")); 
		
		for(int i = 0; i < tempHold.size(); i++){
			System.out.println((String)tempHold.get(i));		
		}*/
		
		
		
	}
}