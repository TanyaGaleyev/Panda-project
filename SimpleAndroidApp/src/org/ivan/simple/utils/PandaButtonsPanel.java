package org.ivan.simple.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ivan.simple.R;

/**
 * Created by Ivan on 20.11.13.
 */
public class PandaButtonsPanel extends LinearLayout {
    private LinearLayout btnsContainer;
    private ImageView corner;
    public PandaButtonsPanel(Context context) {
        super(context);
        init();
    }

    public PandaButtonsPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        btnsContainer = new LinearLayout(getContext());
        btnsContainer.setOrientation(HORIZONTAL);
        btnsContainer.setBackgroundResource(R.drawable.bp_fill);
        addView(btnsContainer);
        corner = new ImageView(getContext());
        corner.setBackgroundResource(R.drawable.corner);
        addView(corner);
    }

    public void customAddView(View child) {
        btnsContainer.addView(child);
        getLayoutParams().height = child.getLayoutParams().height;
        corner.getLayoutParams().width = getLayoutParams().height / 2;
    }
}
