/**
 * Project Name:FileServicesSystem_1.1
 * File Name:ImageCropService.java
 * Package Name:org.csr.crop.service
 * Date:Jun 23, 20145:14:47 PM
 * Copyright (c) 2014, 版权所有 ,All rights reserved 
 */

package org.csr.core.util.crop;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * ClassName: ImageCropService.java <br/>
 * System Name： 文件上传系统 <br/>
 * Date: Jun 23, 20145:14:47 PM <br/>
 * 
 * @author yjY <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 * 
 */
public class ImageCropUtil {

	public static void imageCrop(File fileSrc, File fileDest, int width, int height, boolean isAdjustSize) throws Exception {
		CropImg stream = ImageTool.zoomPictureStream(fileSrc, width,height, isAdjustSize);
		BufferedImage bi = ImageIO.read(stream.getFile());
		ImageIO.write(bi, "jpg", new File(fileDest.toString()));
	}

	public static CropImg imageCrop(File fileSrc, int width, int height, boolean isAdjustSize) throws Exception {
		return ImageTool.zoomPictureStream(fileSrc, width, height, isAdjustSize);
	}

	public static CropImg imageCrop(InputStream fileSrc, int width, int height, boolean isAdjustSize) throws Exception {
		return ImageTool.zoomPictureStream(fileSrc, width, height, isAdjustSize);
	}

	public static File saveImageAfterCrop(File fileSrc, File fileDest, int top, int left, int width, int height) throws IOException {
		BufferedImage bi = (BufferedImage) ImageIO.read(fileSrc);

		height = Math.min(height, bi.getHeight());
		width = Math.min(width, bi.getWidth());
		if (height <= 0)
			height = bi.getHeight();
		if (width <= 0)
			width = bi.getWidth();
		top = Math.min(Math.max(0, top), bi.getHeight() - height);
		left = Math.min(Math.max(0, left), bi.getWidth() - width);

		BufferedImage bi_cropper = bi.getSubimage(left, top, width, height);
		bi_cropper.flush();

		ImageIO.write(bi_cropper, "png", new File(fileDest.toString()));

		return fileDest;
	}

	public static byte[] saveImageAfterCrop(InputStream fileSrc, int top, int left, int width, int height) throws Exception {
		BufferedImage bi = (BufferedImage) ImageIO.read(fileSrc);

		height = Math.min(height, bi.getHeight());
		width = Math.min(width, bi.getWidth());
		if (height <= 0)
			height = bi.getHeight();
		if (width <= 0)
			width = bi.getWidth();
		top = Math.min(Math.max(0, top), bi.getHeight() - height);
		left = Math.min(Math.max(0, left), bi.getWidth() - width);

		BufferedImage bi_cropper = bi.getSubimage(left, top, width, height);
		return ImageTool.getByteArray(bi_cropper);
	}
	
	public static void main(String[] args) {

		try {
			ImageCropUtil.imageCrop(new File("D:\\testigm\\1.jpg"), new File("D:\\testigm\\1_bak.jpg"), 600, 600,true);
		} catch (Exception e) {

			// Auto-generated catch block
			e.printStackTrace();

		}
	}
}
