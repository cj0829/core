/**
 * Project Name:core
 * File Name:sdfdsf.java
 * Package Name:org.csr.core.common
 * Date:2014-1-27上午9:59:27
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
*/
package org.csr.core.intercepter;

import java.io.Serializable;
import java.util.Stack;

import org.csr.core.page.Page;

public class HistoryUrl implements Serializable{
	private static final long serialVersionUID=1L;
	private String url;
	private Page page;
	
	private boolean backtrack=false;
	
	private Stack<HistoryUrl>  child = new Stack<HistoryUrl>();
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public Stack<HistoryUrl> getChild() {
		return child;
	}
	
	public static void resolve(String url){
		
	}
	public void setPage(Page page) {
		this.page=page;
	}
	public Page getPage() {
		return this.page;
	}
	public boolean isBacktrack() {
		return backtrack;
	}
	public void setBacktrack(boolean backtrack) {
		this.backtrack = backtrack;
	}
	public void isNew() {
		// TODO Auto-generated method stub
		
	}
	public void setNew() {
		// TODO Auto-generated method stub
		
	}
	public void setNew(int i) {
		// TODO Auto-generated method stub
		
	}
}
