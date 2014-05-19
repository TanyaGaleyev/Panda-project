package org.ivan.simple.welcome;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.ivan.simple.PandaApplication;
import org.ivan.simple.PandaBaseActivity;
import org.ivan.simple.R;
import org.ivan.simple.choose.LevelChooseActivity;
import org.ivan.simple.utils.PandaButtonsPanel;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends PandaBaseActivity {
	
	public static final String SET_ID = "Id of levels set";
	public static final String LAST_FINISHED_SET = "Last finished set of levels";
	private final String[] levelsCaptions = {"ACCESS", "BUTTON", "ZOMBIE", "SYSTEM"};
	public final int levCount = levelsCaptions.length;
	private SparseArray<ImageView> levButtons = new SparseArray<ImageView>();

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide screen title
        setContentView(R.layout.activity_start);
        TextView caption = (TextView) findViewById(R.id.text_view);
        caption.setTypeface(app().getFontProvider().bold());
        caption.setTextSize(TypedValue.COMPLEX_UNIT_PX, app().displayHeight / 10);
//        caption.setText(String.format("max memory: %d KiB", Runtime.getRuntime().maxMemory() / 1024));

        initPacksButtons();

        View backBtn = prepare(R.drawable.back);
        View settingsBtn = prepare(R.drawable.settings);

        PandaButtonsPanel bp = (PandaButtonsPanel) findViewById(R.id.level_packs_bp);
        bp.customAddView(backBtn);
        bp.customAddView(settingsBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSettingsScreen();
            }
        });
    }

    private int lastFinishedSet() {
        return getSharedPreferences(LevelChooseActivity.CONFIG, MODE_PRIVATE)
                .getInt(LAST_FINISHED_SET, 0);
    }

    private void initPacksButtons() {
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
            if(id <= lastFinishedSet() + 1) {
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
            levButtons.put(id, levbtn);
//        	levbtn.getLayoutParams().width = (int) (displayWidth * 0.85);
//        	levbtn.getLayoutParams().height = (int) (displayHeight * 0.20);
        }
    }


    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == 0) {
			System.out.println("Choose activity results!");
			boolean setComplete = data.getBooleanExtra(LevelChooseActivity.SET_COMPLETE, false);
            int startedSet = data.getIntExtra(SET_ID, 0);
            int packToOpenId = startedSet + 1;
            final ImageView packToOpen;
            if(setComplete && (packToOpen = levButtons.get(packToOpenId)) != null) {
                final AnimationDrawable chestOpening =
                        app().loadAnimationFromFolder("animations/menu/pack_opening");
                chestOpening.setOneShot(true);
                packToOpen.setBackgroundDrawable(chestOpening);
                // TODO rake here, get rid of alpha channel manipulation, calculate chests sizes instead
                packToOpen.setImageAlpha(0);
                packToOpen.post(new Runnable() {
                    @Override
                    public void run() {
                        chestOpening.start();
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                chestOpening.stop();
                                packToOpen.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        packToOpen.setImageAlpha(255);
                                        packToOpen.setImageResource(R.drawable.chest_open);
                                        packToOpen.setBackgroundDrawable(null);
                                    }
                                });
                            }
                        }, chestOpening.getNumberOfFrames() * PandaApplication.ONE_FRAME_DURATION);
                    }
                });
                packToOpen.setEnabled(true);
			}
		}
	}

	
	private void startPack(int setId) {
		Intent intent = new Intent(this, LevelChooseActivity.class);
		intent.putExtra(SET_ID, setId);
		startActivityForResult(intent, 0);
	}

}
