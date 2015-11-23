package com.bluetag.model;

import java.util.ArrayList;

public class AllLocationDocsModel {
	private int total_rows;
	private int offset;
	private ArrayList<LocationRowModel> rows;
	
	public int getTotal_rows() {
		return total_rows;
	}
	public void setTotal_rows(int total_rows) {
		this.total_rows = total_rows;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public ArrayList<LocationRowModel> getRows() {
		return rows;
	}
	public void setRows(ArrayList<LocationRowModel> rows) {
		this.rows = rows;
	}
}
