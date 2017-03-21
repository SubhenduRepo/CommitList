
package com.tcs.kits.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import javax.naming.AuthenticationException;

import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;

import com.tcs.kits.util.*;

public class JenkinsMain {

	

	public static void main(String[] args) {
		
		CsvUtil csvUtil = new CsvUtil();
		csvUtil.csvReader();
		
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
		try{
			Iterator itr1=itr;
			String write=itr1.next().toString();
			System.out.println("Writing excel for "+  write.substring(0, write.indexOf("-")) +"board");
			while(itr.hasNext()){
				String issueStr=itr.next().toString();
				String issue = invokeGetMethod(auth, BASE_URL + API_ISSUE_PATH + issueStr );
				//System.out.println("Authenticated Successfully for::"+issueStr);
				JSONObject obj = new JSONObject(issue);
				if (obj!=null && !obj.toString().equalsIgnoreCase("{\"errors\":{},\"errorMessages\":[\"Issue Does Not Exist\"]}")){
					//System.out.println("obj=="+obj+"   and issueStr=="+issueStr);
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
				
			}
			
		
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return board1;
	}
	
	public ArrayList<ArrayList<String>> getJiraDetails(ArrayList<ArrayList<String>> allBoards)
	{
		ArrayList<ArrayList<String>> board2 = new ArrayList<ArrayList<String>>();
		Properties prop = new Properties();
		InputStream input = null;
		try{
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
	while (oItr.hasNext()){
	Iterator itr = ((ArrayList<String>) oItr.next()).iterator();
	System.out.println("get all issues");
	System.out.println(oItr);
	//System.out.println(itr.next());
		//Get Issue and add to the main board
		board2.add(getIssueDetails(itr, auth, BASE_URL, API_ISSUE_PATH));
	
	}
	
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return board2;
	}
	

}
