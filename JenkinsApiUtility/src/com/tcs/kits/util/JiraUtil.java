package com.tcs.kits.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;

import javax.naming.AuthenticationException;

import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;

public class JiraUtil {

	static ArrayList<String> nonJiraIds= new ArrayList<String>();


	public void jiraControl(ArrayList<ArrayList<String>> allBoards,ArrayList<ArrayList<String>> csvArrayTotal){
		ArrayList<ArrayList<String>> nonJiraDetails = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> allIssues = new ArrayList<ArrayList<String>>();
		Boolean isCompleted = false;
		allIssues=getJiraDetails(allBoards);
		ExcelUtil excelUtil = new ExcelUtil();
		isCompleted=excelUtil.ExcelwriterJStories(allIssues);

		//get all commit id arrays from csvArrayTotal and create another array and send to a new method to edit the created excel
		if(isCompleted){
			nonJiraIds.add("NON_EXISTING_JIRA_STORIES");
			nonJiraDetails=getNonJiraIdDetails(csvArrayTotal, nonJiraIds);
			//System.out.println(nonJiraDetails);
			excelUtil.ExcelUpdateNonJira(nonJiraDetails);
		}
	}


	private static String invokeGetMethod(String auth, String url) throws AuthenticationException, ClientHandlerException {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
				.accept("application/json").get(ClientResponse.class);
		int statusCode = response.getStatus();
		if (statusCode == 401) {
			throw new AuthenticationException("Invalid Username or Password");
		}
		return response.getEntity(String.class);
	}


	private static ArrayList<String> getIssueDetails (Iterator itr, String auth, String BASE_URL, String API_ISSUE_PATH)
	{
		ArrayList<String> board1 = new ArrayList<String>();
		String concatString = "";
		String seperator = "~~split~~";
		String issueId="";
		int jiraCount=0;
		int nonJiraCount=0;
		long startTime = System.currentTimeMillis();

		try{

			System.out.println("Fetching Data for "+itr.next()+" board from KITS jira-board......");
			System.out.println();
			while(itr.hasNext()){
				//System.out.println("Inside while");
				issueId=itr.next().toString();
				String issue = invokeGetMethod(auth, BASE_URL + API_ISSUE_PATH + issueId );
				//System.out.println("Authenticated Successfully for::"+issue);
				JSONObject obj = new JSONObject(issue);

				if (obj!=null && !obj.toString().equalsIgnoreCase("{\"errors\":{},\"errorMessages\":[\"Issue Does Not Exist\"]}")){
					jiraCount++;	
					String key = obj.getString("key");
					JSONObject fields = obj.getJSONObject("fields");
					String summary = fields.getString("summary");
					JSONObject issuetypeObj = fields.getJSONObject("issuetype");
					String issuetypeName = issuetypeObj.getString("name");
					String priority = fields.getJSONObject("priority").getString("name");
					String status = fields.getJSONObject("status").getJSONObject("statusCategory").getString("name");

					concatString=concatString.concat(key);
					concatString=concatString.concat(seperator);
					concatString=concatString.concat(summary);
					concatString=concatString.concat(seperator);
					concatString=concatString.concat(status);
					concatString=concatString.concat(seperator);
					concatString=concatString.concat(issuetypeName);
					concatString=concatString.concat(seperator);
					concatString=concatString.concat(priority);


					board1.add(concatString);
					concatString="";

				}
				else{
					nonJiraCount++;
					nonJiraIds.add(issueId);
				}


			}



		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis();
		float totalTime=(endTime - startTime)/1000;


		System.out.println((jiraCount+nonJiraCount)+"("+jiraCount+" jira, "+ nonJiraCount + " non-jira) records fetched in "+totalTime+ " seconds");
		return board1;
	}

	public ArrayList<ArrayList<String>> getJiraDetails(ArrayList<ArrayList<String>> allBoards)
	{
		ArrayList<ArrayList<String>> board2 = new ArrayList<ArrayList<String>>();
		ArrayList<String> dummyBoard = new ArrayList<String>();
		Properties prop = new Properties();
		InputStream input = null;
		try{
			System.out.println("Reading properties for values.....");
			input = new FileInputStream("releaseNotes.properties");
			prop.load(input);

			String BASE_URL = prop.getProperty("BASE_URL").toString();
			String API_ISSUE_PATH = prop.getProperty("API_ISSUE_PATH").toString();

			String jiraUserID = prop.getProperty("jiraUserID").toString();
			String jiraPwd = prop.getProperty("jiraPwd").toString();
			String issueID = prop.getProperty("issueID").toString();





			//System.out.println("afafaf"+boards);

			String auth = new String(Base64.encode(jiraUserID+":"+jiraPwd));

			Iterator oItr = allBoards.iterator();
			long startTime = System.currentTimeMillis();
			System.out.println();
			System.out.println("Authenticating jira REST API using KITS id: "+jiraUserID);
			System.out.println();
			System.out.println("Initializing fetch module.....");
			while (oItr.hasNext()){
				Iterator itr = ((ArrayList<String>) oItr.next()).iterator();
				//System.out.println("get all issues");

				//Get Issue and add to the main board
				dummyBoard = getIssueDetails(itr, auth, BASE_URL, API_ISSUE_PATH);
				if (!dummyBoard.isEmpty()){
					board2.add(dummyBoard);
				}

			}

			long endTime = System.currentTimeMillis();
			float totalTime=(endTime - startTime)/1000;
			System.out.println("Total fetch time is "+ totalTime +" seconds");
			System.out.println();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println("Non-Jiras::"+nonJiraIds);

		return board2;
	}


	public ArrayList<ArrayList<String>> getNonJiraIdDetails(ArrayList<ArrayList<String>> csvArrayTotal, ArrayList<String> lnonJiraIds){
		ArrayList<ArrayList<String>> nonJiraDetails = new ArrayList<ArrayList<String>>();

		for (int i=0;i<lnonJiraIds.size(); i++){
			for (int j=0;j<csvArrayTotal.size();j++){
				if ((csvArrayTotal.get(j).get(3).contains(lnonJiraIds.get(i)))){
					if(!nonJiraDetails.contains(csvArrayTotal.get(j))){
						nonJiraDetails.add(csvArrayTotal.get(j));
					}
				}
			}
		}

		return nonJiraDetails;
	}


	public ArrayList<String> splitter(String concatString)
	{

		ArrayList<String> list = new ArrayList<String>(Arrays.asList(concatString.split("~~split~~")));

		return list;
	}

}
