/*
 * Created on 2013-05-24
 *
 * Copyright pointdew Inc.
 */
package org.csr.core.util.extractor;

import java.io.InputStream;

import org.csr.core.util.io.IOUtil;

/**
 * @author star
 *
 */
public class ExtractorTxtImpl extends ExtractorTextImpl {

	private static final ExtractorTxtImpl INSTANCE = new ExtractorTxtImpl();

	private ExtractorTxtImpl() {

		// noop
	}

	public static ExtractorText getExtractor() {

		return INSTANCE;
	}

	public ExtractionResult extractText(InputStream in, String encoding) throws Exception {
		String result = IOUtil.stringFromStream(in, encoding);
		return new ExtractionResultImpl(result);
	}

}
