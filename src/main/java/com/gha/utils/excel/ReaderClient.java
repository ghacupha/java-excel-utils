/**
 * 
 */
package com.gha.utils.excel;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.gha.utils.excel.reader.SimpleExcelReader;
import com.gha.utils.excel.reader.SimpleExcelReaderImpl;
import com.gha.utils.excel.writer.SimpleExcelWriter;
import com.gha.utils.excel.writer.SimpleExcelWriterImpl;

/**
 * @author edwin.njeru
 *
 */
public class ReaderClient {
	
	private static String filePath = "C:\\ExcelFilesForAnalysis\\All_Debits.bk - Copy.xls";

	private static String writePath = "C:\\ExcelFilesForAnalysis\\Unified_Debits.xls";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SimpleExcelReader excelReader = SimpleExcelReaderImpl.createSimpleExcelReader();
		SimpleExcelWriter excelWriter = SimpleExcelWriterImpl.createSimpleExcelWriterImpl();
		
		Map<String,Map<Row,List<Cell>>> cellMap = excelReader.acquireBufferedCellMap(filePath);

		System.out.println("# of sheets : " + cellMap.size());
		System.out.println("List of sheets : " + cellMap.keySet());
		
		for(String key : cellMap.keySet()){
			
			System.out.println(key + " has "+cellMap.get(key).size()+ " rows");
		}
		
		System.out.println("Writing unified file from memory");
		
		excelWriter.createStreamedFileAt(writePath, cellMap);
		
		System.out.println("Reading row objects from filePath");
		excelReader.readRowObjects(filePath);
	}

}
