package localization;


import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * The panel to draw the results
 */
public class Display extends JPanel {

    private BufferedImage img = null;

    public Display() {
    }
    public BufferedImage getImage() {
        return img;
    }
    public void update(BufferedImage img) {
        this.img = img;
    }
    @Override
    public void paintComponent(Graphics g) {
        g.clearRect(0,0, getWidth(), getHeight());
        if(img != null)
            g.drawImage(img, 0, 0, this);
    }
}
