package org.ivan.simple;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

public class LevelChooseActivity extends Activity {
	public static final String LEVEL_ID = "levId";
	public static final String LEVEL_COMPLETE = "complete";
	public static final String COMPLETE_SCORE = "score";
	public static final String SET_COMPLETE = "set complete";
	public static final int FINISHED_LEVEL_ID = 1;
	public static final String FINISHED_LEVELS = "finished";
	public static final String CONFIG = "panda_config";
	
	private SharedPreferences preferences;
	
	private LevelChooseView view;
	private int levelsSetId;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // hide screen title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose);
        view = (LevelChooseView) findViewById(R.id.choose);
        
        preferences = getSharedPreferences(CONFIG, MODE_PRIVATE);
        levelsSetId = getIntent().getIntExtra(StartActivity.SET_ID, 0);
        String finishedArray = preferences.getString(FINISHED_LEVELS + levelsSetId, "");
        view.setChooseScreenProperties(levelsSetId, finishedArray);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == FINISHED_LEVEL_ID && resultCode == RESULT_OK) {
			boolean complete = data.getBooleanExtra(LEVEL_COMPLETE, false);
			if(complete) {
				byte score = data.getByteExtra(COMPLETE_SCORE, (byte) 0);
				byte oldScore = view.completeCurrentLevel(score);
				if(score > oldScore) {
					SharedPreferences.Editor prEditor = preferences.edit();
					prEditor.putString(FINISHED_LEVELS + levelsSetId, view.getFinishedLevels());
					prEditor.commit();
				}
			}
			if(view.allLevelsFinished()) {
				boolean setWasNotCompleteBefore = levelsSetId > preferences.getInt(StartActivity.LAST_FINISHED_SET, 0);
				if(setWasNotCompleteBefore) {
					SharedPreferences.Editor prEditor = preferences.edit();
					prEditor.putInt(StartActivity.LAST_FINISHED_SET, levelsSetId);
					prEditor.commit();
					finish();
				}
			}
		}
	}
	
	@Override
	public void finish() {
		Intent resultIntent = new Intent();
		resultIntent.putExtra(LevelChooseActivity.SET_COMPLETE, view.allLevelsFinished());
		setResult(RESULT_OK, resultIntent);
		super.finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		view = null;
	}
}
