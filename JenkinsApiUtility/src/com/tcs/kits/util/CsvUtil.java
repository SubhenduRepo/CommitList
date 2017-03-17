package com.tcs.kits.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;

import au.com.bytecode.opencsv.CSVReader;

public class CsvUtil {
	
	public void csvReader (){
		CSVReader reader = null;
		int subjectIndex = 1;
		String jiraIdTrimmed = "";
		
		ArrayList<ArrayList<String>> csvArrayTotal = new ArrayList<ArrayList<String>>();
        try
        {
            //Get the CSVReader instance with specifying the delimiter to be used
            reader = new CSVReader(new FileReader("D:\\projects\\try.csv"),'^');
            String [] nextLine;
            //Read one line at a time
            while ((nextLine = reader.readNext()) != null) 
            {
            	ArrayList<String> csvArray = new ArrayList<String>();
            	//System.out.println("length:::"+nextLine[2]);
                for(String token : nextLine)
                {
                	
                    //Print all tokens
                    //System.out.println(token);
                	csvArray.add(token);
                }
                
                jiraIdTrimmed = trimmer(nextLine[subjectIndex].trim());
                //System.out.println(jiraIdTrimmed);
                
                //System.out.println("line break");
                csvArray.add(jiraIdTrimmed);
                csvArrayTotal.add(csvArray);
                
            }
            
        
            
            
            //valueFromList(csvArrayTotal);
            ExcelUtil eu = new ExcelUtil();
            eu.excelWriterCL(csvArrayTotal);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    
	}
	
	public String trimmer(String gitSubject){
		
		String trimmedStr ="";
		System.out.println(gitSubject);
		Pattern pOne = Pattern.compile("\\[.*\\]");
		Pattern pTwo = Pattern.compile("[A-Z,a-z]{2,}-[0-9]{1,}");
		Matcher mOne = pOne.matcher(gitSubject);
		Pattern pThree = Pattern.compile("^Merge\\s");
		
		try{
		if (mOne.find()){
			trimmedStr ="";
			
			Matcher mTwo = pTwo.matcher(mOne.group());
			if (mTwo.find()){
				mTwo.reset();
			while(mTwo.find()){			
			//System.out.println(mTwo.group());
			trimmedStr = trimmedStr.concat(mTwo.group());
			trimmedStr=trimmedStr.concat(",");
			}
			}
			else{
				trimmedStr = "NonExistingJiraStories";
			}
		}
		else 
		{
			Matcher mThree= pThree.matcher(gitSubject);
			if(mThree.find()){
				trimmedStr = "noNeed";
			}
			else{
				trimmedStr = "NonExistingJiraStories";
			}
		}
		
		 
		}
		catch (Exception e){
			e.printStackTrace();
			
		}
		
		return trimmedStr;
		
	}
	
	
	
	
	public void valueFromList(ArrayList<ArrayList<String>> csvArrayTotal)
	{
		for (int j=0; j<csvArrayTotal.size(); j++)
		{
			for (int i = 0 ; i<csvArrayTotal.get(j).size() ; i++){
				System.out.println("value from List::"+csvArrayTotal.get(j).get(i));
			}
		}
	}
	
	
	
	

}
