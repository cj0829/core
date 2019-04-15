package org.csr.core.util.crop;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.csr.core.util.mm.MimeTypeTool;

public class ImageTool {
	private static final String BMP = ".bmp";
	public static final String THUMBNAIL = "thumbnail.jpg";

	/**
	 * 缩略图片,并返回缩略图的文件流
	 * 
	 * @param source
	 * @param width
	 * @param height
	 * @param adjustSize
	 * @return
	 * @throws Exception
	 */
	public static CropImg zoomPictureStream(File source, int width, int height, boolean adjustSize) throws Exception {

		BufferedImage image = getBufferImage(source, width, height, adjustSize);
		if (null == image) {
			return null;
		} else {
			return getInputSream(image);
		}
	}

	/**
	 * 缩略图片,并返回缩略图的文件流
	 * 
	 * @param source
	 * @param width
	 * @param height
	 * @param adjustSize
	 * @return
	 * @throws Exception
	 */
	public static CropImg zoomPictureStream(InputStream source, int width, int height, boolean adjustSize)
			throws Exception {

		BufferedImage image = getBufferImage(source, width, height, adjustSize);
		if (null == image) {
			return null;
		} else {
			return getInputSream(image);
		}
	}

	private static BufferedImage getBufferImage(InputStream source, int width, int height, boolean adjustSize)
			throws Exception {
		if (width < 1 || height < 1) {
			return null;
		}
		Image image = ImageIO.read(source);

		if (null == image) {
			return null;
		}
		int theImgWidth = image.getWidth(null);
		int theImgHeight = image.getHeight(null);

		if (width >= theImgWidth && height >= theImgHeight) {
			return (BufferedImage) image;
		}

		int[] size = { width, height };
		if (adjustSize) {
			size = adjustImageSize(theImgWidth, theImgHeight, width, height);
		}
		return getBufferImage(image, size[0], size[1]);
	}

	private static BufferedImage getBufferImage(File source, int width, int height, boolean adjustSize)
			throws Exception {
		if (width < 1 || height < 1) {
			return null;
		}
		Image image;
		if (MimeTypeTool.getMimeType(source).toLowerCase().endsWith(BMP)) {
			image = getBMPImage(source);
		} else {
			image = ImageIO.read(source);
		}
		if (null == image) {
			return null;
		}
		int theImgWidth = image.getWidth(null);
		int theImgHeight = image.getHeight(null);

		if (width >= theImgWidth && height >= theImgHeight) {
			return (BufferedImage) image;
		}

		int[] size = { width, height };
		if (adjustSize) {
			size = adjustImageSize(theImgWidth, theImgHeight, width, height);
		}
		return getBufferImage(image, size[0], size[1]);
	}

	private static BufferedImage getBufferImage(Image image, int width, int height) {
		if (image == null)
			return null;
		BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 绘制图
		tag.getGraphics().drawImage(image, 0, 0, width, height, null);

		return tag;
	}

	public static CropImg getInputSream(BufferedImage image) throws Exception {
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", bao);
		byte[] data = bao.toByteArray();
		return new CropImg(new ByteArrayInputStream(data), data.length, image.getWidth(), image.getHeight());
	}

	public static byte[] getByteArray(BufferedImage image) throws Exception {
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", bao);
		return bao.toByteArray();
	}

	private static Image getBMPImage(File source) throws Exception {
		InputStream fs = null;
		Image image = null;
		try {
			fs = new FileInputStream(source);
			int bfLen = 14;
			byte bf[] = new byte[bfLen];
			fs.read(bf, 0, bfLen); // 读取14字节BMP文件头
			int biLen = 40;
			byte bi[] = new byte[biLen];
			fs.read(bi, 0, biLen); // 读取40字节BMP信息头

			// 源图宽度
			int nWidth = (((int) bi[7] & 0xff) << 24) | (((int) bi[6] & 0xff) << 16) | (((int) bi[5] & 0xff) << 8)
					| (int) bi[4] & 0xff;

			// 源图高度
			int nHeight = (((int) bi[11] & 0xff) << 24) | (((int) bi[10] & 0xff) << 16) | (((int) bi[9] & 0xff) << 8)
					| (int) bi[8] & 0xff;

			// 位数
			int nBitCount = (((int) bi[15] & 0xff) << 8) | (int) bi[14] & 0xff;

			// 源图大小
			int nSizeImage = (((int) bi[23] & 0xff) << 24) | (((int) bi[22] & 0xff) << 16)
					| (((int) bi[21] & 0xff) << 8) | (int) bi[20] & 0xff;

			// 对24位BMP进行解析
			if (nBitCount == 24) {
				int nPad = (nSizeImage / nHeight) - nWidth * 3;
				int nData[] = new int[nHeight * nWidth];
				byte bRGB[] = new byte[(nWidth + nPad) * 3 * nHeight];
				fs.read(bRGB, 0, (nWidth + nPad) * 3 * nHeight);
				int nIndex = 0;
				for (int j = 0; j < nHeight; j++) {
					for (int i = 0; i < nWidth; i++) {
						nData[nWidth * (nHeight - j - 1) + i] = (255 & 0xff) << 24
								| (((int) bRGB[nIndex + 2] & 0xff) << 16) | (((int) bRGB[nIndex + 1] & 0xff) << 8)
								| (int) bRGB[nIndex] & 0xff;
						nIndex += 3;
					}
					nIndex += nPad;
				}
				Toolkit kit = Toolkit.getDefaultToolkit();
				image = kit.createImage(new MemoryImageSource(nWidth, nHeight, nData, 0, nWidth));
			}
		} finally {
			if (fs != null) {
				try {
					fs.close();
				} catch (Exception e) {
				}
			}
		}
		return image;
	}

	private static int[] adjustImageSize(int theImgWidth, int theImgHeight, int defWidth, int defHeight) {
		int[] size = { 0, 0 };

		float theImgHeightFloat = Float.parseFloat(String.valueOf(theImgHeight));
		float theImgWidthFloat = Float.parseFloat(String.valueOf(theImgWidth));
		if (theImgWidth < theImgHeight) {
			float scale = theImgHeightFloat / theImgWidthFloat;
			size[0] = Math.round(defHeight / scale);
			size[1] = defHeight;
		} else {
			float scale = theImgWidthFloat / theImgHeightFloat;
			size[0] = defWidth;
			size[1] = Math.round(defWidth / scale);
		}
		return size;
	}

	public static byte[] png2jpg(InputStream cardimage) throws IOException {
		BufferedImage bufferedImage = ImageIO.read(cardimage);
		// create a blank, RGB, same width and height, and a white background
		BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),BufferedImage.TYPE_INT_RGB);
		newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(newBufferedImage, "jpg", out);
		return out.toByteArray();
	}
}
