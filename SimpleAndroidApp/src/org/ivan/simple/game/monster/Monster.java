package org.ivan.simple.game.monster;

import org.ivan.simple.ImageProvider;
import org.ivan.simple.R;
import org.ivan.simple.game.hero.Sprite;
import org.ivan.simple.game.level.LevelCell;
import org.ivan.simple.game.level.PlatformType;

import android.graphics.Canvas;

public class Monster {
	private Sprite sprite = new Sprite("monster/monster.png", 1, 16);
	
	public int xCoordinate = 0;
	public int yCoordinate = 0;
	
	private final MonsterModel model;
	
	public Monster(MonsterModel model) {
		this.model = model;
		sprite.setAnimating(true);
	}
	
	public void onDraw(Canvas canvas, boolean update) {
		if(model == null) return;
		sprite.onDraw(canvas, xCoordinate - sprite.getWidth() / 2, yCoordinate - sprite.getHeight() / 2, update);
	}
	
	public void moveInCurrentDirection(int speed) {
		if(model == null) return;
		switch(model.getDirection()) {
		case UP:
			yCoordinate -= speed;
			break;
		case LEFT:
			xCoordinate -= speed;
			break;
		case DOWN:
			yCoordinate += speed;
			break;
		case RIGHT:
			xCoordinate += speed;
			break;
		case IDLE:
		default:
			break;
		}
	}
	
}
