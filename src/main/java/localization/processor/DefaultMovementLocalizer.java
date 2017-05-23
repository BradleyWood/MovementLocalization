package localization.processor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brad on 5/22/2017.
 */
public class DefaultMovementLocalizer extends Localizer {


    /**
     * To prevent recalculation when the images aren't updated
     */
    private List<Rectangle> rectangleCache = new ArrayList<>();

    private BufferedImage last;
    private BufferedImage current;



    public DefaultMovementLocalizer(BufferedImage initial) {
        this.current = initial;
    }
    @Override
    public void update(BufferedImage image) {
        if(image.getWidth() != this.current.getWidth() ||
                image.getHeight() != this.current.getHeight())
            return;
        last = this.current;
        this.current = image;
        rectangleCache.clear();
    }

    @Override
    public List<Rectangle> localize() {
        if(!rectangleCache.isEmpty())
            return rectangleCache;


        return null;
    }
}
