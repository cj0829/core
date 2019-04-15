/*
 * File   : $Source: TextExtractor.java,v $
 * Date   : $Date: 2013/03/06 09:10:01 $
 * Version: $Revision: 1.1.1.1 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Mananagement System
 *
 * Copyright (c) 2005 Alkacon Software GmbH (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software GmbH, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.csr.core.util.extractor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.csr.core.Constants;
import org.csr.core.util.io.IOUtil;

/**
 * Base utility class that allows extraction of the indexable "plain" text from a given document format.<p>
 * 
 * @author Alexander Kandzior 
 * 
 * @version $Revision: 1.1.1.1 $ 
 * 
 * @since 6.0.0 
 */
public class ExtractorTextImpl implements ExtractorText {
	/** Static member instance of the extractor. */
	private static final ExtractorTextImpl INSTANCE = new ExtractorTextImpl();

	/**
	 * Returns an instance of this text extractor.<p> 
	 * 
	 * @return an instance of this text extractor
	 */
	public static ExtractorText getExtractor() {

		return INSTANCE;
	}

	/** A buffer in case the input stream must be read more then once. */
	protected byte[] m_inputBuffer;

	/**
	 * @see org.ExtractorText.search.extractors.I_CmsTextExtractor#extractText(byte[])
	 */
	public ExtractionResult extractText(byte[] content) throws Exception {

		// encoding is null        
		return extractText(content, null);
	}

	public ExtractionResult extractText(String content) throws Exception {
		return extractText(IOUtil.bytesFromStream(new ByteArrayInputStream(content.getBytes(Constants.UTF8_ENCODING))));
	}

	/**
	 * @see org.ExtractorText.search.extractors.I_CmsTextExtractor#extractText(byte[], java.lang.String)
	 */
	public ExtractionResult extractText(byte[] content, String encoding) throws Exception {

		// call stream based method of extraction
		m_inputBuffer = content;
		return extractText(new ByteArrayInputStream(content), encoding);
	}

	/**
	 * @see org.ExtractorText.search.extractors.I_CmsTextExtractor#extractText(java.io.InputStream)
	 */
	public ExtractionResult extractText(InputStream in) throws Exception {

		// encoding is null        
		return extractText(in, null);
	}

	/**
	 * @see org.ExtractorText.search.extractors.I_CmsTextExtractor#extractText(java.io.InputStream, java.lang.String)
	 */
	public ExtractionResult extractText(InputStream in, String encoding) throws Exception {

		ByteArrayOutputStream out = new ByteArrayOutputStream(512);
		int c = 0;
		while (true) {
			try {
				c = in.read();
				if (c < 0) {
					break;
				}
				out.write(c);
			} catch (IOException e) {
				// finished
				break;
			}
		}

		// call byte array based method of extraction
		return extractText(out.toByteArray(), encoding);
	}

	/**
	 * Creates a copy of the original input stream, which allows to read the input stream more then 
	 * once, required for certain document types.<p>
	 * 
	 * @param in the inpur stram to copy
	 * @return a copy of the original input stream
	 * @throws IOException in case of read errors from the original input stream
	 */
	public InputStream getStreamCopy(InputStream in) throws IOException {

		if (m_inputBuffer != null) {
			return new ByteArrayInputStream(m_inputBuffer);
		}

		// read the input stream fully and copy it to a byte array
		ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
		int c = 0;
		while (true) {
			c = in.read();
			if (c < 0) {
				break;
			}
			out.write(c);
		}
		m_inputBuffer = out.toByteArray();

		// now return a reader from the byte array
		return new ByteArrayInputStream(m_inputBuffer);
	}

	/**
	 * Removes "unwanted" control chars from the given content.<p>
	 * 
	 * @param content the content to remove the unwanted control chars from
	 * 
	 * @return the content with the unwanted control chars removed
	 */
	protected String removeControlChars(String content) {

		if (StringUtils.isBlank(content)) {
			// to avoid later null pointer exceptions an empty String is returned
			return "";
		}

		content = removeXMLTagChars(content);

		char[] chars = content.toCharArray();
		StringBuffer result = new StringBuffer(chars.length);
		boolean wasUnwanted = false;
		for (int i = 0; i < chars.length; i++) {
			char ch = chars[i];

			int type = Character.getType(ch);
			switch (type) {

			// punctuation
			case Character.CURRENCY_SYMBOL:
			case Character.CONNECTOR_PUNCTUATION:
			case Character.FINAL_QUOTE_PUNCTUATION:
			case Character.INITIAL_QUOTE_PUNCTUATION:
			case Character.DASH_PUNCTUATION:
			case Character.START_PUNCTUATION:
			case Character.END_PUNCTUATION:
			case Character.OTHER_PUNCTUATION:
				// letters
			case Character.OTHER_LETTER:
			case Character.MODIFIER_LETTER:
			case Character.UPPERCASE_LETTER:
			case Character.TITLECASE_LETTER:
			case Character.LOWERCASE_LETTER:
				// digits
			case Character.DECIMAL_DIGIT_NUMBER:
				// spaces
			case Character.SPACE_SEPARATOR:
				result.append(ch);
				wasUnwanted = false;
				break;

			// line separators
			case Character.LINE_SEPARATOR:
				result.append('\n');
				wasUnwanted = true;
				break;

			// symbols
			case Character.MATH_SYMBOL:
			case Character.OTHER_SYMBOL:
				// other stuff:
			case Character.CONTROL:
			case Character.COMBINING_SPACING_MARK:
			case Character.ENCLOSING_MARK:
			case Character.FORMAT:
			case Character.LETTER_NUMBER:
			case Character.MODIFIER_SYMBOL:
			case Character.NON_SPACING_MARK:
			case Character.PARAGRAPH_SEPARATOR:
			case Character.PRIVATE_USE:
			case Character.SURROGATE:
			case Character.UNASSIGNED:
			case Character.OTHER_NUMBER:
			default:
				if (!wasUnwanted) {
					result.append('\n');
					wasUnwanted = true;
				}
			}
		}

		return result.toString();
	}

	private String removeXMLTagChars(String content) {
		String s = "<\\?xml:[^\\>\\<]*/>";
		Pattern p = Pattern.compile(s, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(content);
		return m.replaceAll("");
	}
}