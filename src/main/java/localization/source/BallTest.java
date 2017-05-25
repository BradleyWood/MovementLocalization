/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package localization.source;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Balls bouncing back and forth
 *
 */
public class BallTest extends InputSource {

    private final BallHandler[] handlers = new BallHandler[15];
    private final int type;

    public BallTest(int width, int height, int type) {
        super(width, height);
        this.type = type;
        Random gen = new Random();
        for(int i = 0; i < handlers.length; i++) {
            handlers[i] = new BallHandler(5 + gen.nextInt(width-10),5 + gen.nextInt(height-10));
        }
    }
    public BufferedImage getImage() {
        BufferedImage img = new BufferedImage(width, height, type);
        Graphics g = img.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.setColor(Color.BLUE);
        for (BallHandler handler : handlers) {
            handler.run();
            g.fillOval(handler.x, handler.y, 25, 25);
        }
        return img;
    }
    private class BallHandler {

        private int dirX = 4;
        private int dirY = 4;
        private int x, y;

        private BallHandler(int x, int y) {
            this.x = x;
            this.y = y;
        }
        private void run() {
            if (x + dirX + 14 < 0 || x + dirX + 14 > width)
                dirX = -dirX;
            if (y + dirY + 14 < 0 || y + dirY + 14 > height)
                dirY = -dirY;
            x += dirX;
            y += dirY;
        }
    }
}