package org.csr.core.util.http;

import java.nio.charset.Charset;

import org.apache.http.entity.mime.content.ContentBody;

public interface ContentParam {
	String getName();
	ContentBody getContentBody();
	Charset getCharset();
}
