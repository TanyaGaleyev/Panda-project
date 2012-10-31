package org.ivan.simple;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class LevelChooseActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide screen title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose);
	}
}
