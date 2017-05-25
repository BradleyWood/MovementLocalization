package localization.source;

import java.awt.image.BufferedImage;

/**
 * Abstract to support many sources, Screen, Test Case, Folders, etc.
 */
public abstract class InputSource {

    protected int width;
    protected int height;

    public InputSource(int width, int height) {
        this.width = width;
        this.height = height;
    }
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
    public abstract BufferedImage getImage();

}
