package org.csr.core.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * ClassName:StringUtil.java <br/>
 * System Name：  <br/>
 * Date: 2014-2-25上午9:59:00 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 */
public class StrUtil {

	/**
	 * toUpperCaseFirstOne:首字母更换大写 <br/>
	 * 
	 * @author caijin
	 * @param name
	 * @return
	 * @since JDK 1.7
	 */
	public static String toUpperCaseFirstOne(String name) {
		char[] ch = name.toCharArray();
		if (ObjUtil.isNotEmpty(ch)) {
			ch[0] = Character.toUpperCase(ch[0]);
		}
		StringBuffer a = new StringBuffer();
		a.append(ch);
		return a.toString();
	}

	/**
	 * @description:将字符串的开头字符为patt去掉
	 * @param:
	 * @return: String
	 */
	public static String filterString(String str, String patt) {
		if (str == null) {
			return null;
		} else {
			int size = str.length();
			for (int i = 0; i < size; i++) {
				if (str.startsWith(patt)) {
					if (str.length() == 1)
						str = "";
					else
						str = str.substring(1);
				} else
					break;
			}
		}
		return str;
	}

	/**
	 * subLastString: 去掉后面的字符 <br/>
	 * 
	 * @author caijin
	 * @param root
	 * @param c
	 * @return
	 * @since JDK 1.7
	 */
	public static String subLastString(String path, char ch) {
		if (ObjUtil.isNotEmpty(path)) {
			int index = path.lastIndexOf(ch);
			if (index > 0) {
				return path.substring(0, index);
			}
		}
		return path;
	}

	/**
	 * subString: 去掉前面的全部字符串,不包含当前的的字符 <br/>
	 * 
	 * @author caijin
	 * @param fullPath
	 * @param c
	 * @since JDK 1.7
	 */
	public static String subString(String path, char ch) {
		if (ObjUtil.isNotEmpty(path)) {
			int index = path.indexOf(ch) + 1;
			int len = path.length();
			if (index > 0 && len > index) {
				return path.substring(index, len);
			}
		}
		return path;
	}

	/**
	 * @description:将冒号分隔改为逗号分隔
	 * @param:
	 * @return: String
	 */
	public static String SplitStringWithComma(String str) {
		if (str != null && !"".equals(str)) {
			String[] ids = str.split(":");
			StringBuffer strs = new StringBuffer();
			for (String id : ids) {
				if (id != null && !"".equals(id)) {
					strs.append(id).append(",");
				}
			}
			return strs.toString().substring(0, strs.toString().length() - 1);
		}
		return str;
	}

	public static String filterHtmlTag(String str, boolean flag) {
		StringBuffer sb = new StringBuffer();
		boolean isHtmlTag = false;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '〖' && str.charAt(i + 1) == '〒'
					&& str.charAt(i + 2) == '〗') {
				i = i + 2;
				continue;
			}
			if (c == '〖' && str.charAt(i + 1) == 'E'
					&& str.charAt(i + 2) == 'O' && str.charAt(i + 3) == 'F'
					&& str.charAt(i + 4) == '〗') {
				i = i + 4;
				continue;
			}
			if ((c == '\r' || c == '\n') && flag == true)
				continue;
			if (c == '&' && !isHtmlTag) {
				if ((i + 5) < str.length() && str.charAt(i + 1) == 'n'
						&& str.charAt(i + 2) == 'b' && str.charAt(i + 3) == 's'
						&& str.charAt(i + 4) == 'p' && str.charAt(i + 5) == ';') {
					sb.append(' ');
					i = i + 5;
				} else if ((i + 5) < str.length() && str.charAt(i + 1) == 'q'
						&& str.charAt(i + 2) == 'u' && str.charAt(i + 3) == 'o'
						&& str.charAt(i + 4) == 't' && str.charAt(i + 5) == ';') {
					sb.append('"');
					i = i + 5;
				} else if ((i + 4) < str.length() && str.charAt(i + 1) == 'a'
						&& str.charAt(i + 2) == 'm' && str.charAt(i + 3) == 'p'
						&& str.charAt(i + 4) == ';') {
					sb.append('&');
					i = i + 4;
				} else
					sb.append('&');
			} else {
				if (c == '<' || isHtmlTag) {
					if (c == '>')
						isHtmlTag = false;
					else
						isHtmlTag = true;
				} else {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	public static String filterHtmlTag(String str) {
		return filterHtmlTag(str, false);
	}

	static public final String lineSeperator = "\r\n";

	/**
	 * replaceCharacters: 一个从此字符串派生的字符串，它将此字符串中的所有 oldChar 替代为 newChar <br/>
	 * 
	 * @author caijin
	 * @param str
	 * @param oldChar
	 *            原字符
	 * @param newChar
	 *            新字符
	 * @return
	 * @since JDK 1.7
	 */
	public static String replaceCharacters(String str, char oldChar, char newChar) {
		return str.replace(oldChar, newChar);
	}

	public static String replace(String source, String tobereplace,
			String usetoreplace) {
		if (source == null)
			return null;
		return replace(new StringBuffer(source), tobereplace, usetoreplace);
	}

	public static String replace(StringBuffer sourceBuffer, String tobereplace,
			String usetoreplace) {
		if (tobereplace == null)
			return sourceBuffer.toString();
		if (usetoreplace == null)
			return sourceBuffer.toString();
		if (tobereplace.equals(""))
			return sourceBuffer.toString();

		StringBuffer newStr = new StringBuffer();
		String source = sourceBuffer.toString();
		int find = 0;
		int pos = 0;
		int replaceLength = tobereplace.length();
		do {
			find = source.indexOf(tobereplace, pos);
			if (find == -1)
				return newStr.append(sourceBuffer.substring(pos)).toString();
			newStr.append(sourceBuffer.substring(pos, find));
			newStr.append(usetoreplace);
			pos = find + replaceLength;
		} while (true);
	}

	public static String[] split(String str, String index) {
		if (ObjUtil.isEmpty(str)) {
			return new String[0];
		}

		int len = str.length();
		int lenIndex = index.length();
		int pos = 0;
		int find = -1;

		ArrayList<CharSequence> list = new ArrayList<CharSequence>();

		while (pos < len) {
			find = str.indexOf(index, pos);
			if (find == -1) {
				list.add(str.substring(pos));
				break;
			}
			if (find != 0 && find != pos) {
				list.add(str.subSequence(pos, find));
			}
			pos = find + lenIndex;
		}
		String[] ary = new String[list.size()];
		for (int i = 0; i < ary.length; i++) {
			ary[i] = (String) (list.get(i));
		}
		return ary;
	}

	/**
	 * Check whether the given String has actual text. More specifically,
	 * returns {@code true} if the string not {@code null}, its length is
	 * greater than 0, and it contains at least one non-whitespace character.
	 * 
	 * @param str
	 *            the String to check (may be {@code null})
	 * @return {@code true} if the String is not {@code null}, its length is
	 *         greater than 0, and it does not contain whitespace only
	 * @see #hasText(CharSequence)
	 */
	public static boolean hasText(String str) {
		return hasText((CharSequence) str);
	}

	/**
	 * Check whether the given CharSequence has actual text. More specifically,
	 * returns {@code true} if the string not {@code null}, its length is
	 * greater than 0, and it contains at least one non-whitespace character.
	 * <p>
	 * 
	 * <pre>
	 * StringUtils.hasText(null) = false
	 * StringUtils.hasText("") = false
	 * StringUtils.hasText(" ") = false
	 * StringUtils.hasText("12345") = true
	 * StringUtils.hasText(" 12345 ") = true
	 * </pre>
	 * 
	 * @param str
	 *            the CharSequence to check (may be {@code null})
	 * @return {@code true} if the CharSequence is not {@code null}, its length
	 *         is greater than 0, and it does not contain whitespace only
	 * @see Character#isWhitespace
	 */
	public static boolean hasText(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check that the given CharSequence is neither {@code null} nor of length
	 * 0. Note: Will return {@code true} for a CharSequence that purely consists
	 * of whitespace.
	 * <p>
	 * 
	 * <pre>
	 * StringUtils.hasLength(null) = false
	 * StringUtils.hasLength("") = false
	 * StringUtils.hasLength(" ") = true
	 * StringUtils.hasLength("Hello") = true
	 * </pre>
	 * 
	 * @param str
	 *            the CharSequence to check (may be {@code null})
	 * @return {@code true} if the CharSequence is not null and has length
	 * @see #hasText(String)
	 */
	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}

	/**
	 * �Ƿ��������ַ�
	 * 
	 * @param s
	 * @return
	 */
	public static boolean hasCn(String s) {
		if (s == null) {
			return false;
		}
		return countCn(s) > s.length();
	}

	/**
	 * ����ַ�������ϰ�ߡ�
	 * 
	 * @param s
	 * @param length
	 * @return
	 */
	public static String getCn(String s, int len) {
		if (s == null) {
			return s;
		}
		int sl = s.length();
		if (sl <= len) {
			return s;
		}
		// ����һ��λ�����ڡ�
		len -= 1;
		int maxCount = len * 2;
		int count = 0;
		int i = 0;
		while (count < maxCount && i < sl) {
			if (s.codePointAt(i) < 256) {
				count++;
			} else {
				count += 2;
			}
			i++;
		}
		if (count > maxCount) {
			i--;
		}
		return s.substring(0, i) + "��";
	}

	/**
	 * ����GBK������ַ���ֽ���
	 * 
	 * @param s
	 * @return
	 */
	public static int countCn(String s) {
		if (s == null) {
			return 0;
		}
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.codePointAt(i) < 256) {
				count++;
			} else {
				count += 2;
			}
		}
		return count;
	}

	/**
	 * �ı�תhtml
	 * 
	 * @param txt
	 * @return
	 */
	public static String txt2htm(String txt) {
		if (StringUtils.isBlank(txt)) {
			return txt;
		}
		StringBuilder sb = new StringBuilder((int) (txt.length() * 1.2));
		char c;
		for (int i = 0; i < txt.length(); i++) {
			c = txt.charAt(i);
			switch (c) {
			case '&':
				sb.append("&amp;");
				break;
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '"':
				sb.append("&quot;");
				break;
			case ' ':
				sb.append("&nbsp;");
				break;
			case '\n':
				sb.append("<br/>");
				break;
			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * Html2Text: 描述方法的作用 <br/>
	 * 
	 * @author caijin
	 * @param inputString
	 * @return
	 * @since JDK 1.7
	 */
	public static String html2Text(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;

		java.util.regex.Pattern p_html1;
		java.util.regex.Matcher m_html1;

		try {
			String regEx_script = "<[//s]*?script[^>]*?>[//s//S]*?<[//s]*?///[//s]*?script[//s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[//s//S]*?<///script>
			String regEx_style = "<[//s]*?style[^>]*?>[//s//S]*?<[//s]*?///[//s]*?style[//s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[//s//S]*?<///style>
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			String regEx_html1 = "<[^>]+";
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
			m_html1 = p_html1.matcher(htmlStr);
			htmlStr = m_html1.replaceAll(""); // 过滤html标签

			textStr = htmlStr;

		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}

		return textStr;
	}

	/**
	 * �滻�ַ�
	 * 
	 * @param sb
	 * @param what
	 * @param with
	 * @return
	 */
	public static StringBuilder replace(StringBuilder sb, String what,
			String with) {
		int pos = sb.indexOf(what);
		while (pos > -1) {
			sb.replace(pos, pos + what.length(), with);
			pos = sb.indexOf(what);
		}
		return sb;
	}

	/**
	 * ȫ��-->���
	 * 
	 * @param qjStr
	 * @return
	 */
	public String Q2B(String qjStr) {
		String outStr = "";
		String Tstr = "";
		byte[] b = null;
		for (int i = 0; i < qjStr.length(); i++) {
			try {
				Tstr = qjStr.substring(i, i + 1);
				b = Tstr.getBytes("unicode");
			} catch (java.io.UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (b[3] == -1) {
				b[2] = (byte) (b[2] + 32);
				b[3] = 0;
				try {
					outStr = outStr + new String(b, "unicode");
				} catch (java.io.UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else
				outStr = outStr + Tstr;
		}
		return outStr;
	}

	public static final char[] N62_CHARS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
			'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z' };
	public static final char[] N36_CHARS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
			'x', 'y', 'z' };

	private static StringBuilder longToNBuf(long l, char[] chars) {
		int upgrade = chars.length;
		StringBuilder result = new StringBuilder();
		int last;
		while (l > 0) {
			last = (int) (l % upgrade);
			result.append(chars[last]);
			l /= upgrade;
		}
		return result;
	}

	/**
	 * ������ת����N62
	 * 
	 * @param l
	 * @return
	 */
	public static String longToN62(long l) {
		return longToNBuf(l, N62_CHARS).reverse().toString();
	}

	public static String longToN36(long l) {
		return longToNBuf(l, N36_CHARS).reverse().toString();
	}

	/**
	 * ������ת����N62
	 * 
	 * @param l
	 * @param length
	 *            ��N62����length���ȣ�����0��
	 * @return
	 */
	public static String longToN62(long l, int length) {
		StringBuilder sb = longToNBuf(l, N62_CHARS);
		for (int i = sb.length(); i < length; i++) {
			sb.append('0');
		}
		return sb.reverse().toString();
	}

	public static String longToN36(long l, int length) {
		StringBuilder sb = longToNBuf(l, N36_CHARS);
		for (int i = sb.length(); i < length; i++) {
			sb.append('0');
		}
		return sb.reverse().toString();
	}

	/**
	 * N62ת��������
	 * 
	 * @param n62
	 * @return
	 */
	public static long n62ToLong(String n62) {
		return nToLong(n62, N62_CHARS);
	}

	public static long n36ToLong(String n36) {
		return nToLong(n36, N36_CHARS);
	}

	private static long nToLong(String s, char[] chars) {
		char[] nc = s.toCharArray();
		long result = 0;
		long pow = 1;
		for (int i = nc.length - 1; i >= 0; i--, pow *= chars.length) {
			int n = findNIndex(nc[i], chars);
			result += n * pow;
		}
		return result;
	}

	private static int findNIndex(char c, char[] chars) {
		for (int i = 0; i < chars.length; i++) {
			if (c == chars[i]) {
				return i;
			}
		}
		throw new RuntimeException("N62(N36)�Ƿ��ַ�" + c);
	}

	public static final Pattern SCRIPT = Pattern.compile("<script",
			Pattern.CASE_INSENSITIVE);
	public static final Pattern IFRAME = Pattern.compile("<iframe",
			Pattern.CASE_INSENSITIVE);
	public static final Pattern FRAME = Pattern.compile("<frame",
			Pattern.CASE_INSENSITIVE);
	public static final Pattern EVENT = Pattern.compile("<[^>]*['\" ]on[^>]*>",
			Pattern.CASE_INSENSITIVE);

	/**
	 * ���html�Ƿ�Ϸ�
	 * 
	 * @param html
	 * @return true���Ϸ���false�����Ϸ�
	 */
	public static boolean check(String html) {
		if (StringUtils.isBlank(html)) {
			return true;
		}
		Matcher m;
		boolean b;
		m = SCRIPT.matcher(html);
		b = m.find();
		if (!b) {
			m = IFRAME.matcher(html);
			b = m.find();
		}
		if (!b) {
			m = FRAME.matcher(html);
			b = m.find();
		}
		if (!b) {
			m = EVENT.matcher(html);
			b = m.find();
		}
		return !b;
	}

	public String[] match(String strContent) {
		// String regExp = "[!--empirenews.page--]";
		// String regExp2 = "[/!--empirenews.page--]";
		return strContent.split("[!--empirenews.page--]");
	}

	// public static void main(String[] args) {
	// String content =
	// "种上多了几分但是发生地[!--empirenews.page--]sdfdsfdsfds[/!--empirenews.page--]水电费交了多少见发生的发生的[!--empirenews.page--]s;fksdfdsfmdsf水电费交了多少见发生的发生的";
	// String[] conArr = StrUtil.split(content, "[!--empirenews.page--]");
	// for(String str:conArr){
	// String[] conArr2 = StrUtil.split(str, "[/!--empirenews.page--]");
	// if(conArr2.length>1){
	// System.out.println("标题=="+conArr2[0]);
	// System.out.println(conArr2[1]);
	// }else{
	// System.out.println(conArr2[0]);
	// }
	// }
	// }
	public static void main(String[] args) {
		StringBuffer htmlStr = new StringBuffer();
		htmlStr.append(
				"<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>")
				.append("<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en'><head><title>aaa</title><mce:script type='text/javascript'></mce:script>")
				.append("<link href='static_files/help.css' mce_href='static_files/help.css' rel='stylesheet' type='text/css' media='all' />")
				.append("</head><body><ul><li>XXXX</li></ul></body></html>");

		// System.out.println(html2Text(htmlStr.toString()));
	}

	/**
	 * 
	 * lineBreak: 文本换行 <br/>
	 * 
	 * @author liurui
	 * @param buffer
	 * @param prototype
	 * @param breakIndex
	 * @return
	 * @since JDK 1.7
	 */
	public static String lineBreak(StringBuffer buffer, String prototype,
			int breakIndex) {
		if (prototype.length() > breakIndex) {
			String begin = prototype.substring(0, breakIndex);
			buffer.append(begin + "<br/>");
			String end = prototype.substring(breakIndex);
			lineBreak(buffer, end, breakIndex);
			return buffer.toString();
		} else {
			buffer.append(prototype);
			return buffer.toString();
		}
	}

	/**
	 * 
	 * toLowerCase: 将字符串中的大写转换小写 <br/>
	 * 
	 * @param str
	 * @return
	 * @author liurui
	 * @date 2016-1-18
	 * @since JDK 1.7
	 */
	public static String toLowerCase(String str) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (Character.isUpperCase(c)) {// 如果是大写，则转换成小写。
				sb.append(Character.toLowerCase(c));
			} else {// 否则直接拼接即可。
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * toUpperCase: 将字符串中的小写转换大写 <br/>
	 * 
	 * @param str
	 * @return
	 * @author liurui
	 * @date 2016-1-22
	 * @since JDK 1.7
	 */
	public static String toUpperCase(String str) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (Character.isLowerCase(c)) {// 如果是小写，则转换成大写。
				sb.append(Character.toUpperCase(c));
			} else {// 否则直接拼接即可。
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 转码
	 * 
	 * @author caijin
	 * @param str
	 * @return
	 * @since JDK 1.7
	 */
	public static String enCodeStr(String str, String encoding) {
		try {
			if (ObjUtil.isNotBlank(str)) {
				return new String(str.getBytes("iso-8859-1"), encoding);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		return str;
	}
	
	
	public static String toUtf8String(String s) {
		char[] chars = s.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if ((c >= 0) && (c <= 255)) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}

		return sb.toString();
	}

	public static String unicode(String s) {
		char[] chars = s.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if ((c >= 0) && (c <= 255))
				sb.append(c);
			else {
				sb.append("&#" + Integer.toString(c, 10) + ";");
			}
		}
		return sb.toString();
	}

	public static String uc(String s) {
		StringBuffer sb = new StringBuffer();
		if ((s != null) && (s.length() > 0)) {
			char[] chars = s.trim().toCharArray();
			for (int i = 0; i < chars.length; i++) {
				char c = chars[i];
				if ((c >= 0) && (c <= 255))
					sb.append(c);
				else {
					sb.append("\\u" + Integer.toString(c, 16) + "");
				}
			}
		}
		return sb.toString();
	}

	public static String decUnicode(String uniString) {
		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile("&#[0-9]+;");
		Matcher m = p.matcher(uniString);
		while (m.find()) {
			String findx = m.group();
			findx = findx.substring(2, findx.length() - 1);
			int number = Integer.parseInt(findx);
			m.appendReplacement(sb, String.valueOf((char) number));
		}
		m.appendTail(sb);
		return sb.toString();
	}

	public static String conRegexForUnknown(String unknown) {
		String regex = unknown;
		String symbol = "\\$";
		for (int i = 0; i < symbol.length(); i++) {
			String ch = String.valueOf(symbol.charAt(i));
			regex = regex.replaceAll("\\" + ch, "\\\\\\" + ch);
		}
		symbol = "{}[]()^*+|?,/.";
		for (int i = 0; i < symbol.length(); i++) {
			String ch = String.valueOf(symbol.charAt(i));
			regex = regex.replaceAll("\\" + ch, "\\\\" + ch);
		}
		return regex;
	}

	/**
	 * 注：\n 回车( ) 水平制表符( ) 空格(\u0008) 换行( )
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
}
