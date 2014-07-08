package com.pavlukhin.acropanda.welcome;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.pavlukhin.acropanda.PandaApplication;
import com.pavlukhin.acropanda.PandaBaseActivity;
import com.pavlukhin.acropanda.R;
import com.pavlukhin.acropanda.billing.BuyPremiumDialog;
import com.pavlukhin.acropanda.choose.LevelChooseActivity;
import com.pavlukhin.acropanda.utils.PandaButtonsPanel;

public class StartActivity extends PandaBaseActivity {
	
	public static final String SET_ID = "Id of levels set";
	public static final String LAST_FINISHED_SET = "Last finished set of levels";
    public static final int PACKS_IN_ROW = 3;
    public static final int FREE_PACKS_COUNT = 4;
    //    private final String[] levelsCaptions = {"ACCESS", "BUTTON", "ZOMBIE", "SYSTEM"};
	public final int levCount = 6;
	private SparseArray<ImageView> levButtons = new SparseArray<ImageView>();

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        findViewById(R.id.activity_content).setBackgroundDrawable(app().getBackground());
        initTitle();
        initPacksButtons();
        initServiceButtons();
    }

    private void initTitle() {
        TextView caption = (TextView) findViewById(R.id.text_view);
        caption.setTypeface(app().getFontProvider().bold());
        caption.setTextSize(TypedValue.COMPLEX_UNIT_PX, app().displayHeight / 10);
    }

    private void initServiceButtons() {
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

    private float chestRatio = 173f / 225f;

    private void initPacksButtons() {
        TableLayout buttonsPane = (TableLayout) findViewById(R.id.packs_panel);
        TableRow row = null;
        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        TableRow.LayoutParams packLp = createPackLayoutParams();
        for(int i = 0; i < levCount; i++) {
            if(i % PACKS_IN_ROW == 0) {
                row = new TableRow(this);
                buttonsPane.addView(row, rowParams);
            }
            ImageView levbtn = new ImageView(this);
//            levbtn.setText(levelsCaptions[i]);
            final int id = i + 1;
            if(id <= lastFinishedSet() + 1 && canUnlockPack(id)) {
                levbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.chest_open));
                levbtn.setEnabled(true);
            } else {
                levbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.chest_close));
                // FIXME closed chest buttons should be disabled in the release version
//                levbtn.setEnabled(false);
            }
            levbtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startPack(id);
                }
            });
            row.addView(levbtn, packLp);
            levButtons.put(id, levbtn);
//        	levbtn.getLayoutParams().width = (int) (displayWidth * 0.85);
//        	levbtn.getLayoutParams().height = (int) (displayHeight * 0.20);
        }
    }

    private TableRow.LayoutParams createPackLayoutParams() {
        float availableWidth = app().displayWidth * 0.8f;
        float availableHeight = app().displayHeight * 0.55f;
        int chestWidth = (int) (availableWidth / PACKS_IN_ROW);
        int chestHeight = (int) (availableHeight / Math.ceil((float) levCount / PACKS_IN_ROW));
        if(chestWidth * chestRatio < chestHeight) {
            chestHeight = (int) (chestWidth * chestRatio);
        } else {
            chestWidth = (int) (chestHeight / chestRatio);
        }
        TableRow.LayoutParams packButtonParam = new TableRow.LayoutParams(chestWidth, chestHeight);
        int m = (int) (app().displayHeight * 0.02f);
        packButtonParam.setMargins(m, m, m, m);
        return packButtonParam;
    }


    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == 0) {
			System.out.println("Choose activity results!");
			boolean setComplete = data.getBooleanExtra(LevelChooseActivity.SET_COMPLETE, false);
            int startedSet = data.getIntExtra(SET_ID, 0);
            int packToOpenId = startedSet + 1;
            final ImageView packToOpen = levButtons.get(packToOpenId);
            if(setComplete && canUnlockPack(packToOpenId) && packToOpen != null) {
                final AnimationDrawable chestOpening =
                        app().loadAnimationFromFolder("animations/menu/pack_opening");
                chestOpening.setOneShot(true);
                packToOpen.setBackgroundDrawable(chestOpening);
                packToOpen.post(new Runnable() {
                    @Override
                    public void run() {
                        chestOpening.start();
                        packToOpen.postDelayed(new Runnable() {
                            public void run() {
                                chestOpening.stop();
                                packToOpen.setBackgroundDrawable(getResources().getDrawable(R.drawable.chest_open));
                            }
                        }, chestOpening.getNumberOfFrames() * PandaApplication.ONE_FRAME_DURATION);
                    }
                });
                packToOpen.setEnabled(true);
			}
		}
	}

    private boolean canUnlockPack(int packId) {
        return packId <= FREE_PACKS_COUNT || app().getBillingManager().checkPremium();
    }

    private void startPack(int packId) {
        if(canUnlockPack(packId)) {
            Intent intent = new Intent(this, LevelChooseActivity.class);
            intent.putExtra(SET_ID, packId);
            startActivityForResult(intent, 0);
        } else {
            new BuyPremiumDialog(this, app().getBillingManager()).show();
        }
    }

}
