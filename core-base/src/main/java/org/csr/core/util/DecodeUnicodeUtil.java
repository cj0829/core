package org.csr.core.util;

/**
 * 中文和Unicode码的相互转化
 * @author Administrator
 *
 */
public class DecodeUnicodeUtil {

	 public static void main(String[] args) {   
        String s = "需要是一个合法的URL";   
        System.out.println(chDecodeUnicode(""));   
        System.out.println(unicodeDecodeCh("\u9700\u8981\u662F\u4E00\u4E2A\u5408\u6CD5\u7684URL"));
      }   
	
	/**
	 * 中文转Unicode(只转换汉子)
	 * @param s
	 * @return
	 */
	public static String chDecodeUnicode(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				sb.append("\\u" + Integer.toHexString(c));
			}
		}
		return sb.toString();
	}
	
	/**
	 * Unicode转中文
	 * @param theString
	 * @return
	 */
	public static String unicodeDecodeCh(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed   \\uxxxx   encoding.");
						}

					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else{
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();

	}
}
