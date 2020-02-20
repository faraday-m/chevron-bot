package net.sovkov.chevronbot;

import org.junit.Test;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageTest {
  @Test
  public void testImage() {
    List<String> strings = Arrays.asList(new String[]{"ШЕВРОНЫ"});
    String msg = ImageGenerator.setLinespaces(strings);
    BufferedImage image = ImageGenerator.textToImage(msg);
    try {
      ImageIO.write(image, "png", new File("Text.png"));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
