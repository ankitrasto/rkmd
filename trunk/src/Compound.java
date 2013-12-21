/**
 * Compound.java
 * Basic data structure for reference values (RKMD)
 * */

package rkimp;
 
public class Compound{
	
	private String name;
	private double mObs;
	private double refKMD;
	private int nChains;
	
	public Compound(String auxName, int auxNChains, double auxMObs, double auxRefKMD){
		this.name = auxName;
		this.nChains = auxNChains;
		this.mObs = auxMObs;
		this.refKMD = auxRefKMD;
	}
	
	public Compound(String auxName, int auxNChains, double auxRefKMD){
		this.name = auxName;
		this.nChains = auxNChains;
		this.refKMD = auxRefKMD;
		this.mObs = -1; //any mObs < 0 denotes that mObs has not been set
	}
	
	//accessors:
	
	public boolean isSpecies(String auxKey){
		return auxKey.equalsIgnoreCase(this.name);
	}
	
	public double getRefKMD(){
		return this.refKMD;
	}
	
	public double getmObs(){
		return this.mObs;
	}
	
	public double getNC(){
		return this.nChains;
	}
	
	
 
 
}
