package com.pavlukhin.acropanda.achievements;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pavlukhin.acropanda.PandaBaseActivity;
import com.pavlukhin.acropanda.R;
import com.pavlukhin.acropanda.utils.PandaButtonsPanel;

import java.io.IOException;

public class AchievementsActivity extends PandaBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        findViewById(R.id.activity_content).setBackgroundDrawable(app().getBackground());
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
        achivCaption.setTextSize(TypedValue.COMPLEX_UNIT_PX, app().displayHeight / 15);
        ViewGroup.LayoutParams lp = findViewById(R.id.achiv_big_icon).getLayoutParams();
        lp.height = app().displayHeight / 8;
        lp.width = app().displayHeight / 8;

        LinearLayout achivList = (LinearLayout) findViewById(R.id.achievements_list);
        for(Achievement achiv : Achievement.values()) {
            achivList.addView(createAchievementBar(achiv));
        }
    }

    private LinearLayout createAchievementBar(Achievement achiv) {
        String iconPath = AchievementsDirectories.getIconPath(achiv);
        String title = achiv.title;
        String description = achiv.description;
        LinearLayout hp = new LinearLayout(this);
        hp.setOrientation(LinearLayout.HORIZONTAL);
//        icon.setImageBitmap(app().getImageProvider().getBitmapStrictCache(iconPath));
        ViewGroup.MarginLayoutParams margins = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // TODO adjust margins
        margins.setMargins(20, 20, 20, 20);
        hp.setLayoutParams(margins);
        try {
            ImageView icon = new ImageView(this);
            icon.setBackgroundDrawable(app().createAssetDrawable(iconPath));
            icon.setLayoutParams(new ViewGroup.LayoutParams(app().displayHeight / 5, app().displayHeight / 5));
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
