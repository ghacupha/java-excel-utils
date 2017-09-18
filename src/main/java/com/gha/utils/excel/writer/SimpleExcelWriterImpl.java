package com.gha.utils.excel.writer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleExcelWriterImpl implements SimpleExcelWriter {
	
	public static SimpleExcelWriterImpl createSimpleExcelWriterImpl() {
		return new SimpleExcelWriterImpl();
	}

	private final Logger log = LoggerFactory.getLogger(SimpleExcelWriterImpl.class);
	private final static int MAX_ROW_HEAP = 1000;
	
	private SimpleExcelWriterImpl(){
		
		super();
		
		log.debug("{} created with no arguments",this);
	}
	
	/**
	 * Creates a workbook file using a cellMap, to a given path
	 * 
	 * @param cellMap
	 * @param filePath
	 */
	public void createStreamedFileAt(String filePath,Map<String,Map<Row,List<Cell>>> cellMap){
		
		log.trace("void createStreamedFileAt method called with args {}",filePath);
		
		
		log.debug("Workbook creeted with max stream of rows set as : {}",MAX_ROW_HEAP);		
		Workbook workbook = generateWorkbook(cellMap);
		
		log.debug("Creating the output stream...");
		OutputStream outputStream = null;
		
		try {
			outputStream = 
					new BufferedOutputStream(new FileOutputStream(new File(filePath)));
			
			log.debug("Writing workbook to the outputstream >>>");
			workbook.write(outputStream);
			
			log.debug("Closing the outputstream <<<");
			outputStream.close();
			
			log.debug("Closing the workbook <<<");
			workbook.close();
			
		} catch (FileNotFoundException e) {
			
			log.error("Exception thrown : {}. Caused by : {}. At : {}",
					e.getMessage(),e.getCause(),e.getStackTrace());
			
		} catch (IOException e) {

			log.error("Exception thrown : {}. Caused by : {}. At : {}",
					e.getMessage(),e.getCause(),e.getStackTrace());
			
		} catch (Exception e) {
			
			log.error("Exception thrown : {}. Caused by : {}. At : {}",
					e.getMessage(),e.getCause(),e.getStackTrace());
		} finally{
			
			try {
				if(outputStream != null){
					
					log.debug("Apparrently the outputstream was not closed. Closing it again");
					outputStream.close();
				}
				
				if(workbook != null){
					
					log.debug("Apparrently the workbook was not closed. Closing it again");
					workbook.close();
				}
			} catch (IOException e) {
				
				log.error("Exception thrown: We were unable to close resources : {}. Caused by :{} \n"
						+ "At : {}",e.getMessage(),e.getCause(),e.getStackTrace());
			}
		}
	}

	private Workbook generateWorkbook(Map<String, Map<Row, List<Cell>>> cellMap) {
		
		Workbook wb = new SXSSFWorkbook(MAX_ROW_HEAP);
		
		for(String key : cellMap.keySet()){
			
			log.debug("Creating : {}", key);
			
			wb.createSheet(key);
			
			for(Row row : cellMap.get(key).keySet()){
				
				for(Cell cell : cellMap.get(key).get(row)){
					
					Cell newCell = row.createCell(cell.getColumnIndex());
					newCell.setCellValue(cell.getStringCellValue());
				}
			}
		}
		
		return wb;
	}
	
	
}
