package org.ivan.simple.game.hero;

import java.util.HashMap;

import org.ivan.simple.PandaApplication;


public class SpriteSet {
    private SpriteSet() {}

    public HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
	
	public void putSprite(String name, Sprite sprite) {
		sprites.put(name, sprite);
	}
	
	public void putSprite(String name, String resid, int rows, int cols) {
		putSprite(name, new Sprite(resid, rows, cols));
	}
	
	public void putTPSprite(String name, String resid, int rows, int cols, boolean left) {
        int shiftSt = PandaApplication.getPandaApplication().getImageProvider().getGridStep() / 8;
        putSprite(name, new SymmetricTPSprite(resid, rows, cols, shiftSt, left));
	}

    public void putAsymmetricTPSprite(String name, String inBmpid, String outBmpId, int rows, int cols, boolean lIn, boolean lOut) {
        int shiftSt = PandaApplication.getPandaApplication().getImageProvider().getGridStep() / 8;
        putSprite(name, new AsymmetricTPSprite(inBmpid, outBmpId, rows, cols, shiftSt, lIn, lOut));
    }
    
    public void putAsymmetricTPSpriteLR(String name, String inBmpid, String outBmpid) {
        putAsymmetricTPSprite(name, inBmpid, outBmpid, 1, 8, true, false);
    }
    
    public void putAsymmetricTPSpriteRL(String name, String inBmpid, String outBmpid) {
        putAsymmetricTPSprite(name, inBmpid, outBmpid, 1, 8, false, true);
    }

    public Sprite getSprite(String name) {
		return sprites.get(name);
	}
	
	public TPSprite getTPSprite(String name) {
		return (TPSprite) sprites.get(name);
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
		set.putSprite("prestickleftjump", "panda/in_sticky_left.png", 1, 8);
		set.putSprite("stickleft", "panda/panda_sprite8_stickleft.png", 1, 8);
		set.putSprite("poststickleft", "panda/panda_sprite8_poststickleft.png", 1, 8);
		set.putSprite("prestickright", "panda/panda_sprite8_prestickright.png", 1, 8);
		set.putSprite("prestickrightjump", "panda/in_sticky_right.png", 1, 8);
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
		set.putSprite("startslickright", "panda/panda_sprite8_startslickright.png", 1, 8);
		set.putSprite("slickright", "panda/panda_sprite8_slickright.png", 1, 8);
//		set.putSprite("cloud", "panda/panda_sprite8_cloud.png", 1, 8);
		set.putSprite("cloud", "panda/panda_sprite16_ufo_stay.png", 1, 16);
        set.putSprite("cloud_left", "panda/panda_sprite8_ufo_left.png", 1, 8);
        set.putSprite("cloud_right", "panda/panda_sprite8_ufo_right.png", 1, 8);
        set.putSprite("cloud_in_left", "panda/panda_sprite8_jump_in_ufo_left.png", 1, 8);
        set.putSprite("cloud_in_right", "panda/panda_sprite8_jump_in_ufo_right.png", 1, 8);
        set.putSprite("cloud_out_left", "panda/panda_sprite8_jump_out_ufo_left.png", 1, 8);
        set.putSprite("cloud_out_right", "panda/panda_sprite8_jump_out_ufo_right.png", 1, 8);
        set.putSprite("cloud_prepare", "panda/panda_sprite16_create_helmet.png", 1, 16);
        set.putSprite("cloud_up", "panda/ufo_up.png", 1, 8);
        set.putSprite("cloud_down", "panda/ufo_down.png", 1, 8);

		
		set.putTPSprite("flyleft_tp", "panda/panda_sprite8_flyleft.png", 1, 8, true);
		set.putTPSprite("flyright_tp", "panda/panda_sprite8_flyright.png", 1, 8, false);
		set.putTPSprite("stepleft_tp", "panda/panda_sprite8_stepleft.png", 1, 8, true);
		set.putTPSprite("stepright_tp", "panda/panda_sprite8_stepright.png", 1, 8, false);
		set.putTPSprite("jumpleft_tp", "panda/panda_sprite8_jumpleft.png", 1, 8, true);
		set.putTPSprite("jumpright_tp", "panda/panda_sprite8_jumpright.png", 1, 8, false);
		set.putTPSprite("throwleft1_tp", "panda/panda_sprite8_throwleft1.png", 1, 8, true);
		set.putTPSprite("throwleft2_tp", "panda/panda_sprite8_throwleft2.png", 1, 8, true);
		set.putTPSprite("throwright1_tp", "panda/panda_sprite8_throwright1.png", 1, 8, false);
		set.putTPSprite("throwright2_tp", "panda/panda_sprite8_throwright2.png", 1, 8, false);
		set.putTPSprite("startslickleft_tp", "panda/panda_sprite8_startslickleft.png", 1, 8, true);
		set.putTPSprite("slickleft_tp", "panda/panda_sprite8_slickleft.png", 1, 8, true);
		set.putTPSprite("startslickright_tp", "panda/panda_sprite8_startslickright.png", 1, 8, false);
		set.putTPSprite("slickright_tp", "panda/panda_sprite8_slickright.png", 1, 8, false);

        set.putAsymmetricTPSpriteLR("flyLR_tp", "panda/panda_sprite8_flyleft.png", "panda/panda_sprite8_flyright.png");
		set.putAsymmetricTPSpriteLR("stepLR_tp", "panda/panda_sprite8_stepleft.png", "panda/panda_sprite8_stepright.png");
        set.putAsymmetricTPSpriteLR("jumpLR_tp", "panda/panda_sprite8_jumpleft.png", "panda/panda_sprite8_jumpright.png");
        set.putAsymmetricTPSpriteLR("throwLR1_tp", "panda/panda_sprite8_throwleft1.png", "panda/panda_sprite8_throwright1.png");
        set.putAsymmetricTPSpriteLR("throwLR2_tp", "panda/panda_sprite8_throwleft2.png", "panda/panda_sprite8_throwright2.png");
        set.putAsymmetricTPSpriteLR("startslickLR_tp", "panda/panda_sprite8_startslickleft.png", "panda/panda_sprite8_startslickright.png");
        set.putAsymmetricTPSpriteLR("slickLR_tp", "panda/panda_sprite8_slickleft.png", "panda/panda_sprite8_slickright.png");

        set.putAsymmetricTPSpriteRL("flyRL_tp", "panda/panda_sprite8_flyright.png", "panda/panda_sprite8_flyleft.png");
        set.putAsymmetricTPSpriteRL("stepRL_tp", "panda/panda_sprite8_stepright.png", "panda/panda_sprite8_stepleft.png");
        set.putAsymmetricTPSpriteRL("jumpRL_tp", "panda/panda_sprite8_jumpright.png", "panda/panda_sprite8_jumpleft.png");
        set.putAsymmetricTPSpriteRL("throwRL1_tp", "panda/panda_sprite8_throwright1.png", "panda/panda_sprite8_throwleft1.png");
        set.putAsymmetricTPSpriteRL("throwRL2_tp", "panda/panda_sprite8_throwright2.png", "panda/panda_sprite8_throwleft2.png");
        set.putAsymmetricTPSpriteRL("startslickRL_tp", "panda/panda_sprite8_startslickright.png", "panda/panda_sprite8_startslickleft.png");
        set.putAsymmetricTPSpriteRL("slickRL_tp", "panda/panda_sprite8_slickright.png", "panda/panda_sprite8_slickleft.png");

//		set.putSprite("flyleft_tp", "panda/panda_sprite8_flyleft_tp.png", 1, 8);
//		set.putSprite("flyright_tp", "panda/panda_sprite8_flyright_tp.png", 1, 8);
//		set.putSprite("stepleft_tp", "panda/panda_sprite8_stepleft_tp.png", 1, 8);
//		set.putSprite("stepright_tp", "panda/panda_sprite8_stepright_tp.png", 1, 8);
//		set.putSprite("jumpleft_tp", "panda/panda_sprite8_jumpleft_tp.png", 1, 8);
//		set.putSprite("jumpright_tp", "panda/panda_sprite8_jumpright_tp.png", 1, 8);
//		set.putSprite("throwleft1_tp", "panda/panda_sprite8_throwleft1_tp.png", 1, 8);
//		set.putSprite("throwleft2_tp", "panda/panda_sprite8_throwleft2_tp.png", 1, 8);
//		set.putSprite("throwright1_tp", "panda/panda_sprite8_throwright1_tp.png", 1, 8);
//		set.putSprite("throwright2_tp", "panda/panda_sprite8_throwright2_tp.png", 1, 8);
//		set.putSprite("startslickleft_tp", "panda/panda_sprite8_startslickleft_tp.png", 1, 8);
//		set.putSprite("slickleft_tp", "panda/panda_sprite8_slickleft_tp.png", 1, 8);
//		set.putSprite("startslickright_tp", "panda/panda_sprite8_startslickright_tp.png", 1, 8);
//		set.putSprite("slickright_tp", "panda/panda_sprite8_slickright_tp.png", 1, 8);
//		
//		set.putSprite("flyleft_tpshade", "panda/panda_sprite8_flyleft_tpshade.png", 1, 8);
//		set.putSprite("flyright_tpshade", "panda/panda_sprite8_flyright_tpshade.png", 1, 8);
//		set.putSprite("stepleft_tpshade", "panda/panda_sprite8_stepleft_tpshade.png", 1, 8);
//		set.putSprite("stepright_tpshade", "panda/panda_sprite8_stepright_tpshade.png", 1, 8);
//		set.putSprite("jumpleft_tpshade", "panda/panda_sprite8_jumpleft_tpshade.png", 1, 8);
//		set.putSprite("jumpright_tpshade", "panda/panda_sprite8_jumpright_tpshade.png", 1, 8);
//		set.putSprite("throwleft1_tpshade", "panda/panda_sprite8_throwleft1_tpshade.png", 1, 8);
//		set.putSprite("throwleft2_tpshade", "panda/panda_sprite8_throwleft2_tpshade.png", 1, 8);
//		set.putSprite("throwright1_tpshade", "panda/panda_sprite8_throwright1_tpshade.png", 1, 8);
//		set.putSprite("throwright2_tpshade", "panda/panda_sprite8_throwright2_tpshade.png", 1, 8);
//		set.putSprite("startslickleft_tpshade", "panda/panda_sprite8_startslickleft_tpshade.png", 1, 8);
//		set.putSprite("slickleft_tpshade", "panda/panda_sprite8_slickleft_tpshade.png", 1, 8);
//		set.putSprite("startslickright_tpshade", "panda/panda_sprite8_startslickright_tpshade.png", 1, 8);
//		set.putSprite("slickright_tpshade", "panda/panda_sprite8_slickright_tpshade.png", 1, 8);
		
		set.putSprite("stay", "panda/panda_sprite16_stay.png", 1, 16);
		set.putSprite("fallblansh", "panda/panda_sprite16_fallblansh.png", 1, 16);
		set.putSprite("stepleftwall", "panda/panda_sprite16_stepleftwall.png", 1, 16);
		set.putSprite("steprightwall", "panda/panda_sprite16_steprightwall.png", 1, 16);
		set.putSprite("beginflyleft", "panda/panda_sprite16_beginflyleft.png", 1, 16);
		set.putSprite("beginflyright", "panda/panda_sprite16_beginflyright.png", 1, 16);
		set.putSprite("glue", "panda/panda_sprite16_glue.png", 1, 16);
		set.putSprite("slickleftwall", "panda/panda_sprite16_slickleftwall.png", 1, 16);
		set.putSprite("slickrightwall", "panda/panda_sprite16_slickrightwall.png", 1, 16);

        set.putSprite("detonate", "panda/detonate.png", 1, 16);
		
		for(Sprite sprite : set.sprites.values()) {
			sprite.setAnimating(true);
		}
		return set;
	}
}
