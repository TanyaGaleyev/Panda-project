package com.pavlukhin.acropanda.game.pause;

import com.pavlukhin.acropanda.PandaBaseActivity;
import com.pavlukhin.acropanda.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PauseActivity extends PandaBaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        setContentView(R.layout.activity_pause);
        
		final Button resumeBtn = (Button) findViewById(R.id.resume);
		resumeBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				resumeGameActivity();
			}
		});
	}
	
	private void resumeGameActivity() {
		finish();
	}
}
