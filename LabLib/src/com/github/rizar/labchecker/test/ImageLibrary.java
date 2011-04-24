package com.github.rizar.labchecker.test;

import com.github.rizar.labchecker.loadimage.LoadImgBufferedImageContainer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import com.github.rizar.labchecker.loadimage.LoadImgC;
import static com.github.rizar.labchecker.lab.Constraints.*;
import com.github.rizar.labchecker.loadimage.LoadImgException;

/**
 *
 * @author Rizar
 */
public class ImageLibrary
{
    private File directory;

    private HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();

    private HashMap<String, String> formats = new HashMap<String, String>();

    private HashMap<String, String> depthes = new HashMap<String, String>();

    private class Area
    {
        String fileName;

        int x1, y1, x2, y2;

        public Area(String fileName, int x1, int y1, int x2, int y2)
        {
            this.fileName = fileName;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        @Override
        public int hashCode()
        {
            return fileName.hashCode() + x1 + y1 + x2 + y2;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final Area other = (Area) obj;
            if ((this.fileName == null) ? (other.fileName != null) : !this.fileName.
                    equals(other.fileName))
                return false;
            if (this.x1 != other.x1)
                return false;
            if (this.y1 != other.y1)
                return false;
            if (this.x2 != other.x2)
                return false;
            if (this.y2 != other.y2)
                return false;
            return true;
        }
    };

    private HashMap<Area, ColorSet> colorSets = new HashMap<Area, ColorSet>();

    public ImageLibrary(File directory)
    {
        this.directory = directory;
    }

    private LoadImgBufferedImageContainer getImageContainer(String fileName)
            throws LoadImgException
    {
        return new LoadImgC().getImage(new File(fileName), TEMP_FOLDER, false);
    }

    private BufferedImage getImageNoCashe(String fileName)
            throws LoadImgException
    {
        LoadImgBufferedImageContainer container = getImageContainer(fileName);
        return container.image;
    }

    public BufferedImage getImage(String fileName) throws LoadImgException
    {
        //TODO load format and depth
        BufferedImage image = images.get(fileName);
        if (image == null)
        {
            image = getImageNoCashe(fileName);
            images.put(fileName, image);
        }
        return image;
    }

    public BufferedImage getImage(File file) throws LoadImgException
    {
        return getImage(file.getAbsolutePath());
    }

    public BufferedImage getLibraryImage(String fileName) throws
            LoadImgException
    {
        return getImage(new File(directory, fileName).getAbsolutePath());
    }

    public BufferedImage getLibraryImage(File file) throws LoadImgException
    {
        return getLibraryImage(file.getPath());
    }

    public void releaseImage(String fileName)
    {
        images.remove(fileName);
    }

    public void releaseImage(File file)
    {
        releaseImage(file.getAbsolutePath());
    }

    public void unload()
    {
        images = new HashMap<String, BufferedImage>();
        System.gc();
    }

    public ColorSet getLibraryImageColorSet(String fileName) throws
            LoadImgException
    {
        File file = new File(directory, fileName);
        BufferedImage image = getImage(file);
        return getImageColorSet(file, 0, 0, image.getWidth() - 1, image.
                getHeight() - 1);
    }

    ColorSet getImageColorSet(String fileName, int x1, int y1, int x2, int y2)
            throws LoadImgException
    {
        Area area = new Area(fileName, x1, y1, x2, y2);
        ColorSet colorSet = colorSets.get(area);
        if (colorSet != null)
            return colorSet;

        colorSet = new ColorSet();
        BufferedImage image = getImage(fileName);
        for (int x = x1; x <= x2; x++)
            for (int y = y1; y <= y2; y++)
                colorSet.addColorFromFile(normalize(image.getRGB(x, y)),
                        fileName, x, y);
        colorSets.put(area, colorSet);
        return colorSet;
    }

    ColorSet getImageColorSet(File file, int x1, int y1, int x2, int y2) throws
            LoadImgException
    {
        return getImageColorSet(file.getAbsolutePath(), x1, y1, x2, y2);
    }

    public String getFormat(String fileName) throws LoadImgException
    {
        getImage(fileName);
        String[] answer =
        {
            "", "pcx", "tiff", "gif", "png"
        };
        return answer[fileName.charAt(fileName.length() - 12) - '0'];
        //return formats.get(fileName);
    }

    public String getFormat(File file) throws LoadImgException
    {
        return getFormat(file.getAbsolutePath());
    }

    public String getDepth(String fileName) throws LoadImgException
    {
        LoadImgBufferedImageContainer container = getImageContainer(fileName);
        return Byte.toString(container.detailedBitDepth);
    }

    public String getDepth(File file) throws LoadImgException
    {
        return getDepth(file.getAbsolutePath());
    }

    long getColorPosition(File file, int color) throws LoadImgException
    {
        BufferedImage image = getImage(file);
        for (int x = 0; x < image.getWidth(); x++)
            for (int y = 0; y < image.getHeight(); y++)
                if (normalize(image.getRGB(x, y)) == color)
                    return x + ((long) (y) << 16);
        return -1;
    }

    public static int normalize(int c)
    {
        int r = (c >> 16) & MASK8;
        int g = (c >> 8) & MASK8;
        int b = c & MASK8;
        return r + (g << 8) + (b << 16);
    }

    public static String colorString(int c)
    {
        int r = c & MASK8;
        int g = (c >> 8) & MASK8;
        int b = (c >> 16) & MASK8;
        return String.format("(%d, %d, %d)", r, g, b);
    }
}
