package org.ivan.simple.welcome;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.ivan.simple.PandaBaseActivity;
import org.ivan.simple.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by ivan on 28.09.13.
 */
public class StartActivityNew extends PandaBaseActivity {

    private Typeface regular;
    private Typeface bold;

    private ImageView achievement;
    private ImageView startSettings;
    private ImageView panda;
    private RelativeLayout contentPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startnew);
        contentPanel = (RelativeLayout) findViewById(R.id.activity_startnew);


        regular = Typeface.createFromAsset(getAssets(), "fonts/segoepr.ttf");
        bold = Typeface.createFromAsset(getAssets(), "fonts/segoeprb.ttf");

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
        try {
            final AnimationDrawable pandaAnimation = new AnimationDrawable();
            String pandaAnimationFolder = "animations/menu/panda";
            String[] frameNames = getAssets().list(pandaAnimationFolder);
            for(String frameName : frameNames) {
                System.out.println(frameName);
                pandaAnimation.addFrame(
                        Drawable.createFromStream(
                                getAssets().open(pandaAnimationFolder + File.separator +  frameName),
                                null),
                        40);
            }
            pandaAnimation.setOneShot(false);
            panda.setBackgroundDrawable(pandaAnimation);
//            panda.setBackgroundDrawable(getResources().getDrawable(R.drawable.achievement));
            panda.post(new Runnable() {
                @Override
                public void run() {
                    pandaAnimation.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        initListeners();
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
