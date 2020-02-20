package net.sovkov.chevronbot;

import org.springframework.util.StringUtils;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;


public class ImageGenerator {
  private static final float FONT_SIZE = 210.0f;
  private static final int THICKNESS = (int)(FONT_SIZE/28);
  private static final int TOP_MARGIN = (int)(FONT_SIZE/10);
  private static final int LEFT_MARGIN = (int)(FONT_SIZE/10);
  private static final Font FONT = Font.decode("Arial-BOLD-" + String.valueOf((int)FONT_SIZE));
  private static final HashMap<RenderingHints.Key, Object> RenderingProperties = new HashMap<>();
  private static final FontRenderContext frc = new FontRenderContext(null, true, true);
  
  static{
    RenderingProperties.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    RenderingProperties.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    RenderingProperties.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
  }
  
  static String setLinespaces(List<String> strings) {
    List<String> newStrings = new ArrayList<>();
    int size = strings.size();
    if (size == 1) return strings.get(0);
    boolean wasConcatted = false;
    for (int i = 0; i < size; i++) {
      if (wasConcatted) {
        wasConcatted = false;
        continue;
      }
      if ((strings.get(i).length() <= 3) && (i < size-1)) {
        newStrings.add(strings.get(i) + " " + strings.get(i+1));
        wasConcatted = true;
      } else {
        newStrings.add(strings.get(i));
        wasConcatted = false;
      }
    }
    String result = StringUtils.collectionToDelimitedString(newStrings,"\n");
    return result;
  }
  
  static BufferedImage textToImage(String text){
        long lineCount = new String(text.toCharArray()).chars().mapToObj(i->(char)i).filter((Character c) -> { return c.equals('\n'); }).count() + 1;
    
    Rectangle2D r2d = makeRectangle(text, lineCount);
    
    BufferedImage img = new BufferedImage((int)Math.ceil(r2d.getWidth()), (int)Math.ceil(r2d.getHeight()), BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = img.createGraphics();
    
    g2d.setRenderingHints(RenderingProperties);
    
    clearRect(g2d, img);
    drawBorder(g2d, r2d);
    appendText(g2d, r2d, text);
    
    g2d.dispose();
    
    return img;
  }
  
  private static double getFontLength(String str) {
    return FONT.getStringBounds(str,frc).getWidth();
  }
  
  private static Rectangle2D makeRectangle(String text, long lineCount) {
    //Calculate size of buffered image.
    
    
    String maxLenStr = Stream.of(text.split("\\n+")).max(Comparator.comparing(ImageGenerator::getFontLength)).get();
    Rectangle2D r2d = FONT.getStringBounds(text, frc);
    r2d.setFrame(0,0,FONT.getStringBounds(maxLenStr,frc).getWidth() + LEFT_MARGIN * 2 + THICKNESS * 2, ((FONT.getStringBounds(text,frc).getHeight()) * lineCount) + TOP_MARGIN * 2 + THICKNESS * 2) ;
    
    return r2d;
  }
  
  private static void clearRect(Graphics2D g2d, BufferedImage img) {
    g2d.setBackground(Color.BLACK);
    g2d.setColor(Color.WHITE);
    
    g2d.clearRect(0, 0, img.getWidth(), img.getHeight());
  }
  
  private static void drawBorder(Graphics2D g2d, Rectangle2D r2d) {
    
    g2d.setStroke(new BasicStroke(THICKNESS));
    g2d.setColor(new Color(167, 0, 0));
    g2d.drawRoundRect(LEFT_MARGIN - THICKNESS, TOP_MARGIN - THICKNESS, (int)r2d.getWidth() - LEFT_MARGIN , (int)r2d.getHeight() - TOP_MARGIN, 5, 5);
  }
  
  
  private static void appendText(Graphics2D g2d, Rectangle2D r2d, String text) {
    g2d.setColor(new Color(240, 240, 240));
    g2d.setFont(FONT);
  
    TextLayout tl = new TextLayout(text, FONT, new FontRenderContext(null,true,true));
    int y = TOP_MARGIN + THICKNESS - (int)tl.getLeading() - (int)tl.getDescent();
  
    for (String line : text.split("\n")) {
      Rectangle2D tempRect = FONT.getStringBounds(line, frc);
      int tempWidth = (int)(r2d.getWidth()/2 - tempRect.getWidth()/2 + THICKNESS);
      g2d.drawString(line, tempWidth, y += g2d.getFontMetrics().getHeight());
    }
  }
  
}
