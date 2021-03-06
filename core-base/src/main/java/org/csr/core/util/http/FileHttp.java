package org.csr.core.util.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpRequestBase;

public abstract class FileHttp<T extends HttpRequestBase> {

	public List<NameValuePair> getParam() {
		return new ArrayList<NameValuePair>(0);
	}
	
	public String getUnicode(){
		return "UTF-8";
	}
	public void addHeader(T httpget) {

	}

	abstract public List<ContentParam> getContentParam();

}
