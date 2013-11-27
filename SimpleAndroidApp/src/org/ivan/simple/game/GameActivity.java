package org.ivan.simple.game;

import org.ivan.simple.choose.LevelChooseActivity;
import org.ivan.simple.PandaBaseActivity;
import org.ivan.simple.R;
import org.ivan.simple.game.tutorial.Solutions;
import org.ivan.simple.settings.SettingsInGamePanel;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class GameActivity extends PandaBaseActivity {

    public static final String PAUSE_TITLE = "Pause";
    private GameControl gControl;
	private int levid;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        levid = intent.getIntExtra(LevelChooseActivity.LEVEL_ID, 0);
        setContentView(R.layout.activity_main);
        prepare(findViewById(R.id.game_settings)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSettingsScreen();
            }
        });
        prepare(findViewById(R.id.game_help)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTutorial();
            }
        });
        GameView gView = (GameView) findViewById(R.id.game);
        gView.setLevId(levid);
        gControl = gView.getControl();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
    	switch (item.getItemId()) {
    	case R.id.increaseFPS:
    		GameManager.changeFPS(5);
    		return true;
    	case R.id.decreaseFPS:
    		GameManager.changeFPS(-5);
    		return true;
    	case R.id.restart:
//    		restart();
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }


//    public void restart() {

    public void switchBackToChooseActivity(boolean complete, int score) {
		Intent resultIntent = new Intent();
		resultIntent.putExtra(LevelChooseActivity.LEVEL_COMPLETE, complete);
		resultIntent.putExtra(LevelChooseActivity.COMPLETE_SCORE, score);
		setResult(RESULT_OK, resultIntent);
		finish();
	}

    @Override
    public void onBackPressed() {
    	super.onBackPressed();
    }

    @Override
    protected void onPause() {
    	super.onPause();
    	gControl.stopManager();
    	System.out.println("onPause!");
    }

    @Override
    protected void onResume() {
    	super.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	gControl = null;
    }

    private void showTutorial() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        GameView nestedGame = new GameView(this);
//        nestedGame.setBackgroundResource(R.drawable.settings_border);
        nestedGame.setLevId(-1);
        nestedGame.getControl().setAutoControls(Solutions.getDemo());
        dialog.setContentView(nestedGame);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                gControl.startManager();
            }
        });
        gControl.stopManager();
        dialog.show();
    }

    @Override
    protected void gotoSettingsScreen() {
        SettingsInGamePanel settings = new SettingsInGamePanel(this, app().getSettingsModel());
        final Dialog dialog = getSettingsDialog(PAUSE_TITLE, settings);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                gControl.startManager();
            }
        });
        settings.setExitOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        settings.setReplayOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gControl.restartGame();
                dialog.hide();
            }
        });
        settings.setResumeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
                gControl.startManager();
            }
        });
        gControl.stopManager();
        dialog.show();
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
