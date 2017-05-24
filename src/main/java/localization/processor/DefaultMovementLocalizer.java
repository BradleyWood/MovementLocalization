package localization.processor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static localization.util.Colors.*;

/**
 * Created by Brad on 5/22/2017.
 */
public class DefaultMovementLocalizer extends Localizer {


    /**
     * To prevent recalculation when the images aren't updated
     */
    private List<Rectangle> rectangleCache = new ArrayList<>();

    private float pixelChangeThreshold = 0f;

    private ClusteringAlgorithm clusterer = new OPTICS();
    private BufferedImage last;
    private BufferedImage current;


    public DefaultMovementLocalizer(BufferedImage initial) {
        this.current = initial;
    }
    public void setClusteringAlgorithm(ClusteringAlgorithm clusterer) {
        this.clusterer = clusterer;
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
        List<Point> points = getPoints();

        return null;
    }
    public List<Point> getPoints() {
        if(last == null || current == null)
            return new ArrayList<>();
        List<Point> points = new ArrayList<>();

        int xStep = 6;
        int yStep = 6; // todo;
        int tallie = 0;

        int[] oldBuffer = new int[yStep];
        int[] currBuffer = new int[yStep];
        for(int x = xStep / 2; x < current.getWidth(); x+=xStep) {
            for(int y = 0; x < current.getHeight(); y+=yStep) {
                current.getRGB(x, y, 1, yStep, currBuffer, 0, 1);
                last.getRGB(x, y, 1, yStep, oldBuffer, 0, 1);
                for(int n = 0; n < currBuffer.length; n++) {
                    if(isChanged(currBuffer[n], oldBuffer[n]))
                        tallie++;
                }
                if(tallie >= yStep * 0.5f)
                    points.add(new Point(x,y));
                tallie = 0;
            }
        }

        return points;
    }
    private boolean isChanged(int rgb, int rgb2) {
        int aco = (int) (pixelChangeThreshold * 255);
        final int b2 = getBlue(rgb2);
        final int b1 = getBlue(rgb);
        if(current.getType() == BufferedImage.TYPE_BYTE_GRAY)
            return !(b1 + aco >= b2 && b1 - aco <= b2);
        final int r1 = getRed(rgb);
        final int g1 = getGreen(rgb);
        final int r2 = getRed(rgb2);
        final int g2 = getGreen(rgb2);

        if(r1 + aco >= r2 && r1 - aco <= r2)
            if(g1 + aco >= g2 && g1 - aco <= g2)
                if(b1 + aco >= b2 && b1 - aco <= b2)
                {
                    return false;
                }
        return true;
    }
}
