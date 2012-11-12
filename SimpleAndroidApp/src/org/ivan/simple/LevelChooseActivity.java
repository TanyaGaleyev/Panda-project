package org.ivan.simple;

import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

public class LevelChooseActivity extends Activity {
	public static final String LEVEL_ID = "levId";
	public static final String LEVEL_COMPLETE = "complete";
	public static final int FINISHED_LEVEL_ID = 1;
	public static final String FINISHED_LEVELS = "finished";
	
	private SharedPreferences preferences;
	
	private LevelChooseView view;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // hide screen title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose);
        view = (LevelChooseView) findViewById(R.id.choose);
        
        preferences = getSharedPreferences("panda_config", MODE_PRIVATE);
        String finishedArray = preferences.getString(FINISHED_LEVELS, "");
        view.setFinishedLevels(finishedArray);
	}
	
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		outState.putByteArray(FINISHED_LEVELS, view.getFinishedLevels());
//	}
//	
//	@Override
//	protected void onRestoreInstanceState(Bundle savedInstanceState) {
//		super.onRestoreInstanceState(savedInstanceState);
//		view.setFinishedLevels(savedInstanceState.getByteArray(FINISHED_LEVELS));
//	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		SharedPreferences.Editor prEditor = preferences.edit();
		prEditor.putString(FINISHED_LEVELS, view.getFinishedLevels());
		prEditor.commit();
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
