/*
 * File   : $Source: ExtractorMsWord.java,v $
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

import java.io.InputStream;

import org.apache.poi.hwpf.extractor.WordExtractor;

/**
 * Extracts the text form an MS Word document.
 * <p>
 * 
 * @author Alexander Kandzior
 * 
 * @version $Revision: 1.1.1.1 $
 * 
 * @since 6.0.0
 */
public final class ExtractorMsWordImpl extends ExtractorMsOfficeAbstractBase {

	/** Static member instance of the extractor. */
	private static final ExtractorMsWordImpl INSTANCE = new ExtractorMsWordImpl();

	/**
	 * Hide the public constructor.
	 * <p>
	 */
	private ExtractorMsWordImpl() {

		// noop
	}

	/**
	 * Returns an instance of this text extractor.
	 * <p>
	 * 
	 * @return an instance of this text extractor
	 */
	public static ExtractorText getExtractor() {

		return INSTANCE;
	}

	/**
	 * @see org.ExtractorText.search.extractors.I_CmsTextExtractor#extractText(java.io.InputStream,
	 *      java.lang.String)
	 */
	public ExtractionResult extractText(InputStream in, String encoding) throws Exception {

		WordExtractor wordExtractor = new WordExtractor(in);
		String result = wordExtractor.getText();
		result = removeControlChars(result);

		// WordTextExtractorFactory extractor = new WordTextExtractorFactory();
		// WordTextExtractor ex = (WordTextExtractor)
		// extractor.textExtractor(in);
		// String result = ex.getText();
		// result = removeControlChars(result);
		//
		// // now extract the meta information using POI
		// POIFSReader reader = new POIFSReader();
		// reader.registerListener(this);
		// reader.read(getStreamCopy(in));
		// Map metaInfo = extractMetaInformation();
		//
		// // free some memory
		// cleanup();

		// return the final result
		return new ExtractionResultImpl(result);
	}
}