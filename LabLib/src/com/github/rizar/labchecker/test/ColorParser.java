package com.github.rizar.labchecker.test;

import java.io.IOException;
import java.util.regex.Pattern;
import com.github.rizar.labchecker.exceptions.TestParseException;
import java.util.regex.Matcher;
import static com.github.rizar.labchecker.lab.Constraints.*;

/**
 *
 * @author Rizar
 */
public class ColorParser
{
    private String colorString;
    private ImageLibrary library;

    public ColorParser(String colorString, ImageLibrary library)
    {
        this.colorString = colorString;
        this.library = library;
    }

    public int parse() throws IOException
    {
        Matcher colorMatcher = COLOR_PATTERN.matcher(colorString);
        if (colorMatcher.matches())
        {
            int red = Integer.parseInt(colorMatcher.group(1));
            int green = Integer.parseInt(colorMatcher.group(2));
            int blue = Integer.parseInt(colorMatcher.group(3));
            return red + (green << 8) + (blue << 16);
        }

        Matcher colorInFileMatcher = COLOR_IN_FILE_PATTERN.matcher(colorString);
        if (colorInFileMatcher.matches())
        {
            String file = colorInFileMatcher.group(1);
            int x = Integer.parseInt(colorInFileMatcher.group(2));
            int y = Integer.parseInt(colorInFileMatcher.group(3));
            return ImageLibrary.normalize(library.getImage(file).getRGB(x, y));
        }

        Pattern [] patterns = new Pattern [] {COLOR_PATTERN, COLOR_IN_FILE_PATTERN};
        throw new TestParseException(patterns, colorString);
    }
}
