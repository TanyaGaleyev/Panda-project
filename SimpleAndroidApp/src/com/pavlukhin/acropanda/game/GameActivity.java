package com.pavlukhin.acropanda.game;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.pavlukhin.acropanda.PandaApplication;
import com.pavlukhin.acropanda.PandaBaseActivity;
import com.pavlukhin.acropanda.R;
import com.pavlukhin.acropanda.choose.LevelChooseActivity;
import com.pavlukhin.acropanda.game.dialogs.LoseDialog;
import com.pavlukhin.acropanda.game.scores.LevelMetrics;
import com.pavlukhin.acropanda.game.scores.Scores;
import com.pavlukhin.acropanda.game.tutorial.MessageTutorialGame;
import com.pavlukhin.acropanda.game.tutorial.Solutions;
import com.pavlukhin.acropanda.game.tutorial.TutorialMessages1;
import com.pavlukhin.acropanda.game.tutorial.TutorialMessages12;
import com.pavlukhin.acropanda.settings.SettingsInGamePanel;

public class GameActivity extends PandaBaseActivity {

    public static final String PAUSE_TITLE = "Pause";
    private GameControl gControl;
    private Dialog tutorialDialog;
    private LoseDialog loseDialog;
    public static final String LEVELS_META = "com.pavlukhin.acropanda.level_meta";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int levid = intent.getIntExtra(LevelChooseActivity.LEVEL_ID, 0);
        int packId = intent.getIntExtra(LevelChooseActivity.PACK_ID, 0);
        int inPackPos = intent.getIntExtra(LevelChooseActivity.IN_PACK_POS, 0);
        setContentView(R.layout.activity_main);
        View settingsBtn = prepare(findViewById(R.id.game_settings));
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSettingsScreen();
            }
        });
        View helpBtn = prepare(findViewById(R.id.game_help));
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTutorial(0);
            }
        });
        RelativeLayout contentPanel = (RelativeLayout) findViewById(R.id.game_content_panel);
        GameView gView = levid == 1 ? new MessageTutorialGame(this, new TutorialMessages1())
                : levid == 12 ? new MessageTutorialGame(this, new TutorialMessages12()) : new GameView(this);
        contentPanel.addView(gView);
        gControl = gView.getControl();
        gControl.setLevId(levid, packId, inPackPos);
        settingsBtn.bringToFront();
        helpBtn.bringToFront();
        app().getSettingsModel().registerControlChangeObserver(gControl);
        settingsDialog.setTitle(PAUSE_TITLE);
        initLoseDialog();
        // FIXME this log info should be covered
        Log.w(PandaApplication.LOG_TAG, levelMeta(levid).getAll().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// FIXME we need get rid of this code in the release version
    	switch (item.getItemId()) {
    	case R.id.increaseFPS:
    		GameManager.changeFPS(5);
    		return true;
    	case R.id.decreaseFPS:
    		GameManager.changeFPS(-5);
    		return true;
    	case R.id.restart:
            gControl.restartGame();
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }

    public void switchBackToChooseActivity(boolean complete, LevelMetrics metrics) {
        updateMetrics(complete, metrics);
		Intent resultIntent = new Intent();
		resultIntent.putExtra(LevelChooseActivity.LEVEL_COMPLETE, complete);
		resultIntent.putExtra(LevelChooseActivity.COMPLETE_SCORE, metrics.getScore());
		setResult(RESULT_OK, resultIntent);
		finish();
	}

    private void updateMetrics(boolean complete, LevelMetrics metrics) {
        if(complete)
            submitNewScore(metrics);
    }

    public void updateAttempts() {
        SharedPreferences levelMeta = levelMeta(gControl.levId);
        if(levelMeta.getInt("ice_creams", Scores.NO_SCORE) == Scores.NO_SCORE) {
            int attempts = levelMeta.getInt("attempts", 0);
            levelMeta.edit().putInt("attempts", attempts + 1).commit();
        }
    }

    private void submitNewScore(LevelMetrics metrics) {
        SharedPreferences levelMeta = levelMeta(gControl.levId);
        int oldScore = levelMeta.getInt("ice_creams", Scores.NO_SCORE);
        if(Scores.better(metrics.getScore(), oldScore)) {
            levelMeta.edit()
                    .putInt("ice_creams", metrics.getScore())
                    .putInt("prizes", metrics.getPrizes())
                    .putLong("time", metrics.getTime())
                    .putInt("steps", metrics.getSteps())
                    .commit();
        }
    }

    public SharedPreferences levelMeta(int levid) {
        return getSharedPreferences(LEVELS_META + levid, MODE_PRIVATE);
    }

    @Override
    protected void onPause() {
    	super.onPause();
    	gControl.stopManager();
        Log.d(PandaApplication.LOG_TAG, "onPause!");
    }

    private boolean firstRun = true;
    @Override
    protected void onResume() {
    	super.onResume();
        if(firstRun) {
            firstRun = false;
        } else {
            gotoSettingsScreen();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
    	super.onDestroy();
        app().getSettingsModel().unregisterControlChangeObserver(gControl);
        gControl.releaseResources();
        loseDialog.cancel();
    }

    public void startTutorial(int type) {
        tutorialDialog = new Dialog(this);
        tutorialDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        tutorialDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        GameView nestedGame = new GameView(this);
        nestedGame.getControl().setLevId(-1, -1, -1);
        nestedGame.getControl().setAutoControls(Solutions.getDemo(type));
        tutorialDialog.setContentView(R.layout.tutorial_dialog);
        ((RelativeLayout) tutorialDialog.findViewById(R.id.tutorial_content_panel)).addView(nestedGame);
        Button continueBtn = (Button) tutorialDialog.findViewById(R.id.continue_button);
        continueBtn.setTypeface(app().getFontProvider().regular());
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTutorial();
            }
        });
        continueBtn.bringToFront();
        tutorialDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                gControl.startManager();
            }
        });
        gControl.stopManager();
        tutorialDialog.show();
    }

    public void stopTutorial() {
        tutorialDialog.cancel();
    }

    @Override
    protected void gotoSettingsScreen() {
        if(loseDialog.isShowing()) return;
        SettingsInGamePanel settings = new SettingsInGamePanel(this, app().getSettingsModel());
        settingsDialog.setContentView(settings);
        settingsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(!isFinishing())
                    gControl.startManager();
            }
        });
        settings.setExitOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                settingsDialog.cancel();
            }
        });
        settings.setReplayOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsDialog.cancel();
                gControl.view.post(new Runnable() {
                    @Override
                    public void run() {
                        gControl.restartGame();
                    }
                });
            }
        });
        settings.setResumeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsDialog.cancel();
            }
        });
        gControl.stopManager();
        settingsDialog.show();
    }

    public void showLoseDialog() {
        if(settingsDialog.isShowing()) return;
        loseDialog.show();
    }

    private void initLoseDialog() {
        loseDialog = new LoseDialog(this);
        loseDialog.setReplayOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gControl.restartGame();
                gControl.view.post(new Runnable() {
                    @Override
                    public void run() {
                        loseDialog.cancel();
                    }
                });
            }
        });
        loseDialog.setBackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loseDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK)
                    finish();
                return true;
            }
        });
    }

    /** Show an event in the LogCat view, for debugging */
	private void dumpEvent(MotionEvent event) {
		String names[] = { "DOWN" , "UP" , "MOVE" , "CANCEL" , "OUTSIDE" ,
		"POINTER_DOWN" , "POINTER_UP" , "7?" , "8?" , "9?" };
		StringBuilder sb = new StringBuilder();
		int action = event.getAction();
		int actionCode = event.getActionMasked();// & MotionEvent.ACTION_MASK;
		sb.append("event ACTION_" ).append(names[actionCode]);
		if (actionCode == MotionEvent.ACTION_POINTER_DOWN
		|| actionCode == MotionEvent.ACTION_POINTER_UP) {
		sb.append("(pid " ).append(
		action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
		sb.append(")" );
		}
		sb.append("[");
		for (int i = 0; i < event.getPointerCount(); i++) {
		sb.append("#" ).append(i);
		sb.append("(pid " ).append(event.getPointerId(i));
		sb.append(")=" ).append((int) event.getX(i));
		sb.append("," ).append((int) event.getY(i));
		if (i + 1 < event.getPointerCount())
		sb.append(";" );
		}
		sb.append("]" );
		if(actionCode == MotionEvent.ACTION_MOVE) return;
		Log.d("DumpEvent", sb.toString());
	}

}
