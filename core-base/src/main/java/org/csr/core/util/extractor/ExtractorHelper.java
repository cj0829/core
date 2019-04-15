/*
 * Copyright (c) 2013, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package org.csr.core.util.extractor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

import org.csr.core.Constants;
import org.csr.core.util.ObjUtil;
import org.csr.core.util.StrUtil;

/**
 * Defined class file the ExtractorHelper.java
 *
 *
 * @author Rock.Lee
 * @version elearning 1.0
 * @since JDK-1.6.0
 * @date 2013-6-14����11:37:27
 */
public class ExtractorHelper {
	public static final String TEXT_TYPE = "text/plain";
	// pdf type
	public static final String PDF_TYPE = "application/pdf";
	// word type
	public static final String MSWORD_TYPE = "application/msword";
	// html type
	public static final String HTML_TYPE = "text/html";
	public static final String HTML_JSEDIT_TYPE = "text/html;ezio=1";
	// excel type
	public static final String XLS_TYPE_1 = "application/vnd.ms-excel";
	public static final String XLS_TYPE_2 = "application/x-msexcel";
	// powerpoint type
	public static final String POT_PPO_PPS_TYPE = "application/vnd.ms-powerpoint";
	public static final String PPT_TYPE = "application/x-mspowerpoint";
	public static final String PWZ_TYPE = "application/vnd.ms-powerpoint";

	/**
	 * check specified contentType if can be extract
	 * 
	 * @param contentType
	 * @return can extract return true, else false;
	 */
	public static boolean checkExtract(String contentType) {
		String[] textTypes = new String[] { TEXT_TYPE, PDF_TYPE, MSWORD_TYPE, HTML_TYPE, HTML_JSEDIT_TYPE, XLS_TYPE_1, XLS_TYPE_2, POT_PPO_PPS_TYPE, PPT_TYPE, PWZ_TYPE };
		return Arrays.asList(textTypes).contains(contentType);
	}

	public static String getSummary(InputStream in, String contentType, String encoding) {
		return getSummary(in, contentType, encoding, 20);
	}

	public static String getSummary(InputStream in, String contentType, String encoding, int length) {
		try {
			ExtractionResult extraction = extractorContent(in, contentType, encoding);
			return getSubExtract(extraction, length);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String getSummary(String content, int length) {
		if (ObjUtil.isBlank(content)) {
			return null;
		}
		InputStream contentStream = null;
		try {
			contentStream = new ByteArrayInputStream(content.getBytes(Constants.UTF8_ENCODING));
			return getSummary(contentStream, HTML_JSEDIT_TYPE, Constants.UTF8_ENCODING, length);

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (contentStream != null) {
				try {
					contentStream.close();
					contentStream = null;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	private static ExtractionResult extractorContent(InputStream in, String contentType, String encoding) {
		try {
			if (contentType.equals(TEXT_TYPE)) {
				return ExtractorTextImpl.getExtractor().extractText(in, encoding);
			} else if (contentType.equals(PDF_TYPE)) {
				return ExtractorPdfImpl.getExtractor().extractText(in);
			} else if (contentType.equals(MSWORD_TYPE)) {
				return ExtractorMsWordImpl.getExtractor().extractText(in);
			} else if (contentType.equals(HTML_TYPE)) {
				return ExtractorHtmlImpl.getExtractor().extractText(in, encoding);
			} else if (contentType.equals(HTML_JSEDIT_TYPE)) {
				return ExtractorHtmlImpl.getExtractor().extractText(in);
			} else if (contentType.equals(XLS_TYPE_1) || contentType.equals(XLS_TYPE_2)) {
				return ExtractorMsExcelImpl.getExtractor().extractText(in);
			} else if (contentType.equals(POT_PPO_PPS_TYPE) || contentType.equals(PPT_TYPE) || contentType.equals(PWZ_TYPE)) {
				return ExtractorMsPowerPointImpl.getExtractor().extractText(in);
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	private static String getSubExtract(ExtractionResult extraction, int length) {
		String result = "";
		if (extraction != null) {
			String content = StrUtil.decUnicode(extraction.getContent().trim());
			if (length > 0 && content.length() > length) {
				result = content.substring(0, length);
			} else {
				result = content;
			}
			extraction.release();
		}
		return result;
	}

	public static String tidyHtmlContent(String content) {
		if (ObjUtil.isBlank(content)) {
			return content;
		}
		String lowcase = content.toLowerCase();
		// start 2010-12-20.���EXCEL�ؼ��ڵ��ַ����html,body������
		// ��sitemesh��ͬ�����г�ȡ���ˣ�Ϊ�˱��⣬��Ҫ����Щ�ַ����⴦�?
		String classid = "clsid:0002e551-0000-0000-c000-000000000046";
		int findClsidPos = lowcase.indexOf(classid);
		while (findClsidPos != -1) {
			// �������ҵ������classid����������ѭ����
			int objStartPos = lowcase.lastIndexOf("<object ", findClsidPos);
			int objEndPos = lowcase.indexOf("</object>", findClsidPos);
			if (objStartPos == -1 || objEndPos == -1) {
				break;
			}
			String objString = content.substring(objStartPos, objEndPos + 9);
			objString = objString.replaceAll("<html", "&lt;html");
			objString = objString.replaceAll("</html>", "&lt;/html&gt;");
			objString = objString.replaceAll("<body", "&lt;body");
			objString = objString.replaceAll("</body>", "&lt;/body&gt;");
			// objString = objString.replaceAll("<style", "&lt;style");
			// objString = objString.replaceAll("</style>", "&lt;/style&gt;");
			content = content.substring(0, objStartPos) + objString + content.substring(objEndPos + 9);
			findClsidPos = lowcase.indexOf(classid, findClsidPos + 100);
		}// end 2010-12-20

		int start = lowcase.indexOf("<!doctype");
		int end = -1;
		if (start != -1) {
			end = lowcase.indexOf('>', start + 9);
			content = content.substring(0, start) + content.substring(end + 1);
			lowcase = content.toLowerCase();
		}
		start = lowcase.indexOf("<html");
		if (start != -1) {
			end = lowcase.indexOf('>', start + 5);
			content = content.substring(0, start) + content.substring(end + 1);
			lowcase = content.toLowerCase();
		}
		start = lowcase.indexOf("<title>");
		if (start != -1) {
			end = lowcase.indexOf("</title>", start + 7);
			if (end == -1) {
				return content.substring(0, start);
			} else {
				content = content.substring(0, start) + content.substring(end + 8);
				lowcase = content.toLowerCase();
			}
		}
		start = lowcase.indexOf("<style>");
		if (start != -1) {
			end = lowcase.indexOf("</style>", start + 7);
			if (end == -1) {
				return content.substring(0, start);
			} else {
				content = content.substring(0, start) + content.substring(end + 8);
				lowcase = content.toLowerCase();
			}
		}
		start = lowcase.indexOf("<body");
		if (start != -1) {
			content = content.substring(0, start) + "<span" + content.substring(start + 5);
			end = lowcase.lastIndexOf("</body>");
			if (end != -1) {
				content = content.substring(0, end) + "</span>" + content.substring(end + 7);
			}
		}
		start = lowcase.lastIndexOf("</html>");
		if (start != -1) {
			content = content.substring(0, start) + content.substring(start + 7);
		}

		content = content.replaceAll("\"stream\\?key=", "\"../portal/blob\\?key=");
		content = content.replaceAll("'stream\\?key=", "\'../portal/blob\\?key=");

		content = content.replaceAll("\"res\\?key=", "\"../portal/res\\?key=");
		content = content.replaceAll("'res\\?key=", "\'../portal/res\\?key=");

		lowcase = null;

		return content;
	}
}
