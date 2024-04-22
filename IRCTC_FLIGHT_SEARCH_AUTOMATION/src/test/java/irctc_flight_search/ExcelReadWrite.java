package irctc_flight_search;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReadWrite {
	
	public static File src;
	public static FileInputStream fis;
	public static FileOutputStream fos;
	public static XSSFWorkbook work;
	public static XSSFSheet sheet;
	public static XSSFRow row;
	public static XSSFCell cell;
	
	// Method to read data from Excel Sheet
	public static String readExcel(String sheetName, int rowNum, int column) throws IOException{
		fis = new FileInputStream("src/ExcelFiles/Excel_Data.xlsx");
		work = new XSSFWorkbook(fis);
		sheet = work.getSheet(sheetName);
		row = sheet.getRow(rowNum);
		cell = row.getCell(column);
		
		String data;
		try {
			DataFormatter formatter =new DataFormatter();
			data = formatter.formatCellValue(cell);
			return data;
		}catch(Exception e) {
			data = "";
		}
		work.close();
		fis.close();
		return data;	
	}
	// Method to write data into Excel Sheet
	public static void setExcel( String sheetName, int rowNum, int column,String data) throws IOException{
		fis = new FileInputStream("src/ExcelFiles/Excel_Data.xlsx");
		work = new XSSFWorkbook(fis);
		fis.close();
		sheet = work.getSheet(sheetName);
		row = sheet.getRow(rowNum);
		if(row == null) {
			row = sheet.createRow(rowNum);
		}
		cell = row.createCell(column);
		cell.setCellValue(data);
		fos = new FileOutputStream("src/ExcelFiles/Excel_Data.xlsx");
		work.write(fos);
		fos.close();
		work.close();
	}
	//Method to clear the previous data in the Excel sheet
	public static void clearExcel() throws IOException {
		fis = new FileInputStream("src/ExcelFiles/Excel_Data.xlsx");
		work = new XSSFWorkbook(fis);
		fis.close();
		sheet = work.getSheet("Test_Results");
		while(sheet.getLastRowNum() >= 0) {
			 row = sheet.getRow(sheet.getLastRowNum());
			sheet.removeRow(row);
		}
		fos = new FileOutputStream("src/ExcelFiles/Excel_Data.xlsx");
		work.write(fos);
		fos.close();
		work.close();
		
	}
	
	

}
