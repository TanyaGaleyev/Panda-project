package org.ivan.simple;

import org.ivan.simple.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class StartActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide screen title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);
        ImageProvider.resources = getResources();
        
        final Button playButton = (Button) findViewById(R.id.play);
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	startGame();
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
	
	private void startGame() {
		Intent intent = new Intent(this, LevelChooseActivity.class);
		startActivity(intent);
	}
	
	private void showScores() {
		
	}
	
	private void exit() {
		finish();
	}
	
	
}
