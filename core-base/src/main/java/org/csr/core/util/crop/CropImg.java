package org.csr.core.util.crop;

import java.io.InputStream;
import java.io.Serializable;

public class CropImg implements Serializable{
	private static final long serialVersionUID = 945783652984639010L;
	
	private final InputStream file;
	private final long length;
	private final int width;
	private final int height;
	public CropImg(InputStream file, long length, int width, int height) {
		super();
		this.file = file;
		this.length = length;
		this.width=width;
		this.height=height;
	}

	public InputStream getFile() {
		return file;
	}

	public long getLength() {
		return length;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
