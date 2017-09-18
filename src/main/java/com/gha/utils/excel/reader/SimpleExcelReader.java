/**
 * 
 */
package com.gha.utils.excel.reader;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.gha.utils.excel.model.RowObject;

/**
 * 
 * Simple methods for reading an excel file and generating structures from them
 * 
 * @author edwin.njeru
 *
 */
public interface SimpleExcelReader {
	
	/**
	 * Creates a Map of sheets, indexed by sheet names to refer to maps of rows, holding
	 * lists of cells using unbuffered streams
	 * 
	 * @param filePath
	 * @return
	 */
	Map<String,Map<Row,List<Cell>>> acquireUnBufferedCellMap(String filePath);
	
	/**
	 * Creates a Map of sheets, indexed by sheet names to refer to maps of rows, holding
	 * lists of cells using buffered streams
	 * 
	 * @param filePath
	 * @return
	 */
	Map<String,Map<Row,List<Cell>>> acquireBufferedCellMap(String filePath);
	
	/**
	 * Creates a Map of sheets, indexed by sheet names to refer to maps of rows, holding
	 * lists of cells using thw workbook as parameter
	 * 
	 * @param workbook
	 * @return
	 */
	Map<String,Map<Row,List<Cell>>> acquireCellMap(Workbook workbook);
	
	
	/**
	 * Generates a Map of a map of rows with their list of cells, by iterating over a list
	 * of sheets
	 * 
	 * The names of sheets are used as key for the whole map
	 * 
	 * @param sheetList
	 * @return Map<String,Map<Row,List<Cell>>> cell
	 */
	Map<String,Map<Row,List<Cell>>> acquireCellMap(List<Sheet> sheetList);
	
	
	/**
	 * Creates a map indexed  by sheet names to access lists of rows
	 * @param sheetList
	 * @return
	 */
	Map<String,List<Row>> acquireRowMap(List<Sheet> sheetList);
	
	
	/**
	 * Generates a list of sheets from the workbook
	 * 
	 * @param workBook
	 * @return
	 */
	List<Sheet> acquireSheetList(Workbook workBook);
	
	
	/**
	 * Generates a List of sheets using a buffered file stream
	 * 
	 * @param filePath
	 * @return
	 */
	List<Sheet> acquireBufferedWorkBookSheetList(String filePath);
	
	/**
	 * This method acquires a workbook object using an implicit assumptions about the
	 * excel file
	 * 
	 * @param filePath
	 * @return
	 */
	Workbook acquireImplicitWorkbook(String filePath);
	
	/**
	 * Generates a list of RowObjects from a workbook in a given filePath
	 * 
	 * @param filePath
	 * @return a List<RowObject> of row objects
	 */
	List<RowObject> readRowObjects(String filePath);
	
	/**
	 * This method acquires a workbook object using an explicitly defined workbook type
	 * 
	 * @param excelFile
	 * @param filePath
	 * @return
	 */
	Workbook acquireWorkbook(ExcelFile excelFile,String filePath);
	
	/**
	 * This private method abstracts the exact creation of the workbook object
	 * using the excelFile, by using two different classes for the two different types of
	 * files
	 * This is useful where there is need for speedy operations by reducing the number of IO
	 * operations, but results in high memory usage
	 * 
	 * @param excelFile
	 * @return
	 */
	Workbook createBufferedWorkBook(ExcelFile excelFile,String filePath);
	
	/**
	 * This private method abstracts the exact creation of the workbook object
	 * using the excelFile, by using two different classes for the two different types of
	 * files.
	 * This method creates an unbuffured stream, which is useful is there are memory usage
	 * concerns, but results in possibly slow execution time due to increased IO operations
	 * 
	 * @param excelFile
	 * @return
	 */
	Workbook createUnBufferedWorkBook(ExcelFile excelFile,String filePath);
	
	
	
}
