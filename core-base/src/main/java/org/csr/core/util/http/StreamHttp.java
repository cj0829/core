package org.csr.core.util.http;

import java.util.List;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.NameValuePair;

public interface StreamHttp<T extends HttpEntityEnclosingRequest>{
	
	List<NameValuePair> getParam();

	void addHeader(T httpget);
	
	String getStream();
	
	String getUnicode();
}
