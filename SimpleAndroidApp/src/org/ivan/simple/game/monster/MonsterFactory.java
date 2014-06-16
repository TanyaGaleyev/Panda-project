package org.ivan.simple.game.monster;

import org.ivan.simple.game.hero.Sprite;

public class MonsterFactory {

    public static Monster createMonster(MonsterModel model, int gridStep) {
		if(model == null) return new Monster(null, null, gridStep);
		Sprite monsterSprite;
		switch(model.getMonsterId()) {
		case 0: 
			monsterSprite = Sprite.createLru("monster/dishes.png", 1, 16);
			break;
		case 1:
            monsterSprite = Sprite.createLru("monster/harmonica.png", 1, 16);
			break;
		case 2:
            monsterSprite = Sprite.createLru("monster/whirligig.png", 1, 16);
			break;
		case 3:
            monsterSprite = Sprite.createLru("monster/monster.png", 1, 32);
			break;
		case 4:
            monsterSprite = Sprite.createLru("monster/clown.png", 1, 36);
			break;
		default:
            monsterSprite = Sprite.createLru("monster/dishes.png", 1, 16);
			break;
		}
		monsterSprite.setAnimating(true);
		return new Monster(model, monsterSprite, gridStep);
	}
}
