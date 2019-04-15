package org.csr.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.csr.core.Constants;
import org.csr.core.exception.Exceptions;

/**
 * ClassName:FileUtil.java <br/>
 * System Name：  <br/>
 * Date: 2014-2-25上午9:56:24 <br/>
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 */
public class FileUtil {

	/**
	 * clearOnechar: 清除第一个字符 <br/>
	 * @author caijin
	 * @param path
	 * @return
	 * @since JDK 1.7
	 */
	public static String clearOnechar(String path) {
		if (UrlUtil.isValidRedirectUrl(path)) {
			return StrUtil.subString(path, '/');
		}
		if (FileUtil.isFileSeparator(path)) {
			return StrUtil.subString(path, File.separatorChar);
		}
		return path;
	}
	/**
	 * clearOnechar: 清除第一个字符 <br/>
	 * 
	 * @author caijin
	 * @param path
	 * @return
	 * @since JDK 1.7
	 */
	public static String clearEndchar(String path) {
		if (path != null && path.endsWith("/")) {
			return StrUtil.subLastString(path, '/');
		}
		int s=path.length() - path.lastIndexOf(File.separatorChar);
		if (path != null && path.lastIndexOf(File.separatorChar)!=-1 && s == 1) {
			return StrUtil.subLastString(path, File.separatorChar);
		}
		return path;
	}
	
	
	public static String addNewPath(String parent, String... paths) {
		StringBuffer path=new StringBuffer();
		if(ObjUtil.isNotBlank(parent)){
			path.append(File.separator).append(FileUtil.clearEndchar(FileUtil.clearOnechar(parent)));
		}
		if(ObjUtil.isNotEmpty(paths)){
			for (String p : paths) {
				if(ObjUtil.isNotBlank(p)){
					path.append(File.separator).append(FileUtil.clearEndchar(FileUtil.clearOnechar(p)));
				}
			}
		}
		return path.toString() ;
	}
	/**
	 * createDateDir: 创建一个时间文件夹的名称，没有创建文件夹的动作 <br/>
	 * 文件夹名称带/符合
	 * @author caijin
	 * @param rootPath
	 * @return
	 * @since JDK 1.7
	 */
	public static String createDateDir(){
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd"); 
		String dateStr = sdf.format(date); 
		return File.separator+dateStr;
	}
	/**
	 * 将上传的文件进行重命名
	 * 
	 * @param name
	 * @return
	 */
	public static String rename(String name, Date date) {
		Long now = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(date));
		Long random = (long) (Math.random() * now);
		String fileName = now + "" + random;
		if (name.indexOf(".") != -1) {
			fileName += name.substring(name.lastIndexOf("."));
		}
		return fileName;
	}

	/**
	 * 将上传的文件进行重命名
	 * 
	 * @param name
	 * @return
	 */
	public static String reNameasUUID(String name) {
		String fileName = UUID.randomUUID().toString();
		if (name.indexOf(".") != -1) {
			fileName += name.substring(name.lastIndexOf("."));
		}
		return fileName;
	}
	/**
	 * isFileSeparator: 判断字符串首字符是否为\\ <br/>
	 * 
	 * @author caijin
	 * @param filePath
	 * @return
	 * @since JDK 1.7
	 */
	public static boolean isFileSeparator(String filePath) {
		if (ObjUtil.isNotBlank(filePath)) {
			if (filePath.indexOf(File.separatorChar) == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取 web工程服务名称/root 根服务名称
	 * 
	 * @return
	 */
	public static String webRootPath() {
		String path = getClassRootPath();
		int index = path.lastIndexOf("WEB-INF");
		if (index > 0) {
			return path.substring(0, index);
		}
		return path;
	}
	/**
	 * 获取 web工程服务名称/root 根服务名称
	 * 
	 * @return
	 */
	public static String webRootName() {
		String rootPath = FileUtil.getClassRootPath();
		int index = rootPath.lastIndexOf("WEB-INF");
		if (index > 0) {
			rootPath = cutLastString(rootPath, File.separator, 2);
			int begin = rootPath.lastIndexOf(File.separator);
			return rootPath.substring(begin, rootPath.length());
		}else{
			rootPath = cutLastString(rootPath, File.separator, 1);
			int begin = rootPath.lastIndexOf(File.separator);
			return rootPath.substring(begin, rootPath.length());
		}
	}
	/**
	 * getClassPath: 获取class的根目录路径 <br/>
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	public static String getClassRootPath(){
		return getPropertiesFileByName("/").getAbsolutePath();
	}

	public static String addHtmlRooPath(String path) {
		String newAllPath= FileUtil.addNewPath(Constants.HtmlRootPath,path);
		return FileUtil.toHtmlPath(newAllPath);
	}
	
	/**
	 * description:获取文件路径
	 */
	public static File getPropertiesFileByName(String name) {
		File licenseFile = null;
		ClassLoader clslLoader = FileUtil.class.getClassLoader();
		URL url = clslLoader.getResource(name);
		if (null == url) {
			url = ClassLoader.getSystemResource(File.separator);
		}
		try {
			licenseFile = new File(url.toURI());
		} catch (Exception e) {
			e.printStackTrace();
			licenseFile = new File(url.getFile());
		}
		return licenseFile;
	}

	/**
	 * toRoot: 转化为html路径方式，防止火狐浏览器<br/>
	 * 
	 * @author caijin
	 * @param fullPath
	 * @return
	 * @since JDK 1.7
	 */
	public static String toHtmlPath(String path) {
		if (ObjUtil.isNotEmpty(path)) {
			return path.replace(File.separatorChar, '/');
		}
		return path;
	}

	/**
	 * cutLastString: 剪切文件从右边开始 <br/>
	 * 
	 * @author caijin
	 * @param source
	 * @param dest
	 * @param num
	 * @return
	 * @since JDK 1.7
	 */
	public static String cutLastString(String source, String dest, int num) {
		for (int i = 0; i < num; i++) {
			int index=source.lastIndexOf(dest, source.length());
			if(index>=0){
				source = source.substring(0,index);
			}
		}
		return source;
	}

	/**
	 * cutRigtString: 剪切文件从保存右边 <br/>
	 * 
	 * @author caijin
	 * @param source
	 * @param dest
	 * @param num
	 * @return
	 * @since JDK 1.7
	 */
	public static String cutRigtString(String source, String dest, int num) {
		for (int i = 0; i < num; i++) {
			int index=source.lastIndexOf(dest);
			if(index>=0){
				source = source.substring(index+1,source.length());
			}
		}
		return source;
	}

	/**
	 * 创建文件夹
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static boolean createFile(File file) throws IOException {
		if (!file.exists()) {
			makeDir(file);
		}
		return file.mkdir();
	}

	/**
	 * 创建多级文件夹
	 */
	public static void makeDir(File dir) {
		if (!dir.getParentFile().exists()) {
			makeDir(dir.getParentFile());
		}
		dir.mkdir();
	}

	/**
	 * @description: 删除目录（文件夹）以及目录下的文件
	 * @param sPath
	 *            :被删除目录的文件路径
	 */
	public static void deleteDirectory(String sPath) {
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return;
		}
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				files[i].delete();
			} else { // 删除子目录
				deleteDirectory(files[i].getAbsolutePath());
			}
		}
		dirFile.delete();
	}

	/**
	 * copy: 复制文件 <br/>
	 * @author caijin
	 * @param outputFile
	 * @param file
	 * @throws IOException 
	 * @throws Exception
	 * @since JDK 1.7
	 */
	public static void copy(File outputFile, File file) throws IOException{
		if (outputFile != null) {
			/*
			 * The uploaded file is being stored on disk in a temporary location
			 * so move it to the desired file.
			 */
			if (!outputFile.renameTo(file)) {
				BufferedInputStream in = null;
				BufferedOutputStream out = null;
				try {
					in = new BufferedInputStream(new FileInputStream(outputFile));
					out = new BufferedOutputStream(new FileOutputStream(file));
					IOUtils.copy(in, out);
				} finally {
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
						}
					}
					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {
							// ignore
						}
					}
				}
			}
		} else {
			Exceptions.service("", "Cannot write uploaded file to disk!");
		}
	}

	/**
	 * copy: 复制文件 <br/>
	 * @author caijin
	 * @param outputFile
	 * @param file
	 * @throws IOException 
	 * @throws Exception
	 * @since JDK 1.7
	 */
	public static void copy(InputStream in, OutputStream out) throws IOException {
		if (in != null && out != null) {
			BufferedInputStream inBuf = null;
			BufferedOutputStream outBuf = null;
			try {
				inBuf = new BufferedInputStream(in);
				outBuf = new BufferedOutputStream(out);
				IOUtils.copy(inBuf, outBuf);
			} finally {
				if (inBuf != null) {
					try {
						inBuf.close();
					} catch (IOException e) {
					}
				}
				if (outBuf != null) {
					try {
						outBuf.close();
					} catch (IOException e) {
					}
				}
			}
		} else {
			Exceptions.service("", "Cannot write uploaded file to disk!");
		}
	}
	
	private static int buffSize = 32 * 1024;
	/**
	 * Unzip files from stream into target dir and do NOTHING ELSE!!! See OLAT-6213
	 * 
	 * @param is, stream from zip archive
	 * @param outdir, path to output directory, relative to cwd or absolute
	 */
	public static void unzip(InputStream is, String outdir) throws IOException {

		byte[] buffer = new byte[buffSize];

		java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream(new BufferedInputStream(is));

		java.util.zip.ZipEntry entry;

		try {
			while ((entry = zis.getNextEntry()) != null) {

				File of = new File(outdir + File.separator + entry.getName());

				if (entry.isDirectory()) {
					of.mkdirs();
					continue;
				} else {
					File xx = new File(of.getParent());
					if (!xx.exists()) {
						Stack<String> todo = new Stack<String>();
						do {
							todo.push(xx.getAbsolutePath());
							xx = new File(xx.getParent());
						} while (!xx.exists());
						while (todo.size() > 0) {
							xx = new File(todo.pop());
							if (!xx.exists()) {
								xx.mkdirs();
							}
						}
					}
				}

				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(of), buffer.length);

				cpio(new BufferedInputStream(zis), bos, "unzip:" + entry.getName());

				bos.flush();
				bos.close();
			}
		} catch (IllegalArgumentException e) {
			// problem with chars in entry name likely
		}
		zis.close();

	}
	
	/**
	 * copy in, copy out (leaves both streams open)
	 * <p>
	 * 
	 * @see FileUtils.getBos() which creates a matching BufferedOutputStream
	 *      </p>
	 * @param in BuferedInputStream
	 * @param out BufferedOutputStream
	 * @param wt What this I/O is about
	 */
	private static long cpio(BufferedInputStream in, BufferedOutputStream out, String wt) throws IOException {

		byte[] buffer = new byte[buffSize];

		int c;
		long tot = 0;
		long s = System.nanoTime();
		while ((c = in.read(buffer, 0, buffer.length)) != -1) {
			out.write(buffer, 0, c);
			tot += c;
		}

		long tim = System.nanoTime() - s;
		double dtim = tim == 0 ? 0.5 : tim; // avg of those less than 1 nanoseconds is taken as 0.5 nanoseconds
		double bps = tot * 1000 * 1000 / dtim;
		System.out.println(bps);
		return tot;
	}
	
	
	public static void copyDirectiory(String fromDir, String toDir)
			throws IOException {
		new File(toDir).mkdirs();
		File[] file = new File(fromDir).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				FileInputStream input = new FileInputStream(file[i]);
				FileOutputStream output = new FileOutputStream(toDir
						+ File.separator + file[i].getName());
				byte[] b = new byte[4096];
				int len;
				while ((len = input.read(b)) != -1) {
					output.write(b, 0, len);
				}
				output.flush();
				output.close();
				input.close();
			}
			if (file[i].isDirectory())
				copyDirectiory(fromDir + File.separator + file[i].getName(),toDir + File.separator + file[i].getName());
		}
	}

	/**
	 * 反格式化byte
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] hex2byte(String s) {
		byte[] src = s.toLowerCase().getBytes();
		byte[] ret = new byte[src.length / 2];
		for (int i = 0; i < src.length; i += 2) {
			byte hi = src[i];
			byte low = src[i + 1];
			hi = (byte) ((hi >= 'a' && hi <= 'f') ? 0x0a + (hi - 'a') : hi - '0');
			low = (byte) ((low >= 'a' && low <= 'f') ? 0x0a + (low - 'a') : low - '0');
			ret[i / 2] = (byte) (hi << 4 | low);
		}
		return ret;
	}
	
	/**
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] out = new char[b.length * 2];
		for (int i = 0; i < b.length; i++) {
			byte c = b[i];
			out[i * 2] = Digit[(c >>> 4) & 0X0F];
			out[i * 2 + 1] = Digit[c & 0X0F];
		}
		return new String(out);
	}
	
//	public static void copyDirectiory(String fromDir, String toDir,ZipOutputter outer) throws IOException {
//		File zipFile = new File(fromDir);
//		ZipUtil.zipFileDirectory(zipFile, toDir, outer);
//	}

	public static void main(String[] args) throws IOException {
		System.out.println(rename("adsf.flv", new Date()));
		System.out.println(cutLastString("adsf.flv", ".", 1));
		File file=new File("C:\\Users\\caijin\\Desktop\\scom\\SingleCourse\\SingleCourseEx.zip");
		
		FileInputStream fileIn=new FileInputStream(file);
		FileUtil.unzip(fileIn, "C:\\Users\\caijin\\Desktop\\scom\\SingleCourse\\dre");
	}
	

}
