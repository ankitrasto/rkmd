/**
 * Pair.java
 * Basic Pair data structure = string, double ; immutable 
 */
 
 package rkimp;
 
 public class Pair{
	private String name;
	private double value;
	
	public Pair(String auxName, double auxValue){
		this.name = auxName;
		this.value = auxValue;
	}
	
	public String getName(){
		return this.name;
	}
	
	public double getValue(){
		return this.value;
	}
 }
