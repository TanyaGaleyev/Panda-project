package org.ivan.simple.game.hero;

import java.util.HashMap;

import org.ivan.simple.R;


public class SpriteSet {
	public HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
	
	public void putSprite(String name, Sprite sprite) {
		sprites.put(name, sprite);
	}
	
	public void putSprite(String name, int resid, int rows, int cols) {
		putSprite(name, new Sprite(resid, rows, cols));
	}
	
	public Sprite getSprite(String name) {
		return sprites.get(name);
	}
	
	public static SpriteSet getPandaSprites() {
		SpriteSet set = new SpriteSet();
		
		set.putSprite("sprite8_1", R.drawable.panda_sprite8_1, 8, 8);
		set.putSprite("sprite8_2", R.drawable.panda_sprite8_2, 8, 8);
		set.putSprite("sprite8_3", R.drawable.panda_sprite8_3, 8, 8);
		set.putSprite("sprite8_4", R.drawable.panda_sprite8_4, 8, 8);
		set.putSprite("sprite16", R.drawable.panda_sprite16, 4, 16);
		set.putSprite("sprite16_2", R.drawable.panda_sprite16_2, 3, 16);
		
		for(Sprite sprite : set.sprites.values()) {
			sprite.setAnimating(true);
		}
		return set;
	}
}
