package org.ivan.simple.welcome;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.ivan.simple.PandaApplication;
import org.ivan.simple.PandaBaseActivity;
import org.ivan.simple.R;
import org.ivan.simple.choose.LevelChooseActivity;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
	private List<ImageView> levButtons = new ArrayList<ImageView>();
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
        findViewById(R.id.level_packs_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.level_packs_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSettingsScreen();
            }
        });
        loaded = true;
    }

    private void initPacksButtons(int lastFinishedSet) {
        TableLayout buttonsPane = (TableLayout) findViewById(R.id.packs_panel);
        TableRow row = null;
        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        rowParams.weight = 1;
        TableRow.LayoutParams packButtonParam = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        packButtonParam.weight = 1;
        packButtonParam.setMargins(5, 5, 5, 5);
        packButtonParam.gravity = Gravity.CENTER;

        for(int i = 0; i < levCount; i++) {
            if(i % 3 == 0) {
                row = new TableRow(this);
                buttonsPane.addView(row, rowParams);
            }
            ImageView levbtn = new ImageView(this);
//            levbtn.setText(levelsCaptions[i]);
            final int id = i + 1;
            if(id <= lastFinishedSet + 1) {
                levbtn.setImageResource(R.drawable.chest_open);
                levbtn.setEnabled(true);
            } else {
                levbtn.setImageResource(R.drawable.chest_close);
                levbtn.setEnabled(false);
            }
            levbtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startPack(id);
                }
            });
            row.addView(levbtn, packButtonParam);
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
                final ImageView prevPack = levButtons.get(startedSet);
                try {
                    final AnimationDrawable chestOpening = PandaApplication.getPandaApplication()
                            .loadAnimationFromFolder("animations/menu/pack_opening");
                    chestOpening.setOneShot(true);
                    prevPack.setImageDrawable(chestOpening);
                    prevPack.post(new Runnable() {
                        @Override
                        public void run() {
                            chestOpening.start();
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    chestOpening.stop();
                                    prevPack.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            prevPack.setImageResource(R.drawable.chest_open);
                                        }
                                    });
                                }
                            }, chestOpening.getNumberOfFrames() * PandaApplication.ONE_FRAME_DURATION);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    prevPack.setImageResource(R.drawable.chest_open);
                }
                prevPack.setEnabled(true);
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
