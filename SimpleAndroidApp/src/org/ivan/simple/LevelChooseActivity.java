package org.ivan.simple;

import org.ivan.simple.game.GameActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;

public class LevelChooseActivity extends Activity {
	public static final String LEVEL_ID = "levId";
	
	private LevelChooseView view;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide screen title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose);
        view = (LevelChooseView) findViewById(R.id.choose);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int levId = view.getLevelId(event); 
		if(levId != 0) {
			Intent intent = new Intent(this, GameActivity.class);
			intent.putExtra(LEVEL_ID, levId);
			startActivity(intent);
			return true;
		}
		return super.onTouchEvent(event);
	}
}
