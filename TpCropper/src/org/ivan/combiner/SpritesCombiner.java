package org.ivan.combiner;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpritesCombiner {
	private static final String IMAGES_PATH = "./images/combiner/";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpritesCombiner combiner = new SpritesCombiner();
		File[] sprites = (new File(IMAGES_PATH)).listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File file) {
				if(file.isDirectory()) return true;
				return false;
			}
		});
		for(File spriteDir : sprites) {
			combiner.combineSprite(spriteDir.getName());
		}
	}
	
	public void combineSprite(String spriteDirPath) {
		File spriteDir = new File(IMAGES_PATH + spriteDirPath);
		File[] spriteDirs = spriteDir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File file) {
				if(file.isDirectory()) return true;
				return false;
			}
		});
		BufferedImage[] sequenceV = new BufferedImage[spriteDirs.length];
		for(int j = 0; j < spriteDirs.length; j++) {
			File[] images = spriteDirs[j].listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File file) {
					String fileName = file.getName();
					if(fileName.substring(fileName.length() - 4).equalsIgnoreCase(".png")) return true;
					return false;
				}
			});
			BufferedImage[] sequence = new BufferedImage[images.length];
			try {
				for(int i = 0; i < images.length; i++) {
					File image = images[i];
					sequence[i] = ImageIO.read(image);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				return;
			}
			BufferedImage result = combineSprite(sequence);
			if(result == null) return;
			sequenceV[j] = result;
		}
		BufferedImage allResult = combineSpriteV(sequenceV);
		if(allResult == null) return;
		try {
			ImageIO.write(allResult, "PNG", new File(IMAGES_PATH + spriteDir.getName() + ".png"));
		} catch(IOException ex) {
			ex.printStackTrace();
			return;
		}
	}
	
	public BufferedImage combineSprite(BufferedImage... sequence) {
		if(sequence == null) return null;
		if(sequence.length == 0) return null;
		int singleWidth = sequence[0].getWidth();
		int singleHeight = sequence[0].getHeight(); 
		BufferedImage resultImage = new BufferedImage(
				singleWidth * sequence.length,
				singleHeight,
				BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < sequence.length; i++) {
			BufferedImage image = sequence[i];
			if(image.getWidth() != singleWidth) return null;
			if(image.getHeight() != singleHeight) return null;
		}
		for(int i = 0; i < sequence.length; i++) {
			BufferedImage image = sequence[i];
			int[] rgbArray = image.getRGB(0, 0, singleWidth, singleHeight, null, 0, singleWidth);
			resultImage.setRGB(singleWidth * i, 0, singleWidth, singleHeight, rgbArray, 0, singleWidth);
		}
		return resultImage;
	}
	
	public BufferedImage combineSpriteV(BufferedImage... sequence) {
		if(sequence == null) return null;
		if(sequence.length == 0) return null;
		int singleWidth = sequence[0].getWidth();
		int singleHeight = sequence[0].getHeight(); 
		BufferedImage resultImage = new BufferedImage(
				singleWidth,
				singleHeight * sequence.length,
				BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < sequence.length; i++) {
			BufferedImage image = sequence[i];
			if(image.getWidth() != singleWidth) return null;
			if(image.getHeight() != singleHeight) return null;
		}
		for(int i = 0; i < sequence.length; i++) {
			BufferedImage image = sequence[i];
			int[] rgbArray = image.getRGB(0, 0, singleWidth, singleHeight, null, 0, singleWidth);
			resultImage.setRGB(0, singleHeight * i, singleWidth, singleHeight, rgbArray, 0, singleWidth);
		}
		return resultImage;
	}

}
