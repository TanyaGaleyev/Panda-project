package org.ivan.simple;

import org.ivan.simple.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class StartActivity extends Activity {
	
	public static final String SET_ID = "Id of levels set";
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide screen title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);
        ImageProvider.resources = getResources();
        
        final Button play1Button = (Button) findViewById(R.id.play1);
        play1Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	startGame(1);
            }
        });
        
        final Button play2Button = (Button) findViewById(R.id.play2);
        play2Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	startGame(2);
            }
        });
        
        final Button play3Button = (Button) findViewById(R.id.play3);
        play3Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	startGame(3);
            }
        });
        
        final Button scoresButton = (Button) findViewById(R.id.scores);
        scoresButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	showScores();
            }
        });
        
        final Button exitButton = (Button) findViewById(R.id.exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                exit();
            }
        });
    }
	
	private void startGame(int setId) {
		Intent intent = new Intent(this, LevelChooseActivity.class);
		intent.putExtra(SET_ID, setId);
		startActivity(intent);
	}
	
	private void showScores() {
		
	}
	
	private void exit() {
		finish();
	}
	
	
}
