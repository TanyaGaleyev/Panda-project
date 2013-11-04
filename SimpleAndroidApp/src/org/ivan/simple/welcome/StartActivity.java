package org.ivan.simple.welcome;

import java.util.ArrayList;
import java.util.List;

import org.ivan.simple.PandaApplication;
import org.ivan.simple.PandaBaseActivity;
import org.ivan.simple.R;
import org.ivan.simple.choose.LevelChooseActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class StartActivity extends PandaBaseActivity {
	
	public static final String SET_ID = "Id of levels set";
	public static final String LAST_FINISHED_SET = "Last finished set of levels";
	private final String[] levelsCaptions = {"ACCESS", "BUTTON", "ZOMBIE", "SYSTEM"};
	public final int levCount = levelsCaptions.length;
	private List<Button> levButtons = new ArrayList<Button>();
	private int startedSet = 0;
	private boolean loaded = false;
	
	public static int DISPLAY_WIDTH;
	public static int DISPLAY_HEIGHT;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Point size = PandaApplication.windowSize(this);
        DISPLAY_WIDTH = size.x;
        DISPLAY_HEIGHT = size.y;
        // hide screen title
        setContentView(R.layout.activity_start);
        TextView caption = (TextView) findViewById(R.id.text_view);
        caption.setTypeface(PandaApplication.getPandaApplication().getFontProvider().bold());
        caption.setText(String.format("max memory: %d KiB", Runtime.getRuntime().maxMemory() / 1024));

        int lastFinishedSet = getSharedPreferences(LevelChooseActivity.CONFIG, MODE_PRIVATE)
                .getInt(LAST_FINISHED_SET, 0);

        initPacksButtons(lastFinishedSet);
        loaded = true;
    }

    private void initPacksButtons(int lastFinishedSet) {
        LinearLayout buttonsPane = (LinearLayout) findViewById(R.id.packs_panel);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT
        );
//        lp.addRule(RelativeLayout.CENTER_VERTICAL);
//        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        LinearLayout row = null;
        for(int i = 0; i < levCount; i++) {
            if(i % 2 == 0) {
                row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                buttonsPane.addView(row, lp);
            }
            Button levbtn = new Button(this);
            levbtn.setText(levelsCaptions[i]);
            final int id = i + 1;
            levbtn.setEnabled(id <= lastFinishedSet + 1);
            levbtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startPack(id);
                }
            });
            row.addView(levbtn, lp);
            levButtons.add(levbtn);
//        	levbtn.getLayoutParams().width = (int) (DISPLAY_WIDTH * 0.85);
//        	levbtn.getLayoutParams().height = (int) (DISPLAY_HEIGHT * 0.20);
        }
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

	
	private void startPack(int setId) {
		Intent intent = new Intent(this, LevelChooseActivity.class);
		intent.putExtra(SET_ID, setId);
		startedSet = setId;
		startActivityForResult(intent, 0);
	}

}
