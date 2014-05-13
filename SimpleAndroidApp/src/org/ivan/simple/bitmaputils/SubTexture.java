package org.ivan.simple.bitmaputils;

import java.util.List;

/**
 * Created by ivan on 05.05.2014.
 */
public class SubTexture {
    final TextureImage textureImage;
    final List<InstanceMutation> settings;

    public SubTexture(TextureImage textureImage, List<InstanceMutation> settings) {
        this.textureImage = textureImage;
        this.settings = settings;
    }

    static class InstanceMutation {
        final float left;
        final float top;
        final float width;
        final float height;
        final float alpha;
        final float rotate;

        InstanceMutation(float left, float top, float width, float height, float alpha, float rotate) {
            this.left = left;
            this.top = top;
            this.width = width;
            this.height = height;
            this.alpha = alpha;
            this.rotate = rotate;
        }
    }
}
