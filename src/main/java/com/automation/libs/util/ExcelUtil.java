package com.automation.libs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ExcelUtil {
	
	private String excelFilePath;
	private String excelWorksheetName;
	private String searchText;
	
	private int sheetFirstRowNum;
	private int sheetLastRowNum;
	
	private int headerRowNum = -1;
	private int startRowNum = -1;
	private int endRowNum = -1;
	
	private HSSFWorkbook workbook;
	private Sheet sheet;	
	private File file;
	private FileInputStream fis;
	private FileOutputStream fos;
	
	private Map<String, String> dataMap = new LinkedHashMap<String, String>();
	
	public ExcelUtil(String filePath, String worksheetName, String searchText) throws FileNotFoundException, IOException{
		this.excelFilePath = filePath;
		this.excelWorksheetName = worksheetName;
		this.searchText = searchText;
		
		getExcelWorkbook();
		getExcelSheet();
		setRowNumbersOfSearchText();
	}
	
	private File getFile(){		
		if(file==null){
			file = new File(excelFilePath);
		}
		
		return file;
	}
	
	private FileInputStream getExcelFile() throws FileNotFoundException{
		File file = getFile();
		fis = new FileInputStream(file);	
		return fis;
	}
	
	private HSSFWorkbook getExcelWorkbook() throws FileNotFoundException, IOException{		
		if(workbook==null){
	        workbook = new HSSFWorkbook(getExcelFile());
		}
		return workbook;
	}
	
	private Sheet getExcelSheet(){
		sheet = workbook.getSheet(excelWorksheetName);
		return sheet;
	}
	
	private void setRowNumbersOfSearchText(){
		
		Cell cell;
		sheetFirstRowNum = sheet.getFirstRowNum();
		sheetLastRowNum = sheet.getLastRowNum();
		
		if(searchText.equals("")){
			headerRowNum = sheetFirstRowNum;
			startRowNum = headerRowNum + 1;
			endRowNum = sheetLastRowNum;
		}
		else{
			for(int iterator=sheetFirstRowNum;iterator<=sheetLastRowNum;iterator++){
				Row row = sheet.getRow(iterator);			
			
				if(row==null){
					continue;
				}
				else{
					cell = row.getCell(0);					
				}
					
				String cellValue = cell.getStringCellValue();
				if(cellValue.equalsIgnoreCase(searchText)){				
					if(headerRowNum==-1){
						headerRowNum = row.getRowNum();
						startRowNum = (headerRowNum + 1);
					}
					else{
						endRowNum = row.getRowNum();
					}
				}
				else if(headerRowNum != -1){
					break;
				}
			}
		}
	}
	
	private void storeRowDataInMap(int dataRowNum){
		
		if(headerRowNum==-1){
			throw new NullPointerException("Failed to find test data for test case: " + searchText);
		}
		
		Row headerRow = sheet.getRow(headerRowNum);	
		Row dataRow = sheet.getRow(dataRowNum);
		
		int cellsCount = headerRow.getLastCellNum();
		int cntr=0;
		
		while(cntr<cellsCount){
			Cell headerCell = headerRow.getCell(cntr);
			Cell dataCell = dataRow.getCell(cntr);
			
			String headerValue = headerCell.getStringCellValue();
			String dataValue = "";
			
			if(dataCell!=null){
				switch(dataCell.getCellType()){
					case 1:
						dataValue = dataCell.getStringCellValue();
						break;
					case 0:
						dataValue = Double.toString(dataCell.getNumericCellValue());
						break;
					case 4:
						dataValue = Boolean.toString(dataCell.getBooleanCellValue());
						break;
				}
			}
			
			dataMap.put(headerValue, dataValue);
			cntr++;
		}
	}
	
	public int getSearchTextFirstRowNum(){
		return startRowNum;
	}
	
	public int getSearchTextLastRowNum(){
		return endRowNum;
	}
	
	public Map<String, String> getDataFromExcel(int rowNum) throws FileNotFoundException, IOException{		
		storeRowDataInMap(rowNum);
		closeExcel();
		return dataMap;
	}
	
	public void closeExcel() throws IOException{
		fis.close();
	}
	
	public Row getRowFromExcel(int rowNumber){
		Row row = sheet.getRow(rowNumber);
		return row;
	}
	
	public int getColumnNumberFromExcel(int rowNumber, String searchText) throws Exception{
		Row row = getRowFromExcel(rowNumber);		
		int columnNumber = -1;
		
		Iterator<Cell> cellIterator = row.cellIterator();	
		while(cellIterator.hasNext()){
			Cell cell = cellIterator.next();
			
			String cellValue = (String) getCellValueFromExcel(cell);			
			if(cellValue.equalsIgnoreCase(searchText)){
				columnNumber = cell.getColumnIndex();
				break;
			}
		}
		
		if(columnNumber==-1){
			throw new Exception(searchText + " is not found in row " + rowNumber);
		}
		
		return columnNumber;
	}
	
	public Object getCellValueFromExcel(Cell cell){
		
		Object cellValue = "";
		
		switch(cell.getCellType()){
			case 0:
				cellValue = cell.getNumericCellValue();
				break;
			case 1:
				cellValue = cell.getStringCellValue();
				break;			
			case 4:
				cellValue = cell.getBooleanCellValue();
				break;
		}
		
		return cellValue;
	}
	
	public void setCellValueInExcel(int rowNumber, String searchTextInHeader, String cellValueToSet) throws Exception{
		Row row = getRowFromExcel(rowNumber);
		int columnNumber = getColumnNumberFromExcel(0, searchTextInHeader);
		Cell cell = row.getCell(columnNumber);
		cell.setCellValue(cellValueToSet);
	}
	
	public void writeAndSaveExcel() throws IOException{
		fis.close();
		fos = new FileOutputStream(getFile());
		workbook.write(fos);
		workbook.close();
		fos.close();
	}
}
