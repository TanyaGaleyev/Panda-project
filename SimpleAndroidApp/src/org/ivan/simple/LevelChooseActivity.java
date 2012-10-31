package org.ivan.simple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;

public class LevelChooseActivity extends Activity {
	
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
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onTouchEvent(event);
	}
}
