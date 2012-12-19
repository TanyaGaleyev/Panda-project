package org.ivan.simple.game.monster;

import org.ivan.simple.ImageProvider;
import org.ivan.simple.R;
import org.ivan.simple.game.hero.Sprite;
import org.ivan.simple.game.level.LevelCell;
import org.ivan.simple.game.level.PlatformType;

import android.graphics.Canvas;

public class Monster {
	private Sprite sprite = new Sprite(ImageProvider.getBitmap(R.drawable.monster), 1, 1);
	
	public int xCoordinate = 0;
	public int yCoordinate = 0;

	private MonsterStrategy strategy;
	
	private MonsterDirection currDirection = MonsterDirection.IDLE;
	
	public Monster() {
		this(new MonsterStrategy(
				MonsterDirection.UP,
				MonsterDirection.LEFT,
				MonsterDirection.DOWN,
				MonsterDirection.RIGHT)); 
	}
	
	public Monster(MonsterStrategy strategy) {
		this.strategy = strategy;
	}
	
	public void onDraw(Canvas canvas, boolean update) {
		sprite.onDraw(canvas, xCoordinate - sprite.getWidth() / 2, yCoordinate - sprite.getHeight() / 2, update);
	}
	
	public void moveInCurrentDirection(int speed) {
		switch(currDirection) {
		case UP:
			yCoordinate -= speed;
			break;
		case LEFT:
			xCoordinate += speed;
			break;
		case DOWN:
			yCoordinate += speed;
			break;
		case RIGHT:
			xCoordinate -= speed;
			break;
		case IDLE:
		default:
			break;
		}
	}
	
	public void getNextDirection(LevelCell prevCell) {
		for(MonsterDirection direction : strategy.getDirectionsByPriority()) {
			if(directionAvaible(direction, prevCell)) {
				currDirection = direction;
				return;
			}
		}
		currDirection = MonsterDirection.IDLE;
	}
	
	private boolean directionAvaible(MonsterDirection direction, LevelCell prevCell) {
		switch(direction) {
		case UP:
			return prevCell.getRoof().getType() == PlatformType.NONE;
		case LEFT:
			return prevCell.getLeft().getType() == PlatformType.NONE;
		case DOWN:
			return prevCell.getFloor().getType() == PlatformType.NONE;
		case RIGHT:
			return prevCell.getRight().getType() == PlatformType.NONE;
		case IDLE:
			return true;
		default:
			return false;
		}
	}
	
}
