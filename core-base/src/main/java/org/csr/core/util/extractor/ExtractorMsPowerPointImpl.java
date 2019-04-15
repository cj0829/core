/*
 * File   : $Source: ExtractorMsPowerPoint.java,v $
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

import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.SlideShow;

/**
 * Extracts the text form an MS PowerPoint document.
 * <p>
 * 
 * @author Alexander Kandzior
 * 
 * @version $Revision: 1.1.1.1 $
 * 
 * @since 6.0.0
 */
public final class ExtractorMsPowerPointImpl extends ExtractorMsOfficeAbstractBase {

	/** The buffer that is written with the content of the PPT. */
	private StringBuffer m_buffer;

	/**
	 * Hide the public constructor.
	 * <p>
	 */
	private ExtractorMsPowerPointImpl() {

		m_buffer = new StringBuffer(4096);
	}

	/**
	 * Returns an instance of this text extractor.
	 * <p>
	 * 
	 * @return an instance of this text extractor
	 */
	public static ExtractorText getExtractor() {

		// since this extractor requires a member variable we have no static
		// instance
		return new ExtractorMsPowerPointImpl();
	}

	/**
	 * @see org.ExtractorText.search.extractors.I_CmsTextExtractor#extractText(java.io.InputStream,
	 *      java.lang.String)
	 */
	public ExtractionResult extractText(InputStream in, String encoding) throws Exception {

		StringBuffer content = new StringBuffer("");
		try {
			SlideShow ss = new SlideShow(new HSLFSlideShow(in));// is
			// Ϊ�ļ���InputStream������SlideShow
			Slide[] slides = ss.getSlides();// ���ÿһ�Żõ�Ƭ
			for (int i = 0; i < slides.length; i++) {
				TextRun[] t = slides[i].getTextRuns();// Ϊ��ȡ�ûõ�Ƭ���������ݣ�����TextRun
				for (int j = 0; j < t.length; j++) {
					content.append(t[j].getText());// ����Ὣ�������ݼӵ�content��ȥ
				}
				content.append(slides[i].getTitle());
			}
		} catch (Exception ex) {

		}
		String result = content.toString();
		result = result.replaceAll("null", "\n");
		result = removeControlChars(result);
		return new ExtractionResultImpl(result);
	}

}