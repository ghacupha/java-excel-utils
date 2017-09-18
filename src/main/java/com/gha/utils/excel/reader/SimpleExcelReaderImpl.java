/**
 * 
 */
package com.gha.utils.excel.reader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gha.utils.excel.model.CellIndex;
import com.gha.utils.excel.model.RowObject;


/**
 * @author edwin.njeru
 *
 */
public class SimpleExcelReaderImpl implements SimpleExcelReader {
	
	public static SimpleExcelReader createSimpleExcelReader() {
		return new SimpleExcelReaderImpl();
	}

	private final Logger log = LoggerFactory.getLogger(SimpleExcelReaderImpl.class);
	
	private ExcelFile excelFile;
	
	private SimpleExcelReaderImpl(){
		super();
		
		log.trace("{} created with no arguments",this);
		
		excelFile = ExcelFile.createExcelFile();
	}
	
	/**
	 * Generates a list of RowObjects from a workbook in a given filePath
	 * 
	 * @param filePath
	 * @return a List<RowObject> of row objects
	 */
	public List<RowObject> readRowObjects(String filePath){
		log.trace("List<RowObject> readRowObjects called with {} as argument",filePath);
		
		List<RowObject> rowObjects = new FastList<RowObject>();
		
		log.debug("Creating workbook...");
		ExcelFile excelFile = ExcelFile.createExcelFile(false);
		excelFile.setFileFormat(excelFile.acquireFileFormat(filePath));		
		Workbook workbook = acquireWorkbook(excelFile,filePath);
		
		if(workbook == null){
			
			log.error("The workbook object is null...");
			
		}else {
			log.debug("The workbook object created successfully");
			
			for(Sheet sheet : workbook){
				
				log.debug("Loping rows in : {}", sheet);
				
				for(Row row : sheet){
					
					RowObject rowObject = new RowObject(row.getRowNum());
					
					List<Map<CellIndex, Object>> rowList = 
							new FastList<Map<CellIndex,Object>>();
					
					Map<CellIndex,Object> cellMap = new UnifiedMap<CellIndex, Object>();
					
					for(Cell cell : row){
						
						CellIndex cellIndex =
								new CellIndex(cell.getColumnIndex(), cell.getCellTypeEnum());
						
						//TODO cellMap.put(cellIndex, getCellValue(cell));
						cellMap.put(cellIndex, cell.getRichStringCellValue());
										
						rowList.add(cellMap);
						
					}// closing the cell loop
					
					rowObject.setRowMap(rowList);
					
					try {
						
						rowObjects.add(rowObject.clone());
						
					} catch (CloneNotSupportedException e) {
						
						log.error("Exception thrown : {}. Caused by : {}. At {}",
								e.getMessage(),e.getCause(),e.getStackTrace());
					}
					
				}// Closing the row loop				
				
			}// closing the cell loop
			
		}		
		
		
		return rowObjects;
	}
	
	//TODO get cell value
	private Object getCellValue(Cell cell) {
		
		CellValue cellValue = null;

		if (cell != null) {

			switch (cell.getCellTypeEnum()) {
			
			case CellType.STRING.getCode():
				
				cellValue = cell.getStringCellValue();
				break;

			case Cell.CELL_TYPE_FORMULA:
				cellValue = cell.getCellFormula();
				break;

			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					cellValue = cell.getDateCellValue().toString();
				} else {
					cellValue = Double.toString(cell.getNumericCellValue());
				}
				break;

			case Cell.CELL_TYPE_BLANK:
				cellValue = "";
				break;

			case Cell.CELL_TYPE_BOOLEAN:
				cellValue = Boolean.toString(cell.getBooleanCellValue());
				break;

			}
		}

		return cellValue;
	}

	/**
	 * Creates a Map of sheets, indexed by sheet names to refer to maps of rows, holding
	 * lists of cells using unbuffered streams
	 * 
	 * @param filePath
	 * @return
	 */
	public Map<String,Map<Row,List<Cell>>> acquireUnBufferedCellMap(String filePath){
		
		log.trace("Map<String,Map<Row,List<Cell>>> acquireUnBufferedCellMap called with args {}",filePath);
	
		ExcelFile excelFile = ExcelFile.createExcelFile(false);
		excelFile.setFileFormat(excelFile.acquireFileFormat(filePath));
		
		Workbook workbook = acquireWorkbook(excelFile,filePath);
		
		Map<String,Map<Row,List<Cell>>> cellMap = new UnifiedMap<String, Map<Row,List<Cell>>>();
		
		try {
			for(Sheet sheet : workbook){
				
				String key = sheet.getSheetName();
				
				cellMap.put(key, new UnifiedMap<Row, List<Cell>>());
				
				for (Row row : sheet){
					
					cellMap.get(key).put(row, new FastList<Cell>());
				}
			}
		} catch (Exception e) {
			
			log.error("{} Caused by {} At {}",e.getMessage(),e.getCause(),e.getStackTrace());
		}
		
		return cellMap;
	}
	
	/**
	 * Creates a Map of sheets, indexed by sheet names to refer to maps of rows, holding
	 * lists of cells using buffered streams
	 * 
	 * @param filePath
	 * @return
	 */
	public Map<String,Map<Row,List<Cell>>> acquireBufferedCellMap(String filePath){
		
		log.trace("Map<String,Map<Row,List<Cell>>> acquireBufferedCellMap called with args {}",filePath);
		
		Workbook workbook = acquireImplicitWorkbook(filePath);
		
		Map<String,Map<Row,List<Cell>>> cellMap = new UnifiedMap<String, Map<Row,List<Cell>>>();
		
		for(Sheet sheet : workbook){
			
			String key = sheet.getSheetName();
			
			cellMap.put(key, new UnifiedMap<Row, List<Cell>>());
			
			for (Row row : sheet){
				
				cellMap.get(key).put(row, new FastList<Cell>());
			}
		}
		
		return cellMap;
	}
	
	/**
	 * Creates a Map of sheets, indexed by sheet names to refer to maps of rows, holding
	 * lists of cells using the workbook as parameter
	 * 
	 * @param workbook
	 * @return
	 */
	public Map<String,Map<Row,List<Cell>>> acquireCellMap(Workbook workbook){
		
		log.trace("Map<String,Map<Row,List<Cell>>> acquireCellMap called with args {}",workbook.getClass());
		
		Map<String,Map<Row,List<Cell>>> cellMap = new UnifiedMap<String, Map<Row,List<Cell>>>();
		
		for(Sheet sheet : workbook){
			
			String key = sheet.getSheetName();
			
			cellMap.put(key, new UnifiedMap<Row, List<Cell>>());
			
			for (Row row : sheet){
				
				cellMap.get(key).put(row, new FastList<Cell>());
			}
		}
		
		return cellMap;
	}
	
	/**
	 * Generates a Map of a map of rows with their list of cells, by iterating over a list
	 * of sheets
	 * 
	 * The names of sheets are used as key for the whole map
	 * 
	 * @param sheetList
	 * @return Map<String,Map<Row,List<Cell>>> cell
	 */
	public Map<String,Map<Row,List<Cell>>> acquireCellMap(List<Sheet> sheetList){
		
		log.trace("Map<String,Map<Row,List<Cell>>> acquireCellMap called with args {}",sheetList.getClass());
		
		Map<String,Map<Row,List<Cell>>> cellMap = new UnifiedMap<String, Map<Row,List<Cell>>>();
		
		for(Sheet sheet : sheetList){
			
			String key = sheet.getSheetName();
			
			cellMap.put(key, new UnifiedMap<Row, List<Cell>>());
			
			for(Row row : sheet){
				
				cellMap.get(key).put(row, new FastList<Cell>());
				
				for(Cell cell : row){
					
					cellMap.get(key).get(row).add(cell);
				}
			}
		}
		
		
		return cellMap;
	}
	
	/**
	 * Creates a map indexed  by sheet names to access lists of rows
	 * @param sheetList
	 * @return
	 */
	public Map<String,List<Row>> acquireRowMap(List<Sheet> sheetList){
		log.trace("Map<String,List<Row>> acquireRowMap called with args {}",sheetList.getClass());
		
		Map<String,List<Row>> rowMap = new UnifiedMap<String, List<Row>>();
		
		for(Sheet sheet: sheetList){
			
			rowMap.put(sheet.getSheetName(), new FastList<Row>());
			
			for(Row row : sheet){
				
				rowMap.get(sheet.getSheetName()).add(row);
			}
		}
		
		return rowMap;
	}
	
	/**
	 * Generates a list of sheets from the workbook
	 * 
	 * @param workBook
	 * @return
	 */
	public List<Sheet> acquireSheetList(Workbook workBook){
		
		log.trace("List<Sheet> acquireSheetList called with args {}",workBook.getClass());
		
		List<Sheet> sheetList = new FastList<Sheet>();
		
		for(Sheet sheet : workBook){
			
			sheetList.add(sheet);
		}
		
		return sheetList;
	}
	
	/**
	 * Generates a List of sheets using a buffered file stream
	 * 
	 * @param filePath
	 * @return
	 */
	public List<Sheet> acquireBufferedWorkBookSheetList(String filePath){
		
		log.trace("List<Sheet> acquireBufferedWorkBookSheetList called with args {}",filePath.getClass());
		
		List<Sheet> sheetList = new FastList<Sheet>();
		
		Workbook workBook = acquireImplicitWorkbook(filePath);
		
		for(Sheet sheet : workBook){
			
			sheetList.add(sheet);
		}
		
		return sheetList;
	}
	
	/**
	 * This method acquires a workbook object using an implicit assumptions about the
	 * excel file
	 * 
	 * @param filePath
	 * @return
	 */
	public Workbook acquireImplicitWorkbook(String filePath){
		
		log.trace(" acquireImplicitWorkbook called with args {}",filePath.getClass());
		
		Workbook workbook = null;
			
			log.debug("Creating workbook using buffered stream");
			
			workbook = createBufferedWorkBook(this.excelFile,filePath);
		
		return workbook;
	}
	
	
	/**
	 * This method acquires a workbook object using an explicitly defined workbook type
	 * 
	 * @param excelFile
	 * @param filePath
	 * @return
	 */
	public Workbook acquireWorkbook(ExcelFile excelFile,String filePath){
		
		log.trace("Workbook acquireWorkbook called with args {}",filePath.getClass());
		
		Workbook workbook = null;
		
		
		if(excelFile.isBufferIO()){
			
			log.debug("Creating workbook using buffered stream");
			
			workbook = createBufferedWorkBook(excelFile,filePath);
			
		} else if(!excelFile.isBufferIO()){
			
			log.debug("Creating workbook using unbuffered stream");
			
			workbook = createUnBufferedWorkBook(excelFile, filePath);
			
		}
		
		return workbook;
	}
	
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
	public Workbook createBufferedWorkBook(ExcelFile excelFile,String filePath){
		
		Workbook workbook = null;
		
		// Overrides naive fileFormat assumptions from excelFile
		excelFile.setFileFormat(excelFile.acquireFileFormat(filePath));
		
		
			try {
				if (excelFile.getFileFormat() == FileFormat.OLD_FORMAT ) {
					
					workbook = new HSSFWorkbook(excelBufferedStream(filePath));
					
				} else if (excelFile.getFileFormat() == FileFormat.NEW_FORMAT ){
					
					workbook = new XSSFWorkbook(excelBufferedStream(filePath));
				}
			} catch (IOException e) {

				log.error("{} caused by : {} at : {}", e.getMessage(), e.getCause(), e.getStackTrace());
			}
			
			
		return workbook;			
				
	}
	
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
	public Workbook createUnBufferedWorkBook(ExcelFile excelFile,String filePath){
		
		Workbook workbook = null;
		
		// baby here sets the fileFormat nicely but naively
		excelFile.setFileFormat(excelFile.acquireFileFormat(filePath));
		
			try {
				if (excelFile.getFileFormat() == FileFormat.OLD_FORMAT ) {
					
					workbook = new HSSFWorkbook(excelStream(filePath));
					
				} else if (excelFile.getFileFormat() == FileFormat.NEW_FORMAT ){
					
					workbook = new XSSFWorkbook(excelStream(filePath));
				}
			} catch (IOException e) {

				log.error("{} Caused by : {} At : {}", e.getMessage(), e.getCause(), e.getStackTrace());
			} catch(Exception ex){
				
				log.error("{} Caused by : {} At : {}", ex.getMessage(), ex.getCause(), ex.getStackTrace());
			}
			
			
		return workbook;			
				
	}
	
	/**
	 * Generates buffered inputStream from the file path
	 * @param path
	 * @return
	 */
	@SuppressWarnings("unused")
	private InputStream excelBufferedStream(String path){
		
		log.trace("InputStream excelBufferedStream called with args {}",path.getClass());

		BufferedInputStream stream = null;
		if (path != null) {
			try {
				stream = new BufferedInputStream(new FileInputStream(new File(path)));
				log.debug("File stream created : {}", stream.getClass());
			} catch (FileNotFoundException e) {

				log.error("{} caused by : {} at : {}", e.getMessage(), e.getCause(), e.getStackTrace());
			} 
		} else {
			
			log.error("The path of the excel file is null");
		}
		
		
		return stream;
	}
	
	/**
	 * Generates unbuffered inputStream from the file path
	 * 
	 * @param file
	 * @return
	 */
	@SuppressWarnings("unused")
	private InputStream excelStream(String file){
		
		log.trace("InputStream excelStream called with args {}",file.getClass());

		InputStream stream = null;

		if (file != null) {
			try {
				stream = new FileInputStream(new File(file));

				log.debug("File stream created : {}", stream.getClass());

			} catch (FileNotFoundException e) {

				log.error("{} caused by : {} at : {}", e.getMessage(), e.getCause(), e.getStackTrace());

			} 
		} else {

			log.error("The filepath given is null");
		}
		return stream;
	}	

}
