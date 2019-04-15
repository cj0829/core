package org.csr.core.util.io;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class IOUtil {
	public static final int BUFFER_SIZE = 4096;

	public static String stringFromStream(InputStream stream)
			throws IOException {
		return stringFromStream(stream, "UTF-8");
	}

	public static String stringFromStream(InputStream stream, String encoding)
			throws IOException {
		StringBuffer buffer = new StringBuffer();
		InputStream input = new BufferedInputStream(stream);
		Reader reader = new InputStreamReader(input, encoding);
		for (int c = reader.read(); c >= 0; c = reader.read()) {
			buffer.append((char) c);
		}
		return buffer.toString();
	}

	public static byte[] bytesFromStream(InputStream stream) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		InputStream input = new BufferedInputStream(stream);
		for (int c = input.read(); c >= 0; c = input.read()) {
			buffer.write(c);
		}
		return buffer.toByteArray();
	}

	public static String byteArrayToString(byte[] bytes) {
		int size = bytes.length;
		StringBuffer str = new StringBuffer(2 * size);

		for (int i = 0; i < size; i++) {
			short value = (short) bytes[i];

			char[] hex = new char[2];
			hex[0] = getHex((value & 0xF0) >>> 4);
			hex[1] = getHex(value & 0xF);
			str.append(hex);
		}

		return str.toString();
	}

	protected static char getHex(int val) {
		if ((val > 15) || (val < 0)) {
			throw new IllegalArgumentException(
					"Invalid number for comverting to hex character: " + val);
		}

		if (val < 10) {
			return (char) (val + 48);
		}
		val -= 10;
		return (char) (val + 65);
	}

	public static long pipe(InputStream in, OutputStream out)
			throws IOException {
		return pipe(in, out, 4096);
	}

	public static long pipe(InputStream in, OutputStream out, int bufferSize)
			throws IOException {
		long result = 0L;
		byte[] buffer = new byte[bufferSize];
		int bytesRead;
		while ((bytesRead = in.read(buffer)) != -1) {
			out.write(buffer, 0, bytesRead);
			result += bytesRead;
		}
		return result;
	}

	public static long pipe(InputStream in, String encoding, Writer out)
			throws IOException {
		return pipe(in, encoding, out, 4096);
	}

	public static long pipe(InputStream in, String encoding, Writer out,
			int bufferSize) throws IOException {
		long result = 0L;
		Reader r = new InputStreamReader(in, encoding);
		char[] buffer = new char[bufferSize];
		int charsRead;
		while ((charsRead = r.read(buffer)) != -1) {
			out.write(buffer, 0, charsRead);
			result += charsRead;
		}
		return result;
	}

	public static void closedPipe(InputStream in, File file) throws IOException {
		FileOutputStream out = new FileOutputStream(file);
		pipe(in, out, 4096);
		in.close();
		out.close();
	}

	public static String getFilenameExt(String filename) {
		if (filename == null) {
			return null;
		}

		int p = filename.lastIndexOf('.');
		if (p > 0) {
			return filename.substring(p);
		}
		return null;
	}

	public static String getFileBaseName(String filename) {
		if (filename == null) {
			return null;
		}

		int p = filename.lastIndexOf('.');
		if (p > 0) {
			return filename.substring(0, p);
		}
		return filename;
	}
}
