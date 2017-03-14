package com.tcs.kits.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import au.com.bytecode.opencsv.CSVReader;

public class CsvUtil {
	
	public void csvReader (){
		CSVReader reader = null;
		int subjectIndex = 2;
		String jiraIdTrimmed = "";
        try
        {
            //Get the CSVReader instance with specifying the delimiter to be used
            reader = new CSVReader(new FileReader("D:\\projects\\try.csv"),'^');
            String [] nextLine;
            //Read one line at a time
            while ((nextLine = reader.readNext()) != null) 
            {
            	//System.out.println("length:::"+nextLine[2]);
               /* for(String token : nextLine)
                {
                    //Print all tokens
                    System.out.println(token);
                }
                */
                jiraIdTrimmed = trimmer(nextLine[subjectIndex].trim());
                //System.out.println(jiraIdTrimmed);
                
                //System.out.println("line break");
                
            }
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
		int indexOfFirst = 0;
		String trimmedStr =null;
		System.out.println(gitSubject);
		Pattern pOne = Pattern.compile("[]");
		 Pattern p = Pattern.compile(":[A-Z]*-[0-9]*");
		 Matcher m = p.matcher(gitSubject);
		 if (m.find()){
			 System.out.println(m.group().replace(':', ' '));
		 }
		
		 
			 indexOfFirst =  gitSubject.indexOf('[');
		
		int indexOfLast=gitSubject.indexOf(']');
		//System.out.println(indexOfFirst+"  "+indexOfLast);
		try{
			/*if (indexOfFirst==-1 && indexOfLast==-1){
				trimmedStr ="Merge String";
			}
			else{*/
		//trimmedStr=gitSubject.substring(indexOfFirst+1, indexOfLast);
			//}
		//System.out.println(trimmedStr);
		//System.out.println(trimmedStr.replaceAll(":[A-Z]*-[0-9]*", "faltu"));
		}
		catch (StringIndexOutOfBoundsException e){
			e.printStackTrace();
			
		}
		return trimmedStr;
	}

}
