package org.ivan.simple;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by ivan on 13.10.13.
 */
public class FontProvider {
    private final Typeface regular;
    private final Typeface bold;
    private final Typeface ptSans;

    public FontProvider(Context context) {
        ptSans = Typeface.createFromAsset(context.getAssets(), "fonts/PTS75F.ttf");
        regular = Typeface.createFromAsset(context.getAssets(), "fonts/segoepr.ttf");
        bold = Typeface.createFromAsset(context.getAssets(), "fonts/segoeprb.ttf");
    }

    public Typeface regular() {
        return regular;
    }

    public Typeface bold() {
        return bold;
    }

    public Typeface ptSans() {
        return ptSans;
    }
}
