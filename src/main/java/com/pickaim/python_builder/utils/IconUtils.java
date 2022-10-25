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

   /* public static org.apache.batik.gvt.GraphicsNode getSvgIcon(java.net.URL url) {
        org.apache.batik.gvt.GraphicsNode svgIcon = null;
        try {
            String xmlParser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory df = new SAXSVGDocumentFactory(xmlParser);
            SVGDocument doc = df.createSVGDocument(url.toString());
            UserAgent userAgent = new UserAgentAdapter();
            DocumentLoader loader = new DocumentLoader(userAgent);
            BridgeContext ctx = new BridgeContext(userAgent, loader);
            ctx.setDynamicState(BridgeContext.DYNAMIC);
            GVTBuilder builder = new GVTBuilder();
            svgIcon = builder.build(ctx, doc);
        } catch (Exception excp) {
            excp.printStackTrace();
        }
        return svgIcon;
    }

    public static void paintSvgIcon(java.awt.Graphics2D g, org.apache.batik.gvt.GraphicsNode svgIcon, int x, int y, double scaleX, double scaleY) {
        java.awt.geom.AffineTransform transform = new java.awt.geom.AffineTransform(scaleX, 0.0, 0.0, scaleY, x, y);
        g.setBackground(new JBColor(new Color(1f, 0f, 0f, .0f), JBColor.DARK_GRAY));
        svgIcon.setTransform(transform);
        svgIcon.paint(g);
    }*/
}
