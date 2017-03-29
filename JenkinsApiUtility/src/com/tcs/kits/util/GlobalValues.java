package com.tcs.kits.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class GlobalValues {
	
	public static void setGlobalProps(){
		Properties prop = new Properties();
		InputStream input = null;
		try{
			System.out.println("Reading properties for values.....");
			input = new FileInputStream("releaseNotes.properties");
			prop.load(input);
			
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
