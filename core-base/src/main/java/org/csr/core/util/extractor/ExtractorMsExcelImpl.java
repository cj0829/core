/*
 * File   : $Source: $
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

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Extracts the text form an MS Excel document.
 * <p>
 * 
 * @author Alexander Kandzior
 * 
 * @version $Revision: 1.1.1.1 $
 * 
 * @since 6.0.0
 */
public final class ExtractorMsExcelImpl extends ExtractorMsOfficeAbstractBase {

	/** Static member instance of the extractor. */
	private static final ExtractorMsExcelImpl INSTANCE = new ExtractorMsExcelImpl();

	/**
	 * Hide the public constructor.
	 * <p>
	 */
	private ExtractorMsExcelImpl() {

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

		// first extract the table content
		String result = extractTableContent(getStreamCopy(in));
		result = removeControlChars(result);

		// now extract the meta information using POI
		// POIFSReader reader = new POIFSReader();
		// reader.registerListener(this);
		// reader.read(getStreamCopy(in));
		// Map metaInfo = extractMetaInformation();

		// return the final result
		return new ExtractionResultImpl(result, null);
	}

	/**
	 * Extracts the text from the Excel table content.
	 * <p>
	 * 
	 * @param in the document input stream
	 * @return the extracted text
	 * @throws IOException if something goes wring
	 */
	protected String extractTableContent(InputStream in) throws IOException {

		HSSFWorkbook excelWb = new HSSFWorkbook(in);
		StringBuffer result = new StringBuffer(4096);

		int numberOfSheets = excelWb.getNumberOfSheets();

		for (int i = 0; i < numberOfSheets; i++) {
			HSSFSheet sheet = excelWb.getSheetAt(i);
			int numberOfRows = sheet.getPhysicalNumberOfRows();
			if (numberOfRows > 0) {

				if (StringUtils.isNotBlank(excelWb.getSheetName(i))) {
					// append sheet name to content
					if (i > 0) {
						result.append("\n\n");
					}
					result.append(excelWb.getSheetName(i).trim());
					result.append(":\n\n");
				}

				Iterator rowIt = sheet.rowIterator();
				while (rowIt.hasNext()) {
					HSSFRow row = (HSSFRow) rowIt.next();
					if (row != null) {
						boolean hasContent = false;
						Iterator it = row.cellIterator();
						while (it.hasNext()) {
							HSSFCell cell = (HSSFCell) it.next();
							String text = null;
							try {
								switch (cell.getCellType()) {
								case HSSFCell.CELL_TYPE_BLANK:
								case HSSFCell.CELL_TYPE_ERROR:
									// ignore all blank or error cells
									break;
								case HSSFCell.CELL_TYPE_NUMERIC:
									text = Double.toString(cell.getNumericCellValue());
									break;
								case HSSFCell.CELL_TYPE_BOOLEAN:
									text = Boolean.toString(cell.getBooleanCellValue());
									break;
								case HSSFCell.CELL_TYPE_STRING:
								default:
									text = cell.getStringCellValue();
									break;
								}
							} catch (Exception e) {
								// ignore this cell
							}
							if (StringUtils.isNotBlank(text)) {
								result.append(text.trim());
								result.append(' ');
								hasContent = true;
							}
						}
						if (hasContent) {
							// append a newline at the end of each row that has
							// content
							result.append('\n');
						}
					}
				}
			}
		}

		return result.toString();
	}

}