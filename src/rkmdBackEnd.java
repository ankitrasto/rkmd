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
	
	public static void main(String[] args){
		System.out.println("hello world 2");
		Pair x = new Pair("hello", 2);
		System.out.println(x.getName() + "\t\t" + x.getValue());
	}
}
