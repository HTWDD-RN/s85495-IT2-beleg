import rtp.JpegFrame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class JpegDisplay extends JpegDisplayDemo {
    @Override
    BufferedImage setTransparency(BufferedImage back, BufferedImage foreground, List<Integer> list) {
        BufferedImage result = new BufferedImage(foreground.getWidth(), foreground.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        g.drawImage(foreground, 0, 0, null);

        int sliceWidth = JpegFrame.MTU / foreground.getHeight();
        int sliceHeight = JpegFrame.MTU / foreground.getWidth();

        for (Integer sliceIndex : list) {
            int x = (sliceIndex % (foreground.getWidth() / sliceWidth)) * sliceWidth;
            int y = (sliceIndex / (foreground.getWidth() / sliceWidth)) * sliceHeight;

            g.setComposite(AlphaComposite.Clear);
            g.fillRect(x, y, sliceWidth, sliceHeight);

            g.drawImage(back, x, y, x + sliceWidth, y + sliceHeight, x, y, x + sliceWidth, y + sliceHeight, null);
        }

        g.dispose();
        return result;
    }
}

