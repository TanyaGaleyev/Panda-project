package org.ivan.simple.game.pause;

import org.ivan.simple.PandaBaseActivity;
import org.ivan.simple.R;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class PauseActivity extends PandaBaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
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
