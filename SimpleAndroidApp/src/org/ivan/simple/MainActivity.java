package org.ivan.simple;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class MainActivity extends Activity {
	
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide screen title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ImageProvider.resources = getResources();
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
    		finish();
    		Intent intent = new Intent(this, MainActivity.class);
    		startActivity(intent);
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
//    public void bugaga(View view) {
//    	Intent intent = new Intent(this, SecondActivity.class);
//    	EditText editText = (EditText) findViewById(R.id.edit_message);
//    	String message = editText.getText().toString();
//    	intent.putExtra(EXTRA_MESSAGE, message);
//    	startActivity(intent);
//    }
}
