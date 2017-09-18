/**
 * 
 */
package com.gha.utils.excel.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.eclipse.collections.impl.list.mutable.FastList;

/**
 * This class is a representation of the row as an object of the excel file
 * A sheet will contain a list of such objects, and the object itself
 * saves the cells as map members of a list (represents fields for flexibility), which is to be persisted using
 * the id as index for each row object.
 * The id is determined by the sheet number and the row number.
 * The key of the map contains an inner class, which contains the cellValueType, and the value
 * in the map is the cell value, of the same type as the cellValueType contained in the key
 * object
 * 
 * @author edwin.njeru
 *
 */
public class RowObject implements Cloneable, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2424750641586362347L;

	/**
	 * Row number of the row object
	 */
	private final int id;
	
	/**
	 * The row is a list of cell maps. Each cell is index by cell column
	 * and cellType
	 */
	private List<Map<CellIndex,Object>> rowMap;
	
	

	/**
	 * @param no args
	 */
	public RowObject() {
		super();
		
		id = 0;
		rowMap = new FastList<Map<CellIndex,Object>>();
	}
	
	

	/**
	 * @param id
	 */
	public RowObject(int id) {
		super();
		this.id = id;
		rowMap = new FastList<Map<CellIndex,Object>>();
	}



	/**
	 * @param id
	 * @param rowMap
	 */
	public RowObject(int id, List<Map<CellIndex, Object>> rowMap) {
		super();
		this.id = id;
		this.rowMap = rowMap;
	}

	public RowObject(RowObject original){
		
		this(original.getId(), original.getRowMap());
	}
	
	
	@Override
	public RowObject clone() throws CloneNotSupportedException {
		
		return new RowObject(this);
	}



	/**
	 * @return the rowMap
	 */
	public List<Map<CellIndex, Object>> getRowMap() {
		return rowMap;
	}



	/**
	 * @param rowMap the rowMap to set
	 */
	public void setRowMap(List<Map<CellIndex, Object>> rowMap) {
		this.rowMap = rowMap;
	}



	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	
}
