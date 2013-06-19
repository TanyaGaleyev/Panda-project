package org.ivan.simple;

import java.util.ArrayList;

import org.ivan.simple.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

public class StartActivity extends PandaBaseActivity {
	
	public static final String SET_ID = "Id of levels set";
	public static final String LAST_FINISHED_SET = "Last finished set of levels";
	private final String[] levelsCaptions = {"ACCESS", "BUTTON", "ZOMBIE", "SYSTEM"};
	public final int levCount = levelsCaptions.length;
	private ArrayList<Button> levButtons = new ArrayList<Button>();
	private int startedSet = 0;
	private boolean loaded = false;
	
	public static int DISPLAY_WIDTH;
	public static int DISPLAY_HEIGHT;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int width = getWindowManager().getDefaultDisplay().getWidth();
        int height= getWindowManager().getDefaultDisplay().getHeight();
        if(width > height) {
        	DISPLAY_WIDTH = width;
        	DISPLAY_HEIGHT = height;
        } else {
        	DISPLAY_WIDTH = height;
        	DISPLAY_HEIGHT = width;
        }
        // hide screen title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);
        ImageProvider.init(getApplicationContext());
        DisplayMetrics display = getApplicationContext().getResources().getDisplayMetrics();
        final float scale = display.density;
        
        int lastFinishedSet = 
        		getSharedPreferences(LevelChooseActivity.CONFIG, MODE_PRIVATE)
        		.getInt(LAST_FINISHED_SET, 0);
        
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
        	levbtn.getLayoutParams().width = (int) (DISPLAY_WIDTH * 0.85);
        	levbtn.getLayoutParams().height = (int) (DISPLAY_HEIGHT * 0.20);
        }
        loaded = true;
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

	
	private void startGame(int setId) {
		Intent intent = new Intent(this, LevelChooseActivity.class);
		intent.putExtra(SET_ID, setId);
		startedSet = setId;
		startActivityForResult(intent, 0);
	}

}
