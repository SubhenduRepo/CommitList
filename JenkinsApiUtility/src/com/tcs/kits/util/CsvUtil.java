package com.tcs.kits.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tcs.kits.main.JenkinsMain;

import au.com.bytecode.opencsv.CSVReader;

public class CsvUtil {

	public void csvReader (){
		CSVReader reader = null;
		int subjectIndex = 1;
		String jiraIdTrimmed = "";
		String allJiraID=",";
		ArrayList<String> boardNames = new ArrayList<String>();

		ArrayList<ArrayList<String>> csvArrayTotal = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> allBoards = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> allIssues = new ArrayList<ArrayList<String>>();
		try
		{
			//Get the CSVReader instance with specifying the delimiter to be used
			reader = new CSVReader(new FileReader("D:\\projects\\GitOutput_Sample.csv"),'^');
			String [] nextLine;
			//Read one line at a time
			while ((nextLine = reader.readNext()) != null) 
			{
				ArrayList<String> csvArray = new ArrayList<String>();
				//System.out.println("length:::"+nextLine[2]);
				for(String token : nextLine)
				{
					csvArray.add(token);
				}

				jiraIdTrimmed = trimmer(nextLine[subjectIndex].trim());
				allJiraID=allJiraID+jiraIdTrimmed;
				//System.out.println(jiraIdTrimmed);
				//System.out.println(allJiraID);
				csvArray.add(jiraIdTrimmed);
				csvArrayTotal.add(csvArray);

			}


			Pattern p = Pattern.compile("(?<=,)([A-Z,a-z])\\w+");
			Matcher m = p.matcher(allJiraID);
			while(m.find()){			
				//System.out.println(m.group());
				if(!boardNames.contains((m.group()).toUpperCase())){
					boardNames.add(m.group().toUpperCase());
				}
			}

System.out.println(boardNames);


			//valueFromList(csvArrayTotal);
			ExcelUtil eu = new ExcelUtil();
			eu.excelWriterCL(csvArrayTotal);
			
			allBoards=createIssueBoard(boardNames,allJiraID);
			JenkinsMain jm = new JenkinsMain();
			allIssues=jm.getJiraDetails(allBoards);
			eu.ExcelwriterJStories(allIssues);
			
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
		//System.out.println(gitSubject);
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
					trimmedStr = "NON_EXISTING_JIRA_STORIES,";
				}
			}
			else 
			{
				Matcher mThree= pThree.matcher(gitSubject);
				if(mThree.find()){
					trimmedStr = "AUTO_MERGE,";
				}
				else{
					trimmedStr = "NON_EXISTING_JIRA_STORIES,";
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


	public ArrayList<ArrayList<String>> createIssueBoard(ArrayList<String> boardNames,String allJiraID)
	{
		System.out.println(allJiraID);
		ArrayList<ArrayList<String>> allBoards = new ArrayList<ArrayList<String>>();
		for(int i=0; i<boardNames.size();i++)
		{
		
			if(!boardNames.get(i).equalsIgnoreCase("AUTO_MERGE") && !boardNames.get(i).equalsIgnoreCase("NON_EXISTING_JIRA_STORIES")){
		String patternString = "("+boardNames.get(i)+")-[0-9]{1,}";
		
		Pattern boardPattern = Pattern.compile(patternString);
		Matcher boardMatcher= boardPattern.matcher(allJiraID);
		ArrayList<String> issueBoard = new ArrayList<String>();
		while(boardMatcher.find())
		{
			if(!issueBoard.contains(boardMatcher.group())){
			issueBoard.add(boardMatcher.group());
			}
		}
		allBoards.add(issueBoard);
			}
		}
		
		return allBoards;
		
	}



}
