package com.github.rizar.labchecker.test;

import static com.github.rizar.labchecker.lab.Constraints.*;
import com.github.rizar.labchecker.exceptions.TestException;
import com.github.rizar.labchecker.exceptions.TestParseException;
import com.github.rizar.labchecker.lab.MacroProcessor;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Formatter;
import java.util.regex.Matcher;
import com.github.rizar.labchecker.loadimage.LoadImgException;

/**
 *
 * @author Rizar
 */
public class ImageSizeTest extends AbstractTest
{
    private String heightMacro, widthMacro;

    public ImageSizeTest(String heightMacro, String widthMacro)
    {
        this.heightMacro = heightMacro;
        this.widthMacro = widthMacro;
    }

    private int expectedHeight, expectedWidth;

    private void parseMacros(MacroProcessor macroProcessor)
    {
        String heightString = macroProcessor.process(heightMacro);
        String widthString = macroProcessor.process(widthMacro);

        Matcher heightMatcher = NUMBER_PATTERN.matcher(macroProcessor.process(
                heightString));
        if (!heightMatcher.matches())
            throw new TestParseException(NUMBER_PATTERN, heightString);
        expectedHeight = Integer.parseInt(heightMatcher.group(1));

        Matcher widthMatcher = NUMBER_PATTERN.matcher(macroProcessor.process(
                widthString));
        if (!widthMatcher.matches())
            throw new TestParseException(NUMBER_PATTERN, widthString);
        expectedWidth = Integer.parseInt(widthMatcher.group(1));
    }

    public boolean check(MacroProcessor macroProcessor, ImageLibrary library,
                         File file) throws
            LoadImgException,
            TestException
    {
        log = new Log ();
        log.addMessage(Log.MessageType.INIT,
                "Performing image size test to check %s.", file.getName());
        parseMacros(macroProcessor);
        log.addMessage(Log.MessageType.INFO,
                "Width must be %d, height must be %d.",
                expectedWidth, expectedHeight);
        BufferedImage image = library.getImage(file);
        log.addMessage(Log.MessageType.INFO, "%s width and height are %d, %d.", file.getName(), image.
                getWidth(), image.getHeight());
        boolean result = image.getWidth() == expectedWidth && image.getHeight() == expectedHeight;
        if (result)
            log.addMessage(Log.MessageType.OK, "Test passed");
        else
            log.addMessage(Log.MessageType.ERROR, "Test not passed");
        return result;
    }
}
