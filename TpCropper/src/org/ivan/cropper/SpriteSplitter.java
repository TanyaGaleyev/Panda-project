package org.ivan.cropper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSplitter {
	public static void main(String[] args) {
		SpriteSplitter splitter = new SpriteSplitter();
		try {
			splitter.splitSprite("images/splitter/panda_sprite16", 230);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void splitSprite(String path, int height) throws IOException {
		BufferedImage source = ImageIO.read(new File(path + ".png"));
		int width = source.getWidth();
		for(int i = 0; i < source.getHeight() / height; i++) {
			int[] rgbArray = source.getRGB(0, height * i, width, height, null, 0, width);
			BufferedImage target = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			target.setRGB(0, 0, width, height, rgbArray, 0, width);
			ImageIO.write(target, "PNG", new File(path + "_" + i + ".png"));
		}
	}
}
