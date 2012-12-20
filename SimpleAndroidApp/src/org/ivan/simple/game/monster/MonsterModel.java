package org.ivan.simple.game.monster;

public class MonsterModel {
	private MonsterStrategy strategy;
	
	private MonsterDirection currDirection = MonsterDirection.IDLE;
	
	public int row;
	public int col;
	
	public MonsterModel(int row, int col, MonsterStrategy strategy) {
		this.strategy = strategy;
		this.row = row;
		this.col = col;
		currDirection = strategy.nextDirection();
	}
	
	public MonsterModel(int row, int col) {
		this(
				row, 
				col, 
				new MonsterStrategy(
					MonsterDirection.UP,
					MonsterDirection.LEFT,
					MonsterDirection.DOWN,
					MonsterDirection.RIGHT));
	}
	
	public MonsterDirection getDirection() {
		return currDirection;
	}
	
	public void setDirection(MonsterDirection direction) {
		currDirection = direction;
	}
	
	public void moveInDirection() {
		switch(currDirection) {
		case UP:
			row--;
			break;
		case LEFT:
			col--;
			break;
		case DOWN:
			row++;
			break;
		case RIGHT:
			col++;
			break;
		default:
			break;
		}
	}
	
	public MonsterStrategy getStartegy() {
		return strategy;
	}
}
