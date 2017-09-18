package com.gha.utils.excel.writer;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public interface SimpleExcelWriter {

	/**
	 * Creates a workbook file using a cellMap, to a given path
	 * 
	 * @param cellMap
	 * @param filePath
	 */
	void createStreamedFileAt(String filePath,Map<String,Map<Row,List<Cell>>> cellMap);

}