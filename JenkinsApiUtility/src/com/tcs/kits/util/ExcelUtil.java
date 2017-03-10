package com.tcs.kits.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	
	private static final String OUTPUT_FILE_NAME = "D:\\MyFirstExcel.xlsx";
	private static final String INPUT_FILE_NAME = "D:\\Commit List from tag 57 to 88.xlsx";
    public void Excelwriter(ArrayList<ArrayList<String>> board2) {

    	
        
        
    	
    	String newWorkSheeetName =null;
    	XSSFWorkbook workbook = new XSSFWorkbook();
        for (int j=0; j<board2.size(); j++){
        	
        	String concatString = null;
            
            XSSFSheet sheet = workbook.createSheet("TestSheet"+j);
            XSSFCellStyle headingStyle = workbook.createCellStyle();
            XSSFCellStyle dataStyle = workbook.createCellStyle();
            
            headingStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
         
            XSSFCellStyle modifiedDataStyle = workbook.createCellStyle();
            XSSFCellStyle finalDataStyle = workbook.createCellStyle();
	
            headingStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            headingStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            headingStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            headingStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            headingStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            
            
            
            dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			 dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			 dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			 dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			 dataStyle.setWrapText(true);
			 
			 modifiedDataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			 modifiedDataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			 modifiedDataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			 modifiedDataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			 modifiedDataStyle.setWrapText(true);
			 modifiedDataStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
			 modifiedDataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
            
            XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);
            //headerFont.setFontHeightInPoints((short) 12); 
            headerFont.setFontName("ARIAL");
            headingStyle.setFont(headerFont);
            
            
            int rowNum=0;
            Row row = sheet.createRow(rowNum++);
            row.setHeight((short)400);
            int colNum=0;
            Cell cell1 = row.createCell(colNum++);
            cell1.setCellValue("Key");
            cell1.setCellStyle(headingStyle);
           
            Cell cell2 = row.createCell(colNum++);
            cell2.setCellValue("Summary");
            cell2.setCellStyle(headingStyle);
           
            Cell cell3 = row.createCell(colNum++);
            cell3.setCellValue("Status");
            cell3.setCellStyle(headingStyle);
            
            Cell cell4 = row.createCell(colNum++);
            cell4.setCellValue("Issue Type");
            cell4.setCellStyle(headingStyle);
           
            Cell cell5 = row.createCell(colNum++);
            cell5.setCellValue("Priority");
            cell5.setCellStyle(headingStyle);
           
            sheet.setColumnWidth(0, 5000);
            sheet.setColumnWidth(1, 15000);
            sheet.setColumnWidth(2, 7000);
            sheet.setColumnWidth(3, 7000);
            sheet.setColumnWidth(4, 7000);
            
            
    		for (int i = 0 ; i<board2.get(j).size() ; i++){
    		 concatString=board2.get(j).get(i);
    		 ArrayList<String> excelData = new ArrayList<String>();
    		 excelData = splitter(concatString);
    		 
    		 Iterator itr = excelData.iterator();
    		 
    		 if (!(excelData.get(2)).equalsIgnoreCase("DONE") && !(excelData.get(2)).equalsIgnoreCase("RESOLVED"))
    		 {
    			 finalDataStyle = modifiedDataStyle;
    		 }else{
    			 finalDataStyle = dataStyle;
    		 }
    		 	
    		 
    		 Row row1 = sheet.createRow(rowNum++);
    		 
    		 int colNum1=0;
    		 Boolean setColorFlag = false;
    		 
    		 while (itr.hasNext())
    		 {
    			 
    			 
    			 String value = itr.next().toString();
    			 if (colNum1 ==0 && rowNum ==2)
    			 {
    				  newWorkSheeetName = value.substring(0, value.indexOf("-"));
    			 }
    			 
    			 Cell cell6 = row1.createCell(colNum1++);
    			 
    			 
    			cell6.setCellValue(value);
    			cell6.setCellStyle(finalDataStyle);
    		 }
    		 
    		 workbook.setSheetName(workbook.getSheetIndex(sheet),newWorkSheeetName );
    		}
    		}
        
        
        
        try {
            FileOutputStream outputStream = new FileOutputStream(OUTPUT_FILE_NAME);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done");
    }
    
    public ArrayList<String> splitter(String concatString)
    {
    	
    	ArrayList<String> list = new ArrayList<String>(Arrays.asList(concatString.split("~~split~~")));
    	
    	return list;
    }

    
    
    public void excelReader()
    {
    	
        String allJiraIds="";
        Workbook workbook;
		try {
			FileInputStream inputStream = new FileInputStream(new File(INPUT_FILE_NAME));
			workbook = new XSSFWorkbook(inputStream);
			Sheet firstSheet = workbook.getSheetAt(0);
	        Iterator<Row> iterator = firstSheet.iterator();
	        
	        while (iterator.hasNext()) {
	            Row nextRow = iterator.next();
	    // System.out.println( nextRow.getCell(3).getCellType());
	            if (nextRow.getCell(3).getCellType() == 1){
	            System.out.println("value is:::"+nextRow.getCell(3).getStringCellValue());
	            allJiraIds = allJiraIds.concat(nextRow.getCell(3).getStringCellValue());
	            //Iterator<Cell> cellIterator = nextRow.cellIterator();
	        }
	            
	           
	        }
	        workbook.close();
	        inputStream.close();
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
}
