package org.csr.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author caijin
 * 
 */
public class Zipper {

	private static final Logger log = LoggerFactory.getLogger(Zipper.class);
	
	public static String zipString(String str) {
		try {
			ByteArrayOutputStream bos = null;
			GZIPOutputStream os = null;
			byte[] bs = null;
			try {
				bos = new ByteArrayOutputStream();
				os = new GZIPOutputStream(bos);
				os.write(str.getBytes());
				os.close();
				bos.close();
				bs = bos.toByteArray();
				return new String(bs, "iso-8859-1");
			} finally {
				bs = null;
				bos = null;
				os = null;
			}
		} catch (Exception ex) {
			return str;
		}
	}

	public static String unzipString(String str) {
		ByteArrayInputStream bis = null;
		ByteArrayOutputStream bos = null;
		GZIPInputStream is = null;
		byte[] buf = null;
		try {
			bis = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
			bos = new ByteArrayOutputStream();
			is = new GZIPInputStream(bis);
			buf = new byte[1024];
			int len;
			while ((len = is.read(buf)) != -1) {
				bos.write(buf, 0, len);
			}
			is.close();
			bis.close();
			bos.close();
			return new String(bos.toByteArray());
		} catch (Exception ex) {
			return str;
		} finally {
			bis = null;
			bos = null;
			is = null;
			buf = null;
		}
	}

	/**
	 * 
	 */

	public static void zip(OutputStream out, List<FileEntry> fileEntrys) {
		new Zipper(out, fileEntrys);
	}

	protected Zipper(OutputStream out, List<FileEntry> fileEntrys) {
		// Assert.notEmpty(fileEntrys);
		long begin = System.currentTimeMillis();
		try {
			try {
				zipOut = new ZipOutputStream(out);
				for (FileEntry fe : fileEntrys) {
					zip(fe.getFile(), fe.getFilter(), fe.getZipEntry(),
							fe.getPrefix());
				}
			} finally {
				zipOut.close();
			}
		} catch (IOException e) {
			throw new RuntimeException("����ѹ����ʱ������IO�쳣��", e);
		}
		long end = System.currentTimeMillis();
		log.info("����ѹ����ɹ�����ʱ��{}ms��" + String.valueOf(end - begin));
	}

	/**
	 * ѹ���ļ�
	 * 
	 * @param srcFile
	 *            Դ�ļ�
	 * @param pentry
	 *            ��ZipEntry
	 * @throws IOException
	 */
	private void zip(File srcFile, FilenameFilter filter, ZipEntry pentry,
			String prefix) throws IOException {
		ZipEntry entry;
		if (srcFile.isDirectory()) {
			if (pentry == null) {
				entry = new ZipEntry(srcFile.getName());
			} else {
				entry = new ZipEntry(pentry.getName() + "/" + srcFile.getName());
			}
			File[] files = srcFile.listFiles(filter);
			for (File f : files) {
				zip(f, filter, entry, prefix);
			}
		} else {
			if (pentry == null) {
				entry = new ZipEntry(prefix + srcFile.getName());
			} else {
				entry = new ZipEntry(pentry.getName() + "/" + prefix
						+ srcFile.getName());
			}
			FileInputStream in;
			try {
				log.debug("��ȡ�ļ���{}" + srcFile.getAbsolutePath());
				in = new FileInputStream(srcFile);
				try {
					zipOut.putNextEntry(entry);
					int len;
					while ((len = in.read(buf)) > 0) {
						zipOut.write(buf, 0, len);
					}
					zipOut.closeEntry();
				} finally {
					in.close();
				}
			} catch (FileNotFoundException e) {
				throw new RuntimeException("����ѹ����ʱ��Դ�ļ������ڣ�"
						+ srcFile.getAbsolutePath(), e);
			}
		}
	}

	private byte[] buf = new byte[1024];
	private ZipOutputStream zipOut;

	public static class FileEntry {
		private FilenameFilter filter;
		private String parent;
		private File file;
		private String prefix;

		public FileEntry(String parent, String prefix, File file,
				FilenameFilter filter) {
			this.parent = parent;
			this.prefix = prefix;
			this.file = file;
			this.filter = filter;
		}

		public FileEntry(String parent, File file) {
			this.parent = parent;
			this.file = file;
		}

		public FileEntry(String parent, String prefix, File file) {
			this(parent, prefix, file, null);
		}

		public ZipEntry getZipEntry() {
			if (StringUtils.isBlank(parent)) {
				return null;
			} else {
				return new ZipEntry(parent);
			}
		}

		public FilenameFilter getFilter() {
			return filter;
		}

		public void setFilter(FilenameFilter filter) {
			this.filter = filter;
		}

		public String getParent() {
			return parent;
		}

		public void setParent(String parent) {
			this.parent = parent;
		}

		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
		}

		public String getPrefix() {
			if (prefix == null) {
				return "";
			} else {
				return prefix;
			}
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}
	}
}
