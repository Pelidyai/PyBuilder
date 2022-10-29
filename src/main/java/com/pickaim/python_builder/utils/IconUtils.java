package com.pickaim.python_builder.utils;

import com.pickaim.python_builder.icons.NewSVGTranscoder;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.util.SVGConstants;

import javax.swing.*;

public class IconUtils {
    private static final float ICON_WIDTH = 19F;
    private static final float ICON_HEIGHT = 19F;

    public static ImageIcon getSVGIcon(String url){
        NewSVGTranscoder transcoder = new NewSVGTranscoder();
        TranscodingHints hints = new TranscodingHints();
        hints.put(ImageTranscoder.KEY_WIDTH, ICON_WIDTH);
        hints.put(ImageTranscoder.KEY_HEIGHT, ICON_HEIGHT);
        hints.put(ImageTranscoder.KEY_DOM_IMPLEMENTATION, SVGDOMImplementation.getDOMImplementation());
        hints.put(ImageTranscoder.KEY_DOCUMENT_ELEMENT_NAMESPACE_URI, SVGConstants.SVG_NAMESPACE_URI);
        hints.put(ImageTranscoder.KEY_DOCUMENT_ELEMENT, SVGConstants.SVG_SVG_TAG);
        hints.put(ImageTranscoder.KEY_XML_PARSER_VALIDATING, false);
        transcoder.setTranscodingHints(hints);
        try {
            transcoder.transcode(new TranscoderInput(url), null);
        } catch (TranscoderException e) {
            throw new RuntimeException(e);
        }
        return new ImageIcon(transcoder.getImage());
    }
}
