package org.csr.core.util.http;

import java.nio.charset.Charset;

import org.apache.http.entity.mime.content.ContentBody;

public class BaseContentParam implements ContentParam {

	String name;
	ContentBody body;
	Charset charset = null;

	public BaseContentParam(String name, ContentBody body) {
		this.name = name;
		this.body = body;
	}

	public BaseContentParam(String name, ContentBody body, Charset charset) {
		this.name = name;
		this.body = body;
		this.charset = charset;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public ContentBody getContentBody() {
		return this.body;
	}

	@Override
	public Charset getCharset() {
		return this.charset;
	}

}
