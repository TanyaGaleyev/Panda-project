package org.ivan.cropper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Cropper {
	public static void main(String[] args) {
		Cropper cropper = new Cropper();
		cropper.cropAll("images/cropper", 230, 230, 25);
	}
	
	public void cropAll(String path, int oneWidth, int oneHeight, int shiftWidth) {
		try {
			File workDir = new File(path);
			if(!workDir.isDirectory()) return;
			new File(workDir, "cropped").mkdir();
			for(File tocrop : workDir.listFiles()) {
				if(tocrop.isDirectory()) continue;
				BufferedImage[] targets = cropImage(tocrop, oneWidth, oneHeight, shiftWidth);
				String name = tocrop.getName().split("\\.")[0];
				ImageIO.write(targets[0], "PNG", new File(path + "/cropped/" + name + "_tpshade.png"));
				ImageIO.write(targets[1], "PNG", new File(path + "/cropped/" + name + "_tp.png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage[] cropImage(File tocrop, int oneWidth, int oneHeight, int shiftWidth) throws IOException {
		BufferedImage source = ImageIO.read(tocrop);
		BufferedImage target1 = new BufferedImage(source.getWidth(null), source.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		BufferedImage target2 = new BufferedImage(source.getWidth(null), source.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		int cols = source.getWidth() / oneWidth;
		int rows = source.getHeight() / oneHeight;
		for(int j = 0; j < rows; j++) {
			for(int i = 0; i < cols; i++) {
				if(tocrop.getName().contains("right")) {
					int shift = shiftWidth * (i + 1);
					int partWidth = oneWidth - shift;
					if(partWidth >= 0) { 
						int[] shiftedpart = source.getRGB(oneWidth * i, j * oneHeight, partWidth, oneHeight, null, 0, partWidth);
						target1.setRGB(oneWidth * i + shift, 2 * j * oneHeight, partWidth, oneHeight, shiftedpart, 0, partWidth);
					}
					int backPartWidth = shift;
					if(backPartWidth < oneWidth) {
						int[] shiftedback = source.getRGB(oneWidth * (i + 1) - backPartWidth, j * oneHeight, backPartWidth, oneHeight, null, 0, backPartWidth);
						target2.setRGB(oneWidth * i, (2 * j) * oneHeight, backPartWidth, oneHeight, shiftedback, 0, backPartWidth);
					}
				} else {
					int shift = shiftWidth * (i + 1);
					int partWidth = oneWidth - shift;
					if(partWidth >= 0) { 
						int[] shiftedpart = source.getRGB(oneWidth * i + shift, j * oneHeight, partWidth, oneHeight, null, 0, partWidth);
						target1.setRGB(oneWidth * i, 2 * j * oneHeight, partWidth, oneHeight, shiftedpart, 0, partWidth);
					}
					int backPartWidth = shift;
					if(backPartWidth < oneWidth) {
						int[] shiftedback = source.getRGB(oneWidth * i, j * oneHeight, backPartWidth, oneHeight, null, 0, backPartWidth);
						target2.setRGB(oneWidth * (i + 1) - backPartWidth, (2 * j) * oneHeight, backPartWidth, oneHeight, shiftedback, 0, backPartWidth);
					}
				}
			}
		}
		return new BufferedImage[]{target1, target2};
	}

}
