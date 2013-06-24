package org.ivan.simple.game.monster;

import org.ivan.simple.ImageProvider;
import org.ivan.simple.R;
import org.ivan.simple.game.hero.Sprite;
import org.ivan.simple.game.level.LevelCell;
import org.ivan.simple.game.level.PlatformType;

import android.graphics.Canvas;

public class Monster {
	private Sprite sprite;
	
	public int xCoordinate = 0;
	public int yCoordinate = 0;
	
	private final MonsterModel model;
	
	Monster(MonsterModel model, Sprite sprite) {
		this.model = model;
		this.sprite = sprite;
	}
	
	public void onDraw(Canvas canvas, boolean update) {
		if(model == null) return;
		sprite.onDraw(canvas, xCoordinate, yCoordinate, update);
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
