package org.ivan.simple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class LevelChooseActivity extends Activity {
	public static final String LEVEL_ID = "levId";
	public static final String LEVEL_COMPLETE = "complete";
	public static final int FINISHED_LEVEL_ID = 1;
	
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == FINISHED_LEVEL_ID && resultCode == Activity.RESULT_OK) {
			boolean complete = data.getBooleanExtra(LEVEL_COMPLETE, false);
			if(complete) view.completeCurrentLevel();
		}
	}
}
