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
	private double[] inputMObs;
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
	private void loadFile(File auxInputFile) throws Exception{
		//ArrayList<String> input = 
		String dataHold;
		BufferedReader bR = new BufferedReader(new FileReader(auxInputFile));
		dataHold = bR.readLine();
		while(dataHold != null){
			
		
		
		}
	}
	
	public String delimitLine(String auxInput){
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
		
		x[0] = new Compound("SPECIES 1", 34, 340.039, -3);
		x[1] = new Compound("Species 1b", 45, -4);
		
		for(int i = 0; i < x.length; i++){
			if(x[i].isSpecies("Species 1")){
				System.out.println("Matched: " + x[i].getRefKMD());
				System.out.println("Matched: " + x[i].getNC());
			}else{
				System.out.println("Not Matched: " + x[i].getRefKMD());  
				System.out.println("Not Matched: " + x[i].getNC());
			}
		}
		
		final String path = "/home/ankit/Dropbox/RKMDProject_2013/SVN_checkout/tests/";
		
		rkimp.rkmdBackEnd test = new rkimp.rkmdBackEnd(path+"1.txt", path+"2.txt", path+"3.txt");
		String test2 = "\t      quick brown \t fox       jumps over,lazy dog,!";
		System.out.println(test.delimitLine(test2));
		
		
	}
}
