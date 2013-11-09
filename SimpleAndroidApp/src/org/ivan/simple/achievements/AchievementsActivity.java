package org.ivan.simple.achievements;

import android.os.Bundle;


import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ivan.simple.ImageProvider;
import org.ivan.simple.PandaApplication;
import org.ivan.simple.PandaBaseActivity;
import org.ivan.simple.R;

import java.io.File;
import java.io.IOException;

public class AchievementsActivity extends PandaBaseActivity {

    public static final String ACHIV_DIR = "achievements";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        ImageButton backBtn = (ImageButton) findViewById(R.id.achievements_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        LinearLayout achivList = (LinearLayout) findViewById(R.id.achievements_list);
        try {
            for(String achivPath: imageProvider().list(ACHIV_DIR)) {
                achivList.addView(createAchievementBar(achivPath));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void back() {
        finish();
    }

    private LinearLayout createAchievementBar(String achivPath) {
        String iconPath =
                ACHIV_DIR + File.separator + achivPath + File.separator + achivPath + ".png";
        String title = "Title";
        String description = "Description";
        LinearLayout hp = new LinearLayout(this);
        hp.setOrientation(LinearLayout.HORIZONTAL);
        ImageView icon = new ImageView(this);
        icon.setImageBitmap(imageProvider().getBitmap(iconPath));
        hp.addView(icon);

        LinearLayout textPanel = new LinearLayout(this);
        textPanel.setOrientation(LinearLayout.VERTICAL);
        TextView achivTitle = new TextView(this);
        achivTitle.setText(title);
        textPanel.addView(achivTitle);
        TextView achivDescription = new TextView(this);
        achivDescription.setText(description);
        textPanel.addView(achivDescription);
        hp.addView(textPanel);
        return hp;
    }

    private ImageProvider imageProvider() {
        return PandaApplication.getPandaApplication().getImageProvider();
    }

}
