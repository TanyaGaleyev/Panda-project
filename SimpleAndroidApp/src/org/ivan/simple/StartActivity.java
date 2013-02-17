package org.ivan.simple;

import java.util.ArrayList;

import org.ivan.simple.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

public class StartActivity extends Activity {
	
	public static final String SET_ID = "Id of levels set";
	public static final String LAST_FINISHED_SET = "Last finished set of levels";
	private final String[] levelsCaptions = {"ACCESS", "BUTTON", "ZOMBIE", "SYSTEM"};
	public final int levCount = levelsCaptions.length;
	private ArrayList<Button> levButtons = new ArrayList<Button>();
	private int startedSet = 0;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide screen title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);
        ImageProvider.resources = getApplicationContext().getResources();
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        
        int lastFinishedSet = 
        		getSharedPreferences(LevelChooseActivity.CONFIG, MODE_PRIVATE)
        		.getInt(LAST_FINISHED_SET, 0);
        System.out.println(lastFinishedSet);
        
        LinearLayout buttonsPane = (LinearLayout) findViewById(R.id.buttons_pane);
        for(int i = 0; i < levCount; i++) {
        	Button levbtn = new Button(this);
        	levButtons.add(levbtn);
        	levbtn.setText(levelsCaptions[i]);
        	final int id = i + 1;
        	levbtn.setEnabled(id <= lastFinishedSet + 1);
        	levbtn.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	startGame(id);
	            }
            });
        	buttonsPane.addView(levbtn);
        	levbtn.getLayoutParams().width = (int) (400 * scale + 0.5f);
        	levbtn.getLayoutParams().height = (int) (60 * scale + 0.5f);
        }
    }
	
	private void startGame(int setId) {
		Intent intent = new Intent(this, LevelChooseActivity.class);
		intent.putExtra(SET_ID, setId);
		startedSet = setId;
		startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == 0) {
			System.out.println("Choose activity results!");
			boolean setComplete = data.getBooleanExtra(LevelChooseActivity.SET_COMPLETE, false);
			if(setComplete && startedSet != levCount) {
				levButtons.get(startedSet).setEnabled(true);
			}
		}
	}
	
	private void showScores() {
		
	}
	
	private void exit() {
		finish();
	}
	
	
}
