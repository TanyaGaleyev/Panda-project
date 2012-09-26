package org.ivan.simple;

public class MoveEvent {
	UserControlType type = UserControlType.IDLE;
	int move_times = 0;

	public MoveEvent(UserControlType type, int move_times) {
		// TODO Auto-generated constructor stub
		this.type = type;
		this.move_times = move_times;
	}
}
