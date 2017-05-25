package localization.source;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

/**
 * Grabs images in sequence from the directory
 *
 */
public class DirectorySource extends InputSource {

    private static final int MAX_CACHE_SIZE = 10;

    private Map<Integer, BufferedImage> imageCache = new HashMap<>(MAX_CACHE_SIZE);
    private final String directory;
    private List<File> files = new ArrayList<>();
    private int position = 0;

    /**
     *
     * @param directory The directory folder
     * @param width
     * @param height
     * @param sortByNumber
     */
    public DirectorySource(String directory, int width, int height, boolean sortByNumber) {
        super(width, height);
        this.directory = directory;
        File dir = new File(directory);
        if(dir.exists() && dir.isDirectory() && dir.listFiles().length > 0)
        files.addAll(Arrays.asList(
                dir.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith("jpg") || name.endsWith("png");
                    }
                })
        ));
        if(sortByNumber) { // numeric comparator for numbered files. String Comparison will fail!
            Collections.sort(files, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    return getNum(o1.getName()) - getNum(o2.getName());
                }

                private int getNum(String name) {
                    String num = name.substring(0, name.indexOf('.'));
                    return Integer.parseInt(num);
                }
            });
        }
        // TODO: 2/8/2017 Automatic threshold determination
    }
    @Override
    public BufferedImage getImage() {
        if(position == files.size())
            position = 0;
        //if(imageCache.containsKey(position))
        //    return imageCache.get(position);
        // TODO: 2/8/2017 Image Cache
        try {
            BufferedImage image = ImageIO.read(files.get(position++));
            //imageCache.put(position++, image);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);
    }
}