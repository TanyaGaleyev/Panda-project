package org.ivan.simple.choose;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

import org.ivan.simple.PandaBaseActivity;
import org.ivan.simple.R;
import org.ivan.simple.welcome.StartActivity;

public class LevelChooseActivity extends PandaBaseActivity {
	public static final String LEVEL_ID = "levId";
	public static final String LEVEL_COMPLETE = "complete";
	public static final String COMPLETE_SCORE = "score";
	public static final String SET_COMPLETE = "set complete";
	public static final int FINISHED_LEVEL_ID = 1;
	public static final String FINISHED_LEVELS = "finished";
	public static final String HIGH_SCORES = "high scores";
	public static final String CONFIG = "panda_config";
	
	private SharedPreferences preferences;
	
	private LevelChooseView view;
	private int levelsSetId;

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    private boolean loading = false;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // hide screen title
        setContentView(R.layout.activity_choose);
        view = (LevelChooseView) findViewById(R.id.choose);
        
        preferences = getSharedPreferences(CONFIG, MODE_PRIVATE);
        levelsSetId = getIntent().getIntExtra(StartActivity.SET_ID, 0);
        String finishedArray = preferences.getString(FINISHED_LEVELS + levelsSetId, "");
        view.setChooseScreenProperties(levelsSetId, finishedArray);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
        setLoading(false);
		if(requestCode == FINISHED_LEVEL_ID && resultCode == RESULT_OK) {
			boolean complete = data.getBooleanExtra(LEVEL_COMPLETE, false);
			if(complete) {
				int score = data.getIntExtra(COMPLETE_SCORE, 0);
				int oldScore = view.completeCurrentLevel(score);
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
	
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		view = null;
//	}
}
