package org.ivan.simple.welcome;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.ivan.simple.PandaApplication;
import org.ivan.simple.PandaBaseActivity;
import org.ivan.simple.R;
import org.ivan.simple.achievements.AchievementsActivity;

import java.io.File;
import java.io.IOException;

/**
 * Created by ivan on 28.09.13.
 */
public class StartActivityNew extends PandaBaseActivity {

    public static final String ANIMATIONS_DIR = "animations/menu/panda";
    private Typeface regular;
    private Typeface bold;

    private ImageView achievement;
    private ImageView startSettings;
    private ImageView panda;
    private RelativeLayout contentPanel;
    private AnimationDrawable pandaAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startnew);
        contentPanel = (RelativeLayout) findViewById(R.id.activity_startnew);

        achievement = (ImageView) findViewById(R.id.achievement);
        regular = PandaApplication.getPandaApplication().getFontProvider().regular();
        bold = PandaApplication.getPandaApplication().getFontProvider().bold();

        achievement = (ImageView) findViewById(R.id.achievement);
        startSettings = (ImageView) findViewById(R.id.start_settings);
        panda = new ImageView(getApplicationContext());
        int width = (int) (getWindowManager().getDefaultDisplay().getWidth() * .3125f);
        int height = (int) (getWindowManager().getDefaultDisplay().getHeight() * .5f);
        int left = (int) (getWindowManager().getDefaultDisplay().getWidth() * .65f);
        int top = (int) (getWindowManager().getDefaultDisplay().getHeight() * .20f);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        layoutParams.setMargins(left, top, 0 , 0);
        panda.setLayoutParams(layoutParams);
        contentPanel.addView(panda);
        getPandaAnimation();
        initListeners();
    }

    private AnimationDrawable getPandaAnimation() {
        try {
            if(pandaAnimation != null) return pandaAnimation;
            pandaAnimation = PandaApplication.getPandaApplication().
                    loadAnimationFromFolder("animations/menu/panda");
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
        return pandaAnimation;
    }

    private void initListeners() {
        panda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPacksScreen();
            }
        });
        startSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSettingsScreen();
            }
        });
        achievement.setOnClickListener(new View.OnClickListener() {
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

    private void gotoSettingsScreen() {

    }


    private void gotoPacksScreen() {
        Intent intent = new Intent(this, StartActivity.class);
//        intent.putExtra(StartActivity.SET_ID, setId);
//        startedSet = setId;
        startActivity(intent);
//        startActivityForResult(intent, 0);
    }
}
