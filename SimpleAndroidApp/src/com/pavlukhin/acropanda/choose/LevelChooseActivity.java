package com.pavlukhin.acropanda.choose;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.google.android.gms.ads.AdView;
import com.pavlukhin.acropanda.PandaBaseActivity;
import com.pavlukhin.acropanda.R;
import com.pavlukhin.acropanda.achievements.Achievement;
import com.pavlukhin.acropanda.achievements.AchievementsDirectories;
import com.pavlukhin.acropanda.game.GameActivity;
import com.pavlukhin.acropanda.game.scores.Scores;
import com.pavlukhin.acropanda.utils.PandaButtonsPanel;
import com.pavlukhin.acropanda.welcome.StartActivity;

public class LevelChooseActivity extends PandaBaseActivity {
	public static final String LEVEL_ID = "levId";
	public static final String PACK_ID = "packId";
	public static final String IN_PACK_POS = "inPackPos";
	public static final String LEVEL_COMPLETE = "complete";
	public static final String COMPLETE_SCORE = "score";
	public static final String SET_COMPLETE = "set complete";
	public static final int FINISHED_LEVEL_ID = 1;
	public static final String FINISHED_LEVELS = "finished";
	public static final String HIGH_SCORES = "high scores";
	public static final String PACK_PREFS = "com.pavlukhin.acropanda.pack_prefs";

    private SharedPreferences packPreferences;
	
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
        
        packPreferences = getSharedPreferences(PACK_PREFS, MODE_PRIVATE);
        levelsSetId = getIntent().getIntExtra(StartActivity.SET_ID, 0);
        String finishedArray = packPreferences.getString(FINISHED_LEVELS + levelsSetId, "");
        view.setChooseScreenProperties(levelsSetId, finishedArray);
        View backBtn = prepare(R.drawable.back);
        View settingsBtn = prepare(R.drawable.settings);
        PandaButtonsPanel bp = (PandaButtonsPanel) findViewById(R.id.choose_bp);
        bp.customAddView(backBtn);
        bp.customAddView(settingsBtn);
//        View spec = prepare(R.drawable.help);
//        bp.customAddView(spec);
//        spec.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startLevel(777, 777);
//            }
//        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSettingsScreen();
            }
        });
        loadAd((AdView) findViewById(R.id.adView));
	}

    protected void startLevel(int levId, int pos) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(LEVEL_ID, levId);
        intent.putExtra(PACK_ID, levelsSetId);
        intent.putExtra(IN_PACK_POS, pos);
        startActivityForResult(intent, FINISHED_LEVEL_ID);
    }

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
        setLoading(false);
		if(requestCode == FINISHED_LEVEL_ID && resultCode == RESULT_OK) {
			boolean complete = data.getBooleanExtra(LEVEL_COMPLETE, false);
            if(complete) {
                submitNewScore(
                        data.getIntExtra(IN_PACK_POS, 1),
                        data.getIntExtra(COMPLETE_SCORE, 0));
            }
//            Achievement achievement = Achievement.NEAT;//data.getBooleanExtra(ACHIV, false);
            Achievement achievement = null;
            if(achievement != null) {
                final Dialog achivDialog = new Dialog(LevelChooseActivity.this);
                achivDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                ImageView winAchivView = new ImageView(this);
                final AnimationDrawable winAchivAnimation = app().loadAnimationFromFolder(
                                AchievementsDirectories.getOpeningDir(achievement));
                winAchivView.setBackgroundDrawable(winAchivAnimation);
                winAchivView.post(new Runnable() {
                    @Override
                    public void run() {
                        winAchivAnimation.start();
                    }
                });
                achivDialog.setContentView(winAchivView);
                achivDialog.show();
                winAchivView.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                achivDialog.cancel();
                                tryCompletePack();
                            }
                        }, 2500
                );
            } else {
                tryCompletePack();
            }
        }
	}

    private void tryCompletePack() {
        if(view.allLevelsFinished()) {
            boolean setWasNotCompleteBefore =
                    levelsSetId > packPreferences.getInt(StartActivity.LAST_FINISHED_SET, 0);
            if(setWasNotCompleteBefore) {
                packPreferences.edit().putInt(StartActivity.LAST_FINISHED_SET, levelsSetId).commit();
                finishComplete();
            }
        }
    }

    private void submitNewScore(int inPackPos, int score) {
        int oldScore = view.completeLevel(inPackPos, score);
        if(Scores.better(score, oldScore)) {
            packPreferences.edit()
                    .putString(FINISHED_LEVELS + levelsSetId, view.getFinishedLevels()).commit();
        }
    }

    public void finishComplete() {
		Intent resultIntent = new Intent();
		resultIntent.putExtra(LevelChooseActivity.SET_COMPLETE, true);
        resultIntent.putExtra(StartActivity.SET_ID, levelsSetId);
		setResult(RESULT_OK, resultIntent);
		finish();
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        view.releaseResources();
    }
}
