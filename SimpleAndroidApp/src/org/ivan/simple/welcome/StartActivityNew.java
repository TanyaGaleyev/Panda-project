package org.ivan.simple.welcome;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.ivan.simple.PandaBaseActivity;
import org.ivan.simple.R;
import org.ivan.simple.achievements.AchievementsActivity;
import org.ivan.simple.utils.PandaButtonsPanel;

import java.io.IOException;

/**
 * Created by ivan on 28.09.13.
 */
public class StartActivityNew extends PandaBaseActivity {

    public static final String ANIMATIONS_DIR = "animations/menu/panda";

    private View achivBtn;
    private View startSettings;
    private ImageView panda;
    private RelativeLayout contentPanel;
    private AnimationDrawable pandaAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startnew);
        contentPanel = (RelativeLayout) findViewById(R.id.activity_startnew);
//        Spinner spinner = (Spinner) findViewById(R.id.spinner);
//        spinner.setAdapter(new PandaImageAdapter(this, 0, R.drawable.settings, R.drawable.back));
//        spinner.setAdapter(new PandaCheckBoxAdapter(this, 0, "effects", "music"));

        Typeface bold = app().getFontProvider().bold();
        ((TextView) findViewById(R.id.acro_caption)).setTypeface(bold);
        ((TextView) findViewById(R.id.start_caption)).setTypeface(bold);

        achivBtn = prepare(R.drawable.achievement);
        startSettings = prepare(R.drawable.settings);
        PandaButtonsPanel bp = (PandaButtonsPanel) findViewById(R.id.welcome_bp);
        bp.customAddView(achivBtn);
        bp.customAddView(startSettings);

        panda = new ImageView(this);
        int width = (int) (getWindowManager().getDefaultDisplay().getWidth() * .3125f);
        int height = (int) (getWindowManager().getDefaultDisplay().getHeight() * .5f);
        int left = (int) (getWindowManager().getDefaultDisplay().getWidth() * .65f);
        int top = (int) (getWindowManager().getDefaultDisplay().getHeight() * .20f);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        layoutParams.setMargins(left, top, 0 , 0);
        panda.setLayoutParams(layoutParams);
        contentPanel.addView(panda);
        initPandaDrawable(false);
        initListeners();
    }

    private void initPandaDrawable(boolean animation) {
        if(animation) {
            try {
                if(pandaAnimation != null) return;
                pandaAnimation = app().
                        loadAnimationFromFolder(ANIMATIONS_DIR);
                pandaAnimation.setOneShot(false);
                panda.setBackgroundDrawable(pandaAnimation);
                panda.post(new Runnable() {
                    @Override
                    public void run() {
                        pandaAnimation.start();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            panda.setBackgroundResource(R.drawable.panda_icon);
        }
    }

    private void initListeners() {
        panda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPacksScreen();
            }
        });
        prepare(startSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSettingsScreen();
            }
        });
        prepare(achivBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAchievementsScreen();
            }
        });
    }

    private void gotoAchievementsScreen() {
        Intent intent = new Intent(this, AchievementsActivity.class);
        startActivity(intent);
    }

    private void gotoPacksScreen() {
        Intent intent = new Intent(this, StartActivity.class);
//        intent.putExtra(StartActivity.SET_ID, setId);
//        startedSet = setId;
        startActivity(intent);
//        startActivityForResult(intent, 0);
    }

}
