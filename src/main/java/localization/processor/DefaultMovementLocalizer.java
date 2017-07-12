package localization.processor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static localization.util.Colors.*;

/**
 * Created by Brad on 5/22/2017.
 */
public class DefaultMovementLocalizer extends Localizer {


    private static final float DEFAULT_BOX_RADIUS_FACTOR = 0.01f;
    private static final float DEFAULT_BOX_CHANGE_THRESHOLD_PERCENT = 0.5f;
    private static final float DEFAULT_PIXEL_CHANGE_THRESHOLD = 0.1f;
    private static final int DEFAULT_RANGE_THRESHOLD = 24;
    private static final int DEFAULT_MIN_RECTS_CHANGED = 6;

    private BufferedImage last = null;
    private BufferedImage current = null;

    private List<Rectangle> aoiCache = null;
    private List<Rectangle> movementCache = null;

    public static float boxRadiusFactor = DEFAULT_BOX_RADIUS_FACTOR;
    public static float boxChangeThresholdPercent =  DEFAULT_BOX_CHANGE_THRESHOLD_PERCENT;
    public static float pixelChangeThreshold = DEFAULT_PIXEL_CHANGE_THRESHOLD;
    public static int rangeThreshold = DEFAULT_RANGE_THRESHOLD;
    public static int minRectsChanged = DEFAULT_MIN_RECTS_CHANGED;

    public DefaultMovementLocalizer(BufferedImage initial) {
        this.current = initial;
    }

    /**
     * Updates the image processor with the newest image
     *
     * @param current The newest the image to process
     */
    public void update(BufferedImage current) {
        if(current.getWidth() != this.current.getWidth() ||
                current.getHeight() != this.current.getHeight())
            return;
        last = this.current;
        this.current = current;
        aoiCache = null;
        movementCache = null;
    }

    public List<Cluster> getClusters() {
        List<Rectangle> aoi = getAreasOfInterest();

        DBScan da = new DBScan(getPoints(aoi), rangeThreshold, minRectsChanged);
        da.cluster();
        return da.getClusters();
    }
    public List<Rectangle> localize() {
        if(movementCache != null) {
            return movementCache;
        }

        movementCache = new ArrayList<>();
        List<Rectangle> aoi = getAreasOfInterest();

        boolean overloaded = (int) getXStep() * getYStep() * aoi.size() > current.getWidth() * current.getHeight() * 0.2f;

        if(!overloaded) {
            DBScan da = new DBScan(getPoints(aoi), rangeThreshold, minRectsChanged);
            da.cluster();
            movementCache.addAll(da.getClusterBounds());
        }

        return movementCache;
    }
    public int getXStep() {
        int step = (int)(boxRadiusFactor * current.getWidth());
        return step < 1 ? 1 : step;
    }
    public int getYStep() {
        int step = (int)(boxRadiusFactor * current.getHeight());
        return step < 1 ? 1 : step;
    }
    public List<Rectangle> getAreasOfInterest() {
        if(aoiCache != null)
            return aoiCache;
        if(last == null || current == null)
            return new ArrayList<>();

        aoiCache = new ArrayList<>();
        int xStep = getXStep();
        int yStep = getYStep();
        if(xStep < 1) // only occurs in really small images
            xStep = 1;
        if(yStep < 1)
            yStep = 1;

        int[] cBuffer = new int[yStep];
        int[] oBuffer = new int[yStep];
        Rectangle image = new Rectangle(current.getWidth(), current.getHeight());

        Rectangle r = new Rectangle(0, 0, xStep, yStep);
        for(int x = 0; x < current.getWidth(); x += xStep) {
            int tallie = 0;
            for(int y = 0; y < current.getHeight(); y+= yStep) {
                if(!r.contains(x + (xStep / 2), y))
                    r = new Rectangle(x, y, xStep, yStep);
                //System.out.println();
                if(!image.contains(r))
                    continue;
                current.getRGB(x + xStep/2,y, 1, yStep, cBuffer, 0, 1);
                last.getRGB(x + xStep/2,y, 1, yStep, oBuffer, 0, 1);
                for(int n = 0; n < cBuffer.length; n++) {
                    if(n < oBuffer.length && isChanged(cBuffer[n], oBuffer[n]))
                        tallie++;
                }
                if(tallie > (yStep * boxChangeThresholdPercent)) {
                    aoiCache.add(r);
                }
                tallie = 0;
            }
        }
        return aoiCache;
    }
    /**
     * Calculates a list of center points for given rectangles
     *
     * @param rectangles The rectangles
     * @return A list of the center points
     */
    public static List<Point> getPoints(List<Rectangle> rectangles) {
        return rectangles.stream().map(DefaultMovementLocalizer::getCenter).collect(Collectors.toList());
    }
    private static Point getCenter(Rectangle r) {
        return new Point(r.x + r.width/2 , r.y + r.height/2);
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
