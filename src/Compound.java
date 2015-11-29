/**
 * Basic data structure for storing reference lipid parameters
 * <p> Immutable after construction
 * <p> Used in the RKMD back-end for iterative matching and calculations
 * LICENSE: GPL v.3.0.
 * */

package rkimp;

public class Compound{
    
    
    class timeWindow{
		public double tLow;
		public double tHigh; 
		public timeWindow(double tLoAux, double tHighAux){
			tLow = tLoAux;
			tHigh = tHighAux;
		}
	}  
	
	/**
	 * Semantically represents an optional range of retention times for use in filtering
	 */
	 private timeWindow retFilter = null;
	
	/**
	* Semantically represents the compound's name; stored as a hierarchy of 
class/compound/saturation, etc. delimited by semicolons.
	*/
	private String name;
	
	/**
	* Semantically represents the mass of the compound; typically a 
headgroup
	*/
	private double mObs; //mass of headgroup
	
	/**
	* Semantically represents Reference KMD pulled from 
literature/calculated from headgroups; user-defined.
	*/
	private double refKMD;
	
	/**
	* Number of Fatty acid chains
	*/
	private int nChains;
	
	/**
	* Minimum number of allowable carbon-atoms in the compound
	*/
	private int minC = -1;
	
	/**
	* Maximum number of allowable carbon-atoms in the compound
	*/
	private int maxC = -1;
	
	/**
	 * Maximum number of allowable DOUBLE-BONDS in the compound
	 */
	private int maxDB = -1;
	
	/**
	 * Type of fatty acid: (0 = carbonyl, 1 = aliphatic, ...other species can be added)
	 * 
	*/
	private int FAtype = -1; 
	
	/**
	* Generates a "Compound" object for matching and calculation algorithms 
in RKMD; the parameters are typically
	* loaded from an algorithm which reads a database file.
	* @param auxName Compound name; it is best practice to include the class, 
subclass and adduct as the name, and delimit this hierarchy by semicolons. 
Example: name="class;subclass;adduct"
	* @param auxNChains Number of chains 
	* @param auxMObs Reference mass for the compound
	* @param auxRefKMD Reference mass for the compoun
	* @param auxMinC Minimum number of allowable carbon-atoms in the compound
	* @param auxMaxC Maximum number of allowable carbon-atoms in the compound
	*/
	public Compound(String auxName, int auxNChains, double auxMObs, double 
auxRefKMD, int auxMinC, int auxMaxC, int auxMaxDB, int auxFAtype){
		this.name = auxName;
		this.nChains = auxNChains;
		this.mObs = auxMObs;
		this.refKMD = auxRefKMD;
		this.minC = auxMinC;
		this.maxC = auxMaxC;
		this.maxDB = auxMaxDB;
		this.FAtype = auxFAtype;
	}
	
	
	public Compound(String auxName, int auxNChains, double auxMObs, double 
auxRefKMD, int auxMinC, int auxMaxC, int auxMaxDB, int auxFAtype, double tLo, double tHi){
		this.name = auxName;
		this.nChains = auxNChains;
		this.mObs = auxMObs;
		this.refKMD = auxRefKMD;
		this.minC = auxMinC;
		this.maxC = auxMaxC;
		this.maxDB = auxMaxDB;
		this.FAtype = auxFAtype;
		this.retFilter = new timeWindow(tLo, tHi);
	}
	
	//modifiers
	
	/**
	 * Modify this compound with a sensical timewindow 
	 */
	 
	 public void addRetFilter(double tLo, double tHi){
		this.retFilter = new timeWindow(tLo, tHi);
	 }
	
	//accessors:
	
	/**
	* Determines if a string matches the name of this compound. <code>TRUE</code> if the strings match.
	@param auxKey the string query
	@result Boolean
	*/
	public boolean isSpecies(String auxKey){
		return auxKey.equalsIgnoreCase(this.name);
	}
	
	/**
	 Accessor for the reference KMD value of this compound.
	*/	
	public double getRefKMD(){
		return this.refKMD;
	}
	
	/**
	 Accessor for the m/z value of this compound.
	*/
	public double getmObs(){
		return this.mObs;
	}
	
	/**
	  Accessor for the number of FA chains in this compound.
	*/
	public double getNC(){
		return this.nChains;
	}
	
	public double getMaxDB(){
		return this.maxDB;
	}
	
	/**
	  Determines if a given value (auxCNo) falls between the minimum and maximum number of carbon atoms for this compound.
	  <p> 
	  The equality is inclusive: i.e. <code>inRange(x)</code> returns <code>TRUE</code> if <code> minC <= x <= maxC </code>
	  @param auxCNo the input value to test
	*/
	public boolean inRange(double auxCNo){
		return (auxCNo <= this.maxC) && (auxCNo >= this.minC);
	}
	
	/**
	 * Determines if given value falls within an optional retention time window
	 * */
	 
	 public boolean inRetRange(double auxT){
		if(retFilter != null){
			return (auxT >= retFilter.tLow && auxT <= retFilter.tHigh);
		}
		return true;
	 }
	
	
	/**
	  Accessor for the name of this compound.
	*/
	public String getName(){
		return this.name;
	}
	
	/**
	 * Accessor for the fatty-acid type of this compound
	 */
	public int getFAtype(){
		return this.FAtype;
	}
	
	
	/**
	  Test accessor for all parameters of this compound; prints name, reference KMD, number of chains, m/z value, and minimum/maximum total carbon atoms.
	*/
	public String getInfo(){
		String temp = this.name + " KMD = " + this.refKMD + " , NC=" + this.nChains + " , HM=" + this.mObs;
		temp += ", xE[" + this.minC + "," + this.maxC + "]";
		temp += ", MAXDB = " + this.maxDB;
		temp += ", **RETFILTER = (" + this.retFilter.tLow + " , " + this.retFilter.tHigh + ")";
		return temp;
	}

}
