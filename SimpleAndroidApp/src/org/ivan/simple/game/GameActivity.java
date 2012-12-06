package org.ivan.simple.game;

import org.ivan.simple.LevelChooseActivity;
import org.ivan.simple.R;
import org.ivan.simple.game.pause.PauseActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;

public class GameActivity extends Activity {
	
	private GameView gView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_SCREEN_OFF);
//        filter.addAction(Intent.ACTION_SCREEN_ON);
//        BroadcastReceiver receiver = new ScreenReceiver();
//        registerReceiver(receiver, filter);
        // hide screen title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Intent intent = getIntent();
        int levId = intent.getIntExtra(LevelChooseActivity.LEVEL_ID, 0);
        setContentView(R.layout.activity_main);
        gView = (GameView) findViewById(R.id.game);
        gView.setLevId(levId);
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
    		restart();
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    public void restart() {
    	finish();
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra(LevelChooseActivity.LEVEL_ID, gView.getLevId());
		startActivity(intent);
    }
    
    public void switchBackToChooseActivity(boolean complete) {
		Intent resultIntent = new Intent();
		resultIntent.putExtra(LevelChooseActivity.LEVEL_COMPLETE, complete);
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}
    
    @Override
    public void onBackPressed() {
    	super.onBackPressed();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	gView.stopManager();
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
		sb.append("[" );
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
	
//	private class ScreenReceiver extends BroadcastReceiver {
//
//	    @Override
//	    public void onReceive(Context context, Intent intent) {
//	        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
//	        	gView.stopManager();
//	        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
//	        	gView.startManager();
//	        }
//	    }
//	}
	
}
