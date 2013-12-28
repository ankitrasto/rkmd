/**
 * Compound.java
 * Basic data structure for reference values (RKMD)
 * */

package rkimp;
 
public class Compound{
	
	private String name;
	private double mObs; //mass of headgroup
	private double refKMD;
	private int nChains;
	private int minC = -1;
	private int maxC = -1;
	
	public Compound(String auxName, int auxNChains, double auxMObs, double auxRefKMD, int auxMinC, int auxMaxC){
		this.name = auxName;
		this.nChains = auxNChains;
		this.mObs = auxMObs;
		this.refKMD = auxRefKMD;
		this.minC = auxMinC;
		this.maxC = auxMaxC;
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
	
	public boolean inRange(double auxCNo){
		return (auxCNo <= this.maxC) && (auxCNo >= this.minC);
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getInfo(){
		String temp = this.name + " KMD = " + this.refKMD + " , NC=" + this.nChains + " , HM=" + this.mObs;
		temp += ", xE[" + this.minC + "," + this.maxC + "]";
		return temp;
	}
	
	
 
 
}
