package localization;


import localization.processor.DefaultMovementLocalizer;
import localization.source.BallTest;
import localization.source.InputSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * A simple test to detect moving balls
 */
public class App
{

    private static InputSource source = new BallTest(800,700, BufferedImage.TYPE_INT_ARGB);
    private static DefaultMovementLocalizer dml;
    private static Display d;

    public static void main(String[] args) {
        d = new Display();
        dml = new DefaultMovementLocalizer(source.getImage());

        new Timer(50, timedEvent).start();

        JFrame frame = new JFrame("Movement Localization");
        frame.setSize(300,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(d);
        frame.setVisible(true);
    }
    static ActionListener timedEvent = e-> {
        BufferedImage img = source.getImage();
        dml.update(img);
        BufferedImage gg = copy(img);
        Graphics g = gg.getGraphics();
        g.setColor(Color.red);
        for(Rectangle r : dml.localize()) {
            g.drawRect(r.x, r.y, r.width, r.height);
        }
        d.update(gg);
        d.repaint();
    };
    private static BufferedImage copy(BufferedImage source) {
        BufferedImage image = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
        image.getGraphics().drawImage(source,0,0, d);
        return image;
    }

}
