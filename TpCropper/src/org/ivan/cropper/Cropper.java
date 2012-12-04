package org.ivan.cropper;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Cropper {
	public static void main(String[] args) {
		Cropper cropper = new Cropper();
		try {
			cropper.cropImage("images/cropper/tocrop");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cropImage(String path) throws IOException {
		int oneWidth = 70;
		int oneHeight = 70;
		int shiftWidth = 8;
		BufferedImage source = ImageIO.read(new File(path + ".png"));
		BufferedImage target = new BufferedImage(source.getWidth(null), source.getHeight(null) * 2, BufferedImage.TYPE_INT_ARGB);
		int cols = 8;
		int rows = 14;
		for(int j = 0; j < rows; j++) {
			for(int i = 0; i < cols; i++) {
				if(j % 2 == 0) {
					int shift = shiftWidth * (i + 1);
					int partWidth = oneWidth - shift;
					if(partWidth >= 0) { 
						int[] shiftedpart = source.getRGB(oneWidth * i, j * oneHeight, partWidth, oneHeight, null, 0, partWidth);
						target.setRGB(oneWidth * i + shift, 2 * j * oneHeight, partWidth, oneHeight, shiftedpart, 0, partWidth);
					}
					int backPartWidth = shift;
					if(backPartWidth < oneWidth) {
						int[] shiftedback = source.getRGB(oneWidth * (i + 1) - backPartWidth, j * oneHeight, backPartWidth, oneHeight, null, 0, backPartWidth);
						target.setRGB(oneWidth * i, (2 * j + 1) * oneHeight, backPartWidth, oneHeight, shiftedback, 0, backPartWidth);
					}
				} else {
					int shift = shiftWidth * (i + 1);
					int partWidth = oneWidth - shift;
					if(partWidth >= 0) { 
						int[] shiftedpart = source.getRGB(oneWidth * i + shift, j * oneHeight, partWidth, oneHeight, null, 0, partWidth);
						target.setRGB(oneWidth * i, 2 * j * oneHeight, partWidth, oneHeight, shiftedpart, 0, partWidth);
					}
					int backPartWidth = shift;
					if(backPartWidth < oneWidth) {
						int[] shiftedback = source.getRGB(oneWidth * i, j * oneHeight, backPartWidth, oneHeight, null, 0, backPartWidth);
						target.setRGB(oneWidth * (i + 1) - backPartWidth, (2 * j + 1) * oneHeight, backPartWidth, oneHeight, shiftedback, 0, backPartWidth);
					}
				}
			}
		}
		ImageIO.write(target, "PNG", new File(path + "_cropped.png"));
	}

}
