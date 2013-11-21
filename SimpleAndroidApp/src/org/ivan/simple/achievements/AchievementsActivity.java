package org.ivan.simple.achievements;

import android.graphics.drawable.Drawable;
import android.os.Bundle;


import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ivan.simple.PandaBaseActivity;
import org.ivan.simple.R;
import org.ivan.simple.utils.PandaButtonsPanel;

import java.io.File;
import java.io.IOException;

public class AchievementsActivity extends PandaBaseActivity {

    public static final String ACHIV_DIR = "achievements";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        View backBtn = prepare(R.drawable.back);
        PandaButtonsPanel bp = (PandaButtonsPanel) findViewById(R.id.achiv_bp);
        bp.customAddView(backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView achivCaption = (TextView) findViewById(R.id.achiv_caption);
        achivCaption.setTypeface(app().getFontProvider().bold());
        achivCaption.setTextSize(TypedValue.COMPLEX_UNIT_PX, DISPLAY_HEIGHT / 15);
        ViewGroup.LayoutParams lp = findViewById(R.id.achiv_big_icon).getLayoutParams();
        lp.height = DISPLAY_HEIGHT / 8;
        lp.width = DISPLAY_HEIGHT / 8;

        LinearLayout achivList = (LinearLayout) findViewById(R.id.achievements_list);
        for(Achievement achiv : Achievement.values()) {
            achivList.addView(createAchievementBar(achiv));
        }
    }

    private LinearLayout createAchievementBar(Achievement achiv) {
        String achivPath = achiv.name().toLowerCase();
        String iconPath =
                ACHIV_DIR + File.separator + achivPath + File.separator + achivPath + ".png";
        String title = achiv.title;
        String description = achiv.description;
        LinearLayout hp = new LinearLayout(this);
        hp.setOrientation(LinearLayout.HORIZONTAL);
//        icon.setImageBitmap(app().getImageProvider().getBitmap(iconPath));
        ViewGroup.MarginLayoutParams margins = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // TODO adjust margins
        margins.setMargins(20, 20, 20, 20);
        hp.setLayoutParams(margins);
        try {
            ImageView icon = new ImageView(this);
            icon.setBackgroundDrawable(
                    Drawable.createFromStream(getResources().getAssets().open(iconPath), null));
            icon.setLayoutParams(new ViewGroup.LayoutParams(DISPLAY_HEIGHT / 5, DISPLAY_HEIGHT / 5));
            hp.addView(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        LinearLayout textPanel = new LinearLayout(this);
        textPanel.setOrientation(LinearLayout.VERTICAL);
        TextView achivTitle = new TextView(this);
        achivTitle.setTypeface(app().getFontProvider().bold());
        achivTitle.setText(title);
        textPanel.addView(achivTitle);
        TextView achivDescription = new TextView(this);
        achivDescription.setTypeface(app().getFontProvider().regular());
        achivDescription.setText(description);
        textPanel.addView(achivDescription);
        hp.addView(textPanel);
        return hp;
    }

}
