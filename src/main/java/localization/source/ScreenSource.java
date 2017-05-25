/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package localization.source;


import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

/**
 * The screen source; takes screenshots
 */
public class ScreenSource extends InputSource {

    private Robot robot;

    public ScreenSource(int width, int height) throws AWTException {
        super(width, height);
        robot = new Robot();

    }
    @Override
    public BufferedImage getImage() {
        return robot.createScreenCapture(new Rectangle(0, 0, width, height));
    }
}
