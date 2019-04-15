/*
 * File   : $Source: ExtractorPdf.java,v $
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

import org.apache.pdfbox.encryption.DocumentEncryption;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.util.PDFTextStripper;

/**
 * Extracts the text form a PDF document. 
 * <p>
 * 
 * @author Alexander Kandzior
 * 
 * @version $Revision: 1.1.1.1 $
 * 
 * @since 6.0.0
 */
public final class ExtractorPdfImpl extends ExtractorTextImpl {

	/** Static member instance of the extractor. */
	private static final ExtractorPdfImpl INSTANCE = new ExtractorPdfImpl();

	/**
	 * Hide the public constructor.
	 * <p>
	 */
	private ExtractorPdfImpl() {

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

		PDDocument pdfDocument = null;

		try {
			PDFParser parser = new PDFParser(in);
			parser.parse();

			pdfDocument = parser.getPDDocument();

			// check for encryption
			if (pdfDocument.isEncrypted()) {
				DocumentEncryption decryptor = new DocumentEncryption(pdfDocument);
				// try using the default password
				decryptor.decryptDocument("");
			}

			// create PDF stripper
			PDFTextStripper stripper = new PDFTextStripper();
			PDDocumentInformation info = pdfDocument.getDocumentInformation();

			// Map metaInfo = new HashMap();
			// // append document meta data to content
			// String meta;
			// meta = info.getTitle();
			// if (StringUtil.isNotEmpty(meta)) {
			// metaInfo.put(I_CmsExtractionResult.META_TITLE, meta);
			// }
			// meta = info.getKeywords();
			// if (StringUtil.isNotEmpty(meta)) {
			// metaInfo.put(I_CmsExtractionResult.META_KEYWORDS, meta);
			// }
			// meta = info.getSubject();
			// if (StringUtil.isNotEmpty(meta)) {
			// metaInfo.put(I_CmsExtractionResult.META_SUBJECT, meta);
			// }
			// // extract other available meta information
			// meta = info.getAuthor();
			// if (StringUtil.isNotEmpty(meta)) {
			// metaInfo.put(I_CmsExtractionResult.META_AUTHOR, meta);
			// }
			// meta = info.getCreator();
			// if (StringUtil.isNotEmpty(meta)) {
			// metaInfo.put(I_CmsExtractionResult.META_CREATOR, meta);
			// }
			// meta = info.getProducer();
			// if (StringUtil.isNotEmpty(meta)) {
			// metaInfo.put(I_CmsExtractionResult.META_PRODUCER, meta);
			// }
			// if (info.getCreationDate() != null) {
			// metaInfo.put(I_CmsExtractionResult.META_DATE_CREATED,
			// info.getCreationDate().getTime());
			// }
			// if (info.getModificationDate() != null) {
			// metaInfo.put(I_CmsExtractionResult.META_DATE_LASTMODIFIED,
			// info.getModificationDate().getTime());
			// }

			// add the main document text
			String result = stripper.getText(pdfDocument);

			// free some memory
			stripper = null;
			info = null;

			// return the final result
			return new ExtractionResultImpl(result, null);

		} finally {
			if (pdfDocument != null) {
				pdfDocument.close();
			}
		}
	}
}
