package org.csr.core.util.mm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.csr.core.util.ObjUtil;
import org.csr.core.util.crop.CodeType;

/**
 * ClassName:org.csr.media.constant.FileType.java <br/>
 * System Name： 流媒体转码系统 <br/>
 * Date: 2014年4月27日上午1:27:36 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 */
public class MimeTypeTool {

	private final static HashMap<String, String> mFileTypes = new HashMap<String, String>();
	private static int MIME = 0;
	/**
	 * Discription:[getAllFileType,常见文件头信息] asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv
	 */
	static {
		
		mFileTypes.put("ffd8fffe00104c617663", "jpg");
		mFileTypes.put("ffd8ffe000104a464946", "jpg");
		mFileTypes.put("89504e470d0a1a0a0000", "png");
		mFileTypes.put("47494638396126026f01", "gif");
		mFileTypes.put("49492a00227105008037", "tif");
		mFileTypes.put("424d228c010000000000", "bmp"); // 16色位图(bmp)
		mFileTypes.put("424d8240090000000000", "bmp"); // 24位位图(bmp)
		mFileTypes.put("424d8e1b030000000000", "bmp"); // 256色位图(bmp)
		mFileTypes.put("41433130313500000000", "dwg");
		mFileTypes.put("3c21444f435459504520", "html");
		mFileTypes.put("3c21646f637479706520", "htm");
		mFileTypes.put("48544d4c207b0d0a0942", "css");
		mFileTypes.put("696b2e71623d696b2e71", "js");
		
		mFileTypes.put("66666d70656720766572", "txt");
		mFileTypes.put("63726561746520746162", "txt");
		mFileTypes.put("7b5c727466315c616e73", "rtf");
		mFileTypes.put("38425053000100000000", "psd");
		mFileTypes.put("46726f6d3a203d3f6762", "eml");
		mFileTypes.put("d0cf11e0a1b11ae10000", "doc"); // MS Excel、Word、Msi
		mFileTypes.put("d0cf11e0a1b11ae10000", "vsd");
		mFileTypes.put("5374616E64617264204A", "mdb");
		mFileTypes.put("255044462d312e350d0a", "pdf");
		mFileTypes.put("2e524d46000000120001", "rmvb"); // rmvb、rm
		mFileTypes.put("464c5601050000000900", "flv"); // flv、f4v
		mFileTypes.put("00000020667479706d70", "mp4");
		mFileTypes.put("0000001c667479706973", "mp4");
		mFileTypes.put("00000018667479706d70", "mp4");
		mFileTypes.put("00000014667479707174", "mov");
		mFileTypes.put("49443303000000002176", "mp3");
		mFileTypes.put("000001ba210001000180", "mpg");
		mFileTypes.put("3026b2758e66cf11a6d9", "wmv"); // wmv、asf
		mFileTypes.put("52494646e27807005741", "wav");
		mFileTypes.put("52494646d07d60074156", "avi");
		mFileTypes.put("4d546864000000060001", "mid");
		mFileTypes.put("504b0304140000000800", "zip");
		mFileTypes.put("526172211a0700cf9073", "rar");
		mFileTypes.put("235468697320636f6e66", "ini");
		mFileTypes.put("504b03040a0000000000", "jar");
		mFileTypes.put("4d5a9000030000000400", "exe");
		mFileTypes.put("3c25402070616765206c", "jsp");
		mFileTypes.put("4d616e69666573742d56", "mf");
		mFileTypes.put("3c3f786d6c2076657273", "xml");
		mFileTypes.put("494e5345525420494e54", "sql");
		mFileTypes.put("7061636b616765207765", "java");
		mFileTypes.put("406563686f206f66660d", "bat");
		mFileTypes.put("1f8b0800000000000000", "gz");
		mFileTypes.put("6c6f67346a2e726f6f74", "properties");
		mFileTypes.put("cafebabe0000002e0041", "class");
		mFileTypes.put("49545346030000006000", "chm");
		mFileTypes.put("04000000010000001300", "mxp");
		mFileTypes.put("504b0304140006000800", "docx");
		mFileTypes.put("d0cf11e0a1b11ae10000", "wps");// WPS(wps、et、dps)
		mFileTypes.put("6431303a637265617465", "torrent");
		mFileTypes.put("6d6f6f76", "mov");
		mFileTypes.put("ff575043", "wpd");
		mFileTypes.put("cfad12fec5fd746f", "dbx");
		mFileTypes.put("2142444E", "pst");
		mFileTypes.put("ac9ebd8f", "qdf");
		mFileTypes.put("e3828596", "pwl");
		mFileTypes.put("2e7261fd", "ram");
		mFileTypes.put("66747970", "mp4");
		mFileTypes.put("ffd8ff", "jpg");
		
	}

	/**
	 * getFileType: 判断文件是否需要转码<br/>
	 * 
	 * @author caijin
	 * @param file
	 * @return
	 * @since JDK 1.7
	 */
	public static int getFileCodeType(File file) {
		try {
			String fileHead = getFileContent(file.getPath());
			String typeName = mFileTypes.get(fileHead);
			if (ObjUtil.isBlank(typeName)) {
				String subHead = StringUtils.substring(fileHead, 8, 16);
				typeName = mFileTypes.get(subHead);
				if (ObjUtil.isBlank(typeName)) {
					String jpgHead = StringUtils.substring(fileHead, 0, 6);
					typeName = mFileTypes.get(jpgHead);
					if (ObjUtil.isBlank(typeName)) {
						return CodeType.OTHER;
					}
				}
			}
			if ("mov".equals(typeName) || "mp4".equals(typeName)
					|| "rmvb".equals(typeName) || "mpg".equals(typeName)
					|| "wmv".equals(typeName) || "wav".equals(typeName)
					|| "avi".equals(typeName)) {
				return CodeType.CODING;
			} else if ("flv".equals(typeName)) {
				return CodeType.FLV;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CodeType.OTHER;
	}

	/**
	 * 判断文件类型
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 文件类型
	 */
	public static String getType(File file){
		try {
			return getType(file.getPath());
		} catch (IOException e) {
		}
		return "";
	}

	public static String getMimeType(File file) {
		MimeUtil.registerMimeDetector("org.csr.core.util.mm.detector.ExtensionMimeDetector");
		Collection<?> mimeTypes = MimeUtil.getMimeTypes(file);
		MimeType mineType = (MimeType) mimeTypes.iterator().next();
		return mineType.toString();
	}

	public static String getMimeType(String filePath) {
		MimeUtil.registerMimeDetector("org.csr.core.util.mm.detector.ExtensionMimeDetector");
		Collection<?> mimeTypes = MimeUtil.getMimeTypes(filePath);
		MimeType mineType = (MimeType) mimeTypes.iterator().next();
		return mineType.toString();
	}
	
	public static String getExtension(String filePath) {
		MimeUtil.registerMimeDetector("org.csr.core.util.mm.detector.ExtensionMimeDetector");
		return MimeUtil.getExtension(filePath);
	}
	
	public static String getExtension(File file) {
		MimeUtil.registerMimeDetector("org.csr.core.util.mm.detector.ExtensionMimeDetector");
		return MimeUtil.getExtension(file);
	}
	
	/**
	 * 判断文件类型
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 文件类型
	 */
	public static String getType(String filePath) throws IOException {
		String fileHead = getFileContent(filePath);
		String typeName = mFileTypes.get(fileHead);
		if (ObjUtil.isBlank(typeName)) {
			String subHead = StringUtils.substring(fileHead, 8, 16);
			typeName = mFileTypes.get(subHead);
			if (ObjUtil.isBlank(typeName)) {
				String jpgHead = StringUtils.substring(fileHead, 0, 6);
				typeName = mFileTypes.get(jpgHead);
				if (ObjUtil.isBlank(typeName)) {
					return "";
				}
			}
		}
		return typeName;
	}
	/**
	 * 得到文件头
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 文件头
	 * @throws IOException
	 */
	private static String getFileContent(String filePath){
		byte[] b = new byte[10];
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(filePath);
			inputStream.read(b, 0, 10);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bytesToHexString(b);
	}

	private static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF; // java中byte转换int时与0xff进行与运算 ?
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static void main(String[] args) throws Exception {

		// File f = new File("C:\\Users\\caijin\\Desktop\\IMG_0350.mov");
		// File f = new File("C:\\Users\\caijin\\Desktop\\新建文件夹 (2)\\张季01.mp4");
		File f = new File("C:\\Users\\caijin\\Desktop\\下载.jpg");
		System.out.println(f.getAbsoluteFile());
		System.out.println(f.getParent());
		System.out.println(getMimeType(f));
		System.out.println(getType(f));
		System.out.println(getExtension(f.getPath()));
		System.out.println(MimeUtil.getMediaType(getType(f)));
//		int df = getFileType(f);
		System.out.println(getFileCodeType(f));

	}


}