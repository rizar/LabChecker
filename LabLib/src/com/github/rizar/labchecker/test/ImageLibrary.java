package com.github.rizar.labchecker.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import loadimg.LoadImgC;
import static com.github.rizar.labchecker.lab.Constraints.*;
import loadimg.LoadImgException;

/**
 *
 * @author Rizar
 */
public class ImageLibrary
{
    private File directory;

    private HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();

    private HashMap<String, ColorSet> colorSets = new HashMap<String, ColorSet>();

    public ImageLibrary(File directory)
    {
        this.directory = directory;
    }

    public BufferedImage getImage(String fileName) throws LoadImgException
    {
        BufferedImage image = images.get(fileName);
        if (image != null)
            return image;
        else
        {
            image = new LoadImgC().getImage(new File(fileName), TEMP_FOLDER, true);
            images.put(fileName, image);
            return image;
        }
    }

    public BufferedImage getImage(File file) throws LoadImgException
    {
        return getImage(file.getAbsolutePath());
    }

    public BufferedImage getLibraryImage(String fileName) throws LoadImgException
    {
        return getImage(new File(directory, fileName).getAbsolutePath());
    }

    public BufferedImage getLibraryImage(File file) throws LoadImgException
    {
        return getLibraryImage(file.getPath());
    }

    public void unload()
    {
        images = new HashMap<String, BufferedImage>();
        System.gc();
    }

    public ColorSet getLibraryImageColorSet(String fileName) throws
            LoadImgException
    {
        return getImageColorSet(new File(directory, fileName));
    }

    ColorSet getImageColorSet(String fileName) throws LoadImgException
    {
        ColorSet colorSet = colorSets.get(fileName);
        if (colorSet != null)
            return colorSet;

        colorSet = new ColorSet();
        BufferedImage image = getImage(fileName);
        for (int x = 0; x < image.getWidth(); x++)
            for (int y = 0; y < image.getHeight(); y++)
                colorSet.addColorFromFile(normalize(image.getRGB(x, y)), fileName, x, y);
        colorSets.put(fileName, colorSet);
        return colorSet;
    }

    ColorSet getImageColorSet(File file) throws LoadImgException
    {
        return getImageColorSet(file.getAbsolutePath());
    }

    long getColorPosition(File file, int color) throws LoadImgException
    {
        BufferedImage image = getImage(file);
        for (int x = 0; x < image.getWidth(); x++)
            for (int y = 0; y < image.getHeight(); y++)
                if (normalize(image.getRGB(x, y)) == color)
                    return x + ((long)(y) << 16);
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

