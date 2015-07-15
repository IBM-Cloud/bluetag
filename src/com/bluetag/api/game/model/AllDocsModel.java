package com.bluetag.api.game.model;

import java.util.ArrayList;

public class AllDocsModel {
	private int total_rows;
	private int offset;
	private ArrayList<CloudantRowModel> rows;
	
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
	public ArrayList<CloudantRowModel> getRows() {
		return rows;
	}
	public void setRows(ArrayList<CloudantRowModel> rows) {
		this.rows = rows;
	}
}
