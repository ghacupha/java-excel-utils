package com.gha.utils.excel.model;

import java.io.Serializable;

import org.apache.poi.ss.usermodel.CellType;

public class CellIndex implements Cloneable, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3388120682459818259L;

	private final int cellColumn;
	
	private final CellType cellType;

	/**
	 * @param cellColumn
	 * @param cellType
	 */
	public CellIndex(int cellColumn, CellType cellType) {
		super();
		this.cellColumn = cellColumn;
		this.cellType = cellType;
	}
	
	public CellIndex(CellIndex original){
		
		this(original.getCellColumn(),original.getCellType());
	}

	/**
	 * @return the cellColumn
	 */
	public int getCellColumn() {
		return cellColumn;
	}

	/**
	 * @return the cellType
	 */
	public CellType getCellType() {
		return cellType;
	}

	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		
		return new CellIndex(this);
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cellColumn;
		result = prime * result + ((cellType == null) ? 0 : cellType.hashCode());
		return result;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CellIndex)) {
			return false;
		}
		CellIndex other = (CellIndex) obj;
		if (cellColumn != other.cellColumn) {
			return false;
		}
		if (cellType != other.cellType) {
			return false;
		}
		return true;
	}

	
	@Override
	public String toString() {
		return "CellIndex [cellColumn=" + cellColumn + ", cellType=" + cellType + "]";
	}
	
	
}
