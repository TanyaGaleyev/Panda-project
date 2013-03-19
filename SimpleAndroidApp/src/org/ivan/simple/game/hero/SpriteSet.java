package org.ivan.simple.game.hero;

import java.util.HashMap;

import org.ivan.simple.R;


public class SpriteSet {
	public HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
	
	public void putSprite(String name, Sprite sprite) {
		sprites.put(name, sprite);
	}
	
	public void putSprite(String name, String resid, int rows, int cols) {
		putSprite(name, new Sprite(resid, rows, cols));
	}
	
	public Sprite getSprite(String name) {
		return sprites.get(name);
	}
	
	public static SpriteSet getPandaSprites() {
		SpriteSet set = new SpriteSet();
		
		set.putSprite("stepleft", "panda/panda_sprite8_stepleft.png", 1, 8);
		set.putSprite("stepright", "panda/panda_sprite8_stepright.png", 1, 8);
		set.putSprite("jumpleft", "panda/panda_sprite8_jumpleft.png", 1, 8);
		set.putSprite("jumpright", "panda/panda_sprite8_jumpright.png", 1, 8);
		set.putSprite("jumpleftwall", "panda/panda_sprite8_jumpleftwall.png", 1, 8);
		set.putSprite("jumprightwall", "panda/panda_sprite8_jumprightwall.png", 1, 8);
		set.putSprite("prejump", "panda/panda_sprite8_prejump.png", 1, 8);
		set.putSprite("jump", "panda/panda_sprite8_jump.png", 1, 8);
		set.putSprite("beatroof", "panda/panda_sprite8_beatroof.png", 1, 8);
		set.putSprite("fall", "panda/panda_sprite8_fall.png", 1, 8);
		set.putSprite("fall2", "panda/panda_sprite8_fall2.png", 1, 8);
		set.putSprite("fallinto", "panda/panda_sprite8_fallinto.png", 1, 8);
		set.putSprite("flyout", "panda/panda_sprite8_flyout.png", 1, 8);
		set.putSprite("fallfly", "panda/panda_sprite8_fallfly.png", 1, 8);
		set.putSprite("flyleft", "panda/panda_sprite8_flyleft.png", 1, 8);
		set.putSprite("flyright", "panda/panda_sprite8_flyright.png", 1, 8);
		set.putSprite("premagnet", "panda/panda_sprite8_premagnet.png", 1, 8);
		set.putSprite("magnet", "panda/panda_sprite8_magnet.png", 1, 8);
		set.putSprite("postmagnet", "panda/panda_sprite8_postmagnet.png", 1, 8);
		set.putSprite("prestickleft", "panda/panda_sprite8_prestickleft.png", 1, 8);
		set.putSprite("stickleft", "panda/panda_sprite8_stickleft.png", 1, 8);
		set.putSprite("poststickleft", "panda/panda_sprite8_poststickleft.png", 1, 8);
		set.putSprite("prestickright", "panda/panda_sprite8_prestickright.png", 1, 8);
		set.putSprite("stickright", "panda/panda_sprite8_stickright.png", 1, 8);
		set.putSprite("poststickright", "panda/panda_sprite8_poststickright.png", 1, 8);
		set.putSprite("beginflyleft8", "panda/panda_sprite8_beginflyleft.png", 1, 8);
		set.putSprite("beginflyright8", "panda/panda_sprite8_beginflyright.png", 1, 8);
		set.putSprite("throwleft1", "panda/panda_sprite8_throwleft1.png", 1, 8);
		set.putSprite("throwleft2", "panda/panda_sprite8_throwleft2.png", 1, 8);
		set.putSprite("throwright1", "panda/panda_sprite8_throwright1.png", 1, 8);
		set.putSprite("throwright2", "panda/panda_sprite8_throwright2.png", 1, 8);
		set.putSprite("startslickleft", "panda/panda_sprite8_startslickleft.png", 1, 8);
		set.putSprite("slickleft", "panda/panda_sprite8_slickleft.png", 1, 8);
		set.putSprite("startslickright", "panda/panda_sprite8_startslickright.png", 1, 8);
		set.putSprite("slickright", "panda/panda_sprite8_slickright.png", 1, 8);
		
		set.putSprite("stay", "panda/panda_sprite16_stay.png", 1, 16);
		set.putSprite("fallblansh", "panda/panda_sprite16_fallblansh.png", 1, 16);
		set.putSprite("stepleftwall", "panda/panda_sprite16_stepleftwall.png", 1, 16);
		set.putSprite("steprightwall", "panda/panda_sprite16_steprightwall.png", 1, 16);
		set.putSprite("beginflyleft", "panda/panda_sprite16_beginflyleft.png", 1, 16);
		set.putSprite("beginflyright", "panda/panda_sprite16_beginflyright.png", 1, 16);
		set.putSprite("glue", "panda/panda_sprite16_glue.png", 1, 16);
		set.putSprite("slickleftwall", "panda/panda_sprite16_slickleftwall.png", 1, 16);
		set.putSprite("slickrightwall", "panda/panda_sprite16_slickrightwall.png", 1, 16);
		
		for(Sprite sprite : set.sprites.values()) {
			sprite.setAnimating(true);
		}
		return set;
	}
}
