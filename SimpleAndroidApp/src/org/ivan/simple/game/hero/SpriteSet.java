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
		
		set.putSprite("stepleft", R.drawable.panda_sprite8_stepleft, 1, 8);
		set.putSprite("stepright", R.drawable.panda_sprite8_stepright, 1, 8);
		set.putSprite("jumpleft", R.drawable.panda_sprite8_jumpleft, 1, 8);
		set.putSprite("jumpright", R.drawable.panda_sprite8_jumpright, 1, 8);
		set.putSprite("jumpleftwall", R.drawable.panda_sprite8_jumpleftwall, 1, 8);
		set.putSprite("jumprightwall", R.drawable.panda_sprite8_jumprightwall, 1, 8);
		set.putSprite("prejump", R.drawable.panda_sprite8_prejump, 1, 8);
		set.putSprite("jump", R.drawable.panda_sprite8_jump, 1, 8);
		set.putSprite("beatroof", R.drawable.panda_sprite8_beatroof, 1, 8);
		set.putSprite("fall", R.drawable.panda_sprite8_fall, 1, 8);
		set.putSprite("fall2", R.drawable.panda_sprite8_fall2, 1, 8);
		set.putSprite("fallinto", R.drawable.panda_sprite8_fallinto, 1, 8);
		set.putSprite("flyout", R.drawable.panda_sprite8_flyout, 1, 8);
		set.putSprite("fallfly", R.drawable.panda_sprite8_fallfly, 1, 8);
		set.putSprite("flyleft", R.drawable.panda_sprite8_flyleft, 1, 8);
		set.putSprite("flyright", R.drawable.panda_sprite8_flyright, 1, 8);
		set.putSprite("premagnet", R.drawable.panda_sprite8_premagnet, 1, 8);
		set.putSprite("magnet", R.drawable.panda_sprite8_magnet, 1, 8);
		set.putSprite("postmagnet", R.drawable.panda_sprite8_postmagnet, 1, 8);
		set.putSprite("prestickleft", R.drawable.panda_sprite8_prestickleft, 1, 8);
		set.putSprite("stickleft", R.drawable.panda_sprite8_stickleft, 1, 8);
		set.putSprite("poststickleft", R.drawable.panda_sprite8_poststickleft, 1, 8);
		set.putSprite("prestickright", R.drawable.panda_sprite8_prestickright, 1, 8);
		set.putSprite("stickright", R.drawable.panda_sprite8_stickright, 1, 8);
		set.putSprite("poststickright", R.drawable.panda_sprite8_poststickright, 1, 8);
		set.putSprite("beginflyleft8", R.drawable.panda_sprite8_beginflyleft, 1, 8);
		set.putSprite("beginflyright8", R.drawable.panda_sprite8_beginflyright, 1, 8);
		set.putSprite("throwleft1", R.drawable.panda_sprite8_throwleft1, 1, 8);
		set.putSprite("throwleft2", R.drawable.panda_sprite8_throwleft2, 1, 8);
		set.putSprite("throwright1", R.drawable.panda_sprite8_throwright1, 1, 8);
		set.putSprite("throwright2", R.drawable.panda_sprite8_throwright2, 1, 8);
		
		set.putSprite("stay", R.drawable.panda_sprite16_stay, 1, 16);
		set.putSprite("fallblansh", R.drawable.panda_sprite16_fallblansh, 1, 16);
		set.putSprite("stepleftwall", R.drawable.panda_sprite16_stepleftwall, 1, 16);
		set.putSprite("steprightwall", R.drawable.panda_sprite16_steprightwall, 1, 16);
		set.putSprite("beginflyleft", R.drawable.panda_sprite16_beginflyleft, 1, 16);
		set.putSprite("beginflyright", R.drawable.panda_sprite16_beginflyright, 1, 16);
		set.putSprite("glue", R.drawable.panda_sprite16_glue, 1, 16);
		
		for(Sprite sprite : set.sprites.values()) {
			sprite.setAnimating(true);
		}
		return set;
	}
}
