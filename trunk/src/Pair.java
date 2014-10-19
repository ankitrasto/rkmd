/**
 * A Basic pair data structure containing a name and floating point value.
 * Intended for storing atomic mass data; i.e. periodic table data (symbol, exact mass)
 * Ankit Rastogi, 25-Jan-2014
 * License: GPLv3
 */

 package rkimp;
 
 public class Pair{
	
	private String name;
	private double value;
	
	
	/**
	 * Creates a data structure containing a name and floating point value.
	 * The name and value are stored globally in the Pair class.
	 * @param auxName Name specifier for a given pair 
	 * @param auxValue Value specifier for a given pair
	 */
	public Pair(String auxName, double auxValue){
		this.name = auxName;
		this.value = auxValue;
	}
	
	
	/**
	 *Accessor for a Pair's name; the name cannot be modified after construction at runtime.
	 * @return String
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 *Accessor for a Pair's value; the value cannot be modified after construction at runtime.
	 * @return double 
	 */
	public double getValue(){
		return this.value;
	}
	
	
	public static void main(String []args){
		System.out.println("Test");
		
	}
 }
