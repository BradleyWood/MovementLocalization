package localization.util;

/**
 * Created by Brad on 5/19/2017.
 */
public class Colors {

    public static int getAlpha(final int rgb) {
        return (rgb >> 24) & 0xFF;
    }
    public static int getRed(final int rgb) {
        return (rgb >> 16) & 0xFF;
    }
    public static int getGreen(final int rgb) {
        return (rgb >> 8) & 0xFF;
    }
    public static int getBlue(final int rgb) {
        return (rgb) & 0xFF;
    }

}
