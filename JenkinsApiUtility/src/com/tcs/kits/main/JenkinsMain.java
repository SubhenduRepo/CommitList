
package com.tcs.kits.main;

import com.tcs.kits.util.CsvUtil;

public class JenkinsMain {

	public static String argu0="";
	public static String argu1="";


	public static void main(String[] args) {
		argu0 = args[0];
		argu1 = args[1];
		System.out.println("Initializing project........");
		System.out.println();
		long startTime = System.currentTimeMillis();
		CsvUtil csvUtil = new CsvUtil();
		csvUtil.csvReader();
		long endTime = System.currentTimeMillis();
		float totalTime=(endTime - startTime)/1000;


		System.out.println("Total Execution Time:: "+totalTime+ " seconds");

	}
}
