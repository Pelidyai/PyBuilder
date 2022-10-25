package com.pickaim.python_builder.icons;

import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import java.awt.image.BufferedImage;

public class NewSVGTranscoder extends ImageTranscoder {
    private BufferedImage image = null;
    public BufferedImage createImage(int w, int h) {
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        return image;
    }

    @Override
    public void writeImage(BufferedImage img, TranscoderOutput out) {
    }
    public BufferedImage getImage() {
        return image;
    }
}
