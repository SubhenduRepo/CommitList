
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
		
		
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			input = new FileInputStream("releaseNotes.properties");
			prop.load(input);
			
			 String BASE_URL = prop.getProperty("BASE_URL").toString();
			 String API_ISSUE_PATH = prop.getProperty("API_ISSUE_PATH").toString();
			
			String jiraUserID = prop.getProperty("jiraUserID").toString();
			String jiraPwd = prop.getProperty("jiraPwd").toString();
			String issueID = prop.getProperty("issueID").toString();
			
			ArrayList ccpIssue = new ArrayList<String>();
			
			ccpIssue.add("CCP-3687");
			ccpIssue.add("CCP-3686");
			ccpIssue.add("CCP-3685");
			ccpIssue.add("CCP-3682");
			ccpIssue.add("CCP-3655");
			
			
			ArrayList cfbIssue = new ArrayList<String>();
			cfbIssue.add("CFB-2088");
			cfbIssue.add("CFB-2082");
			cfbIssue.add("CFB-2067");
			cfbIssue.add("CFB-2053");
			cfbIssue.add("CFB-2046");
			
			ArrayList dcrbIssue = new ArrayList<String>();
			dcrbIssue.add("DCRB-2581");
			dcrbIssue.add("DCRB-2579");
			dcrbIssue.add("DCRB-2577");
			dcrbIssue.add("DCRB-2564");
			dcrbIssue.add("DCRB-2561");
			dcrbIssue.add("DCRB-2560");
			
			
			
			ArrayList<ArrayList<String>> boards = new ArrayList<ArrayList<String>>();
			boards.add(ccpIssue);
			boards.add(cfbIssue);
			boards.add(dcrbIssue);
			
			
			ArrayList<ArrayList<String>> board2 = new ArrayList<ArrayList<String>>();
			
			
			
			//System.out.println("afafaf"+boards);
			
		String auth = new String(Base64.encode(jiraUserID+":"+jiraPwd));
		
		Iterator oItr = boards.iterator();
		while (oItr.hasNext()){
		Iterator itr = ((ArrayList<String>) oItr.next()).iterator();
		
			//Get Issue and add to the main board
			//board2.add(getIssueDetails(itr, auth, BASE_URL, API_ISSUE_PATH));
		
		}
		
		
		ExcelUtil eu = new ExcelUtil();
		//eu.Excelwriter(board2);
		//eu.excelReader();

		CsvUtil csvUtil = new CsvUtil();
		csvUtil.csvReader();
		
		/*for (int j=0; j<board2.size(); j++){
			
		for (int i = 0 ; i<board2.get(j).size() ; i++){
		System.out.println("boasdasdad"+ board2.get(j).get(i));
		}
		System.out.println("============================================================");
		}*/

		} catch (ClientHandlerException e) {
			System.out.println("Error invoking REST method");
			e.printStackTrace();
		} 
		catch (FileNotFoundException fe) {
			System.out.println("File not found");
		fe.printStackTrace();	
		} catch (IOException e) {
			e.printStackTrace();
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
		try{
			while(itr.hasNext()){
				String issue = invokeGetMethod(auth, BASE_URL + API_ISSUE_PATH + itr.next());
				
				JSONObject obj = new JSONObject(issue);
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
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return board1;
	}
	
	

}
