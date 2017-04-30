package com.example.brave.curriculum;

public class Classtable {

	private Integer id;
	private String info;
	private String [][] classtable;
	private String parameter;

	public Classtable() {
	}

	public Classtable(String info, String[][] classtable, String parameter) {
		this.info = info;
		this.classtable = classtable;
		this.parameter = parameter;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String[][] getClasstable() {
		return classtable;
	}
	public void setClasstable(String[][] classtable) {
		this.classtable = classtable;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
}
