package com.tcs.kits.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import au.com.bytecode.opencsv.CSVReader;

public class CsvUtil {

	public void csvReader (){
		long startTime = System.currentTimeMillis();
		CSVReader reader = null;
		int subjectIndex = 1;
		String jiraIdTrimmed = "";
		String allJiraID=",";
		int procRecords=0;
		ArrayList<String> boardNames = new ArrayList<String>();
		ArrayList<ArrayList<String>> csvArrayTotal = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> allBoards = new ArrayList<ArrayList<String>>();

		try
		{
			reader = new CSVReader(new FileReader("D:\\projects\\try.csv"),'^');
			String [] nextLine;
			while ((nextLine = reader.readNext()) != null) 
			{
				ArrayList<String> csvArray = new ArrayList<String>();
				for(String token : nextLine)
				{
					procRecords++;
					csvArray.add(token);
				}
				jiraIdTrimmed = trimmer(nextLine[subjectIndex].trim());
				allJiraID=allJiraID+jiraIdTrimmed;
				csvArray.add(jiraIdTrimmed.substring(0, jiraIdTrimmed.length()-1));
				csvArrayTotal.add(csvArray);
			}
			Pattern p = Pattern.compile("(?<=,)([A-Z,a-z])\\w+");
			Matcher m = p.matcher(allJiraID);
			while(m.find()){			
				if(!boardNames.contains((m.group()).toUpperCase())){
					boardNames.add(m.group().toUpperCase());
				}
			}
			long endTime = System.currentTimeMillis();
			float totalTime=(endTime - startTime)/1000;
			System.out.println(procRecords+" records processed in "+totalTime+" seconds");
			System.out.println();

			ExcelUtil eu = new ExcelUtil();
			eu.excelWriterCL(csvArrayTotal);
			allBoards=createIssueBoard(boardNames,allJiraID);
			JiraUtil jiraUtil = new JiraUtil();
			jiraUtil.jiraControl(allBoards,csvArrayTotal);
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







	public ArrayList<ArrayList<String>> createIssueBoard(ArrayList<String> boardNames,String allJiraID)
	{
		ArrayList<ArrayList<String>> allBoards = new ArrayList<ArrayList<String>>();
		for(int i=0; i<boardNames.size();i++)
		{
			if(!boardNames.get(i).equalsIgnoreCase("AUTO_MERGE") && !boardNames.get(i).equalsIgnoreCase("NON_EXISTING_JIRA_STORIES")){
				String patternString = "(?<=,)("+boardNames.get(i)+")-[0-9]{1,}";
				Pattern boardPattern = Pattern.compile(patternString);
				Matcher boardMatcher= boardPattern.matcher(allJiraID);
				ArrayList<String> issueBoard = new ArrayList<String>();
				issueBoard.add(boardNames.get(i));
				while(boardMatcher.find())
				{
					System.out.println(boardMatcher.group());
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
