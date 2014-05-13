package org.ivan.simple.bitmaputils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 06.05.2014.
 */
public class TextureAtlasParser {

    public static final String ATLAS_BASE = "background/";

    public TextureAtlasBackground createTextureAtlasBackground(Context context, String path)
            throws IOException, XmlPullParserException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        InputStream input = null;
        try {
//            input = context.getAssets().open(ATLAS_BASE + path);
            input = context.getAssets().open(ATLAS_BASE + "method_bg_2.xml");
            parser.setInput(input, null);
            parser.nextTag();
            return parseTextureAtlas(parser, context);
        } finally {
            if(input != null) input.close();
        }
    }

    private TextureAtlasBackground parseTextureAtlas(XmlPullParser parser, Context context) throws XmlPullParserException, IOException {
        int bgColor = 0;
        List<SubTexture> subTextures = new ArrayList<SubTexture>();
        parser.require(XmlPullParser.START_TAG, null, "TextureAtlas");
        Bitmap texturesSprite = getTexturesSprite(
                ATLAS_BASE + parser.getAttributeValue(null, "imagePath"), context);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) continue;
            String name = parser.getName();
            if (name.equals("brcolor"))
                bgColor = parseBgColor(parser);
            else if (name.equals("SubTexture"))
                subTextures.add(parseSubTexture(texturesSprite, parser));
        }
        return new TextureAtlasBackground(bgColor, subTextures);
    }

    private Bitmap getTexturesSprite(String imagePath, Context context) throws IOException {
        InputStream bmpInput = null;
        try {
            bmpInput = context.getAssets().open(imagePath);
            return BitmapFactory.decodeStream(bmpInput);
        } finally {
            if(bmpInput != null) bmpInput.close();
        }
    }

    private int parseBgColor(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "brcolor");
        int bgColor = Color.parseColor(parser.getAttributeValue(null, "color").replace("0x", "#"));
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, null, "brcolor");
        return bgColor;
    }

    private SubTexture parseSubTexture(Bitmap src, XmlPullParser parser)
            throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "SubTexture");
        int x = Integer.parseInt(parser.getAttributeValue(null, "x"));
        int y = Integer.parseInt(parser.getAttributeValue(null, "y"));
        int w = Integer.parseInt(parser.getAttributeValue(null, "width"));
        int h = Integer.parseInt(parser.getAttributeValue(null, "height"));
        float pX = Float.parseFloat(parser.getAttributeValue(null, "pivotX"));
        float pY = Float.parseFloat(parser.getAttributeValue(null, "pivotY"));
        String settingsStr = parser.getAttributeValue(null, "setting");

        Bitmap bmp = Bitmap.createBitmap(src, x, y, w, h);
        List<SubTexture.InstanceMutation> settings = new ArrayList<SubTexture.InstanceMutation>();
        for(String instance : settingsStr.substring(2, settingsStr.length() - 2).split("\\],\\[")) {
            String[] pars = instance.split(",");
            settings.add(new SubTexture.InstanceMutation(
                    Float.parseFloat(pars[0]),
                    Float.parseFloat(pars[1]),
                    Float.parseFloat(pars[2]),
                    Float.parseFloat(pars[3]),
                    Float.parseFloat(pars[4]),
                    Float.parseFloat(pars[5])
            ));
        }
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, null, "SubTexture");
        return new SubTexture(new TextureImage(bmp, pX, pY), settings);
    }
}
