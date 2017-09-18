package com.gha.utils.excel.reader;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This models the excel file object
 * 
 * @author edwin.njeru
 *
 */
public class ExcelFile {
	
	

	private final Logger log = LoggerFactory.getLogger(ExcelFile.class);
	private FileFormat fileFormat;	
	private boolean bufferIO;

	/**
	 * This little factory creates an excel file through explicit description of whether
	 * the file would be buffered or not
	 * @param bufferIO
	 * @return
	 */
	public static ExcelFile createExcelFile(boolean bufferIO) {
		return new ExcelFile(bufferIO);
	}
	
	/**
	 * This factory will create an excelFile with default assumptions
	 * @return
	 */
	public static ExcelFile createExcelFile(){
		
		return new ExcelFile();
	}
	
	/**
	 * This constructor creates an excelFile object with default assumpions like we prefer to
	 * use a buffered stream, with a NEW_FORMAT type of file
	 */
	private ExcelFile(){
		this.fileFormat = FileFormat.NEW_FORMAT;
		this.bufferIO = true;
	}
	
	/**
	 * @param bufferIO
	 */
	private ExcelFile(boolean bufferIO) {
		
		this.bufferIO = bufferIO;
	}

	/**
	 * @param fileFormat
	 * @param bufferIO
	 */
	public ExcelFile(FileFormat fileFormat, boolean bufferIO) {
		super();
		this.fileFormat = fileFormat;
		this.bufferIO = bufferIO;
	}
	
	

	public FileFormat getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(FileFormat fileFormat) {
		this.fileFormat = fileFormat;
	}

	public boolean isBufferIO() {
		return bufferIO;
	}

	public void setBufferIO(boolean bufferIO) {
		this.bufferIO = bufferIO;
	}
	
	/**
	 * This method uses a naive assumption of the convention that the new format files
	 * will have .xlsx extension while the old format files will have the .xls extension
	 * 
	 * @param filePath
	 * @return
	 */
	public FileFormat acquireFileFormat(String filePath){
		
		log.trace("FileFormat acquireFileFormat called with filePath args: {}",filePath);

		FileFormat fileFormat = null;
		
		if (filePath != null) {
			try {
				String fileExt = FilenameUtils.getExtension(filePath);
				log.trace("File path extension : {}", fileExt);
				if (fileExt.equalsIgnoreCase("xslx")) {

					log.debug("File format deduced as NEW FORMAT");

					fileFormat = FileFormat.NEW_FORMAT;

				} else if (fileExt.equalsIgnoreCase("xls")) {

					log.debug("File format deduced as OLD FORMAT");

					fileFormat = FileFormat.OLD_FORMAT;

				} else if (fileExt.equalsIgnoreCase("xlsb")) {

					log.debug("File format deduced as NEW FORMAT");

					fileFormat = FileFormat.NEW_FORMAT;
				} else {

					log.debug("File format deduced as NEW FORMAT");

					fileFormat = FileFormat.NEW_FORMAT;

				}
			} catch (Exception e) {
				
				log.error("{} caused by {} .. at {}",e.getMessage(),e.getCause(),e.getStackTrace());
			} 
		} else {
			
			log.error("The filePath is null");
		}
		return fileFormat;
	}

}
