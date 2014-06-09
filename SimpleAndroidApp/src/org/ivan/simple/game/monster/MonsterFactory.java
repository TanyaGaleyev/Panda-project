package org.ivan.simple.game.monster;

import org.ivan.simple.game.hero.Sprite;

public class MonsterFactory {

    public static Monster createMonster(MonsterModel model, int gridStep) {
		if(model == null) return new Monster(null, null, gridStep);
		Sprite monsterSprite;
		switch(model.getMonsterId()) {
		case 0: 
			monsterSprite = new Sprite("monster/dishes.png", 1, 16);
			break;
		case 1:
            monsterSprite = new Sprite("monster/harmonica.png", 1, 16);
			break;
		case 2:
            monsterSprite = new Sprite("monster/whirligig.png", 1, 16);
			break;
		case 3:
            monsterSprite = new Sprite("monster/monster.png", 1, 32);
			break;
		case 4:
            monsterSprite = new Sprite("monster/clown.png", 1, 36);
			break;
		default:
            monsterSprite = new Sprite("monster/dishes.png", 1, 16);
			break;
		}
		monsterSprite.setAnimating(true);
		return new Monster(model, monsterSprite, gridStep);
	}
}
