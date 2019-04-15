package org.csr.core.util.http;

import java.util.List;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.NameValuePair;

public abstract class BaseStreamHttp<T extends HttpEntityEnclosingRequest> implements StreamHttp<T>{
	public List<NameValuePair> getParam(){
		return null;
	}

	public void addHeader(T httpget){
	}
	
	public  String getUnicode(){
		return "UTF-8";
	}
}
