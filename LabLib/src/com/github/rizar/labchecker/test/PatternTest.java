package com.github.rizar.labchecker.test;

import java.util.Formatter;
import com.github.rizar.labchecker.parser.ColorParser;
import java.util.HashMap;
import com.github.rizar.labchecker.exceptions.TestParseException;
import java.util.regex.Matcher;
import com.github.rizar.labchecker.exceptions.TestException;
import com.github.rizar.labchecker.lab.MacroProcessor;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import loadimg.LoadImgException;
import static com.github.rizar.labchecker.lab.Constraints.*;

/**
 *
 * @author Rizar
 */
public class PatternTest extends AbstractTest
{
    private String fileMacro;

    private String seekMacro;

    private String patternRectangleMacro;

    private String testRectangleMacro;

    private class IgnoreMask
    {
        private String fileMacro, colorMacro, fileName;

        private BufferedImage image;

        private int color;

        public IgnoreMask(String fileMacro, String colorMacro)
        {
            this.fileMacro = fileMacro;
            this.colorMacro = colorMacro;
        }

        public void parseMacros(MacroProcessor macroProcessor,
                                ImageLibrary library) throws LoadImgException
        {
            fileName = macroProcessor.process(fileMacro);
            image = library.getLibraryImage(fileName);
            color = new ColorParser(macroProcessor.process(colorMacro), library).
                    parse();
        }

        public boolean ignore(int x, int y)
        {
            return ImageLibrary.normalize(image.getRGB(x, y)) == color;
        }
    }

    private List<IgnoreMask> ignoreMasks = new ArrayList<IgnoreMask>();

    public void addIgnoreMask(String fileMacro, String colorMacro)
    {
        ignoreMasks.add(new IgnoreMask(fileMacro, colorMacro));
    }

    private class ColorMapping
    {
        private String patternColorMacro, testColorMacro;

        public ColorMapping(String patternColorMacro, String testColorMacro)
        {
            this.patternColorMacro = patternColorMacro;
            this.testColorMacro = testColorMacro;
        }
    }

    private List<ColorMapping> mappings = new ArrayList<ColorMapping>();

    public PatternTest(String fileMacro, String seekMacro)
    {
        this.fileMacro = fileMacro;
        this.seekMacro = seekMacro;
    }

    public void setPatternRectangleMacro(String patternRectangleMacro)
    {
        this.patternRectangleMacro = patternRectangleMacro;
    }

    public void setTestRectangleMacro(String testRectangleMacro)
    {
        this.testRectangleMacro = testRectangleMacro;
    }

    public void addColorMapping(String patternColorMacro, String testColorMacro)
    {
        mappings.add(new ColorMapping(patternColorMacro, testColorMacro));
    }

    boolean doSeek;

    private File testFile, patternFile;

    private BufferedImage testImage, patternImage;

    private int patX1, patY1, patX2, patY2;

    private int testX1, testY1, testX2, testY2;

    private HashMap<Integer, Integer> colorsMap;

    private void parseMacros(MacroProcessor macroProcessor, ImageLibrary library)
            throws LoadImgException
    {
        patternFile = new File(macroProcessor.process(fileMacro));
        patternImage = library.getLibraryImage(patternFile);

        if (seekMacro != null)
        {
            String seekString = macroProcessor.process(seekMacro);
            if (seekString.equals(TRUE_STRING))
                doSeek = true;
            else if (!seekString.equals(FALSE_STRING))
                throw new TestParseException(
                        "Unknown value for boolean attribute \"" + seekString + "\"");
        }
        else
            doSeek = false;

        if (patternRectangleMacro != null)
        {
            String patternRectangleString = macroProcessor.process(
                    patternRectangleMacro);
            Matcher rectangleMatcher = RECTANGLE_PATTERN.matcher(
                    patternRectangleString);
            if (!rectangleMatcher.matches())
                throw new TestParseException(RECTANGLE_PATTERN,
                        patternRectangleString);
            patX1 = Integer.parseInt(rectangleMatcher.group(1));
            patY1 = Integer.parseInt(rectangleMatcher.group(2));
            patX2 = Integer.parseInt(rectangleMatcher.group(3));
            patY2 = Integer.parseInt(rectangleMatcher.group(4));
            if (testRectangleMacro == null)
            {
                testX1 = patX1;
                testY1 = patY1;
                testX2 = patX2;
                testY2 = patY2;
            }
        }
        if (testRectangleMacro != null)
        {
            String testRectangleString = macroProcessor.process(
                    testRectangleMacro);
            Matcher rectangleMatcher = RECTANGLE_PATTERN.matcher(
                    testRectangleString);
            if (!rectangleMatcher.matches())
                throw new TestParseException(RECTANGLE_PATTERN,
                        testRectangleString);
            testX1 = Integer.parseInt(rectangleMatcher.group(1));
            testY1 = Integer.parseInt(rectangleMatcher.group(2));
            testX2 = Integer.parseInt(rectangleMatcher.group(3));
            testY2 = Integer.parseInt(rectangleMatcher.group(4));
            if (patternRectangleMacro == null)
            {
                patX1 = testX1;
                patY1 = testY1;
                patX2 = testX2;
                patY2 = testY2;
            }
        }
        if (patternRectangleMacro == null && testRectangleMacro == null)
        {
            patX1 = patY1 = testX1 = testY1 = 0;
            patX2 = testX2 = testImage.getWidth() - 1;
            patY2 = testY2 = testImage.getHeight() - 1;
        }

        if (!(0 <= patX1 && patX1 <= patX2 && 0 <= patY1 && patY1 <= patY2
                && patX2 < patternImage.getWidth() && patY2 < patternImage.
                getHeight()))
            throw new TestParseException(
                    String.format(
                    "Wrong rectangle coordinates \"%d %d %d %d\" for file %s.",
                    patX1, patY1, patX2, patY2, patternFile.getName()));
        if (!(0 <= testX1 && testX1 <= testX2 && 0 <= testY1 && testY1 <= testY2
                && testX2 < testImage.getWidth() && testY2 < testImage.getHeight()))
            throw new TestParseException(
                    String.format(
                    "Wrong rectangle coordinates \"%d %d %d %d\" for file %s.",
                    testX1, testY1, testX2, testY2, testFile.getName()));

        if (!(patX2 - patX1 == testX2 - testX1 && patY2 - patY1 == testY2 - testY1) && !doSeek)
            throw new TestParseException(
                    "Pattern rectangle size isn't equal to test rectangle size");

        colorsMap = new HashMap<Integer, Integer>();
        for (ColorMapping cm : mappings)
        {
            int patternColor = new ColorParser(macroProcessor.process(
                    cm.patternColorMacro), library).parse();
            int testColor = new ColorParser(macroProcessor.process(
                    cm.testColorMacro), library).parse();
            colorsMap.put(patternColor, testColor);
        }

        for (IgnoreMask im : ignoreMasks)
            im.parseMacros(macroProcessor, library);
    }

    private static int MANY_ERRORS = 20;

    private int nChecked;

    private int doCheck(int xOffset, int yOffset, boolean printInLog)
    {
        int nErrors = 0;
        nChecked = 0;
        for (int x = patX1; x <= patX2; x++)
            for (int y = patY1; y <= patY2; y++)
            {
                int u = testX1 + x - patX1 + xOffset;
                int v = testY1 + y - patY1 + yOffset;

                boolean doIgnore = false;
                for (IgnoreMask im : ignoreMasks)
                    doIgnore |= im.ignore(u, v);
                if (doIgnore)
                    continue;

                int cXY = ImageLibrary.normalize(patternImage.getRGB(x, y));
                int cUV = ImageLibrary.normalize(testImage.getRGB(u, v));
                if (colorsMap.containsKey(cXY))
                {
                    int cMust = colorsMap.get(cXY);
                    if (cMust != cUV)
                    {
                        nErrors++;
                        if (nErrors <= MANY_ERRORS)
                        {
                            int rM = cMust & MASK8;
                            int gM = (cMust >> 8) & MASK8;
                            int bM = (cMust >> 16) & MASK8;
                            int rUV = cUV & MASK8;
                            int gUV = (cUV >> 8) & MASK8;
                            int bUV = (cUV >> 16) & MASK8;
                            if (printInLog)
                                log.addMessage(Log.MessageType.ERROR_INFO, 
                                        "Error: pixel (%d, %d) must be (%d, %d, %d) instead of (%d, %d, %d).",
                                        u, v, rM, gM, bM, rUV, gUV, bUV);
                        }
                        else if (nErrors == MANY_ERRORS + 1)
                            if (printInLog)
                                log.addMessage(Log.MessageType.ERROR_INFO,
                                        "More than %d errors, output stopped.", MANY_ERRORS);
                    }
                    nChecked++;
                }
            }
        return nErrors;
    }

    public boolean check(MacroProcessor macroProcessor, ImageLibrary library,
                         File file) throws LoadImgException,
                                           TestException
    {
        testFile = file;
        testImage = library.getImage(testFile);
        parseMacros(macroProcessor, library);

        log.clear();
        log.addMessage(Log.MessageType.INIT, "Performing pattern test for file %s.", testFile.getName());
        log.addMessage(Log.MessageType.INFO, "Pattern file: %s.", patternFile.getName());
        if (doSeek)
            log.addMessage(Log.MessageType.INFO, "Pattern search in test area turned on.");
        log.addMessage(Log.MessageType.INFO, "Pattern rectangle: x1 = %d y1 = %d x2 = %d y2 = %d.",
                patX1, patY1, patX2, patY2);
        log.addMessage(Log.MessageType.INFO, "Test rectangle: x1 = %d y1 = %d x2 = %d y2 = %d.", testX1,
                testY1, testX2, testY2);
        log.startMessage(Log.MessageType.INFO, "Color map: ");
        for (int patternColor : colorsMap.keySet())
        {
            int testColor = colorsMap.get(patternColor);
            int pr = patternColor & MASK8, pg = (patternColor >> 8) & MASK8, pb = (patternColor >> 16) & MASK8;
            int tr = testColor & MASK8, tg = (testColor >> 8) & MASK8, tb = (testColor >> 16) & MASK8;
            log.appendToMessage(" (%d, %d, %d) -> (%d, %d, %d)", pr, pg, pb, tr, tg, tb);
        }
        log.finishMessage();
        if (!ignoreMasks.isEmpty())
        {
            log.startMessage(Log.MessageType.INFO, "Ignore following masks:");
            for (IgnoreMask im : ignoreMasks)
                log.appendToMessage(" from file %s all pixels of color %s;", im.fileName, ImageLibrary.colorString(im.color));
            log.finishMessage();
        }

        if (doSeek)
        {
            for (int xOffset = 0; xOffset < testX2 - testX1 - patX2 + patX1; xOffset++)
                for (int yOffset = 0; yOffset < testY2 - testY1 - patY2 + patY2; yOffset++)
                {
                    int nErrors = doCheck(xOffset, yOffset, false);
                    if (nErrors == 0)
                    {
                        log.addMessage(Log.MessageType.OK_INFO,
                                "Found pattern rectangle with offset (%d, %d).",
                                xOffset, yOffset);
                        log.addMessage(Log.MessageType.OK_INFO, "Test passed.");
                        return true;
                    }
                }
            log.addMessage(Log.MessageType.ERROR_INFO, "Didn't find pattern rectangle.");
            log.addMessage(Log.MessageType.ERROR, "Test not passed.");
            return false;
        }
        else
        {

            int nErrors = doCheck(0, 0, true);
            log.addMessage(Log.MessageType.INFO, "%d pixels checked.", nChecked);
            if (nErrors == 0)
                log.addMessage(Log.MessageType.OK, "Test passed.");
            else
            {
                log.addMessage(Log.MessageType.ERROR_INFO, "%d errors.", nErrors);
                log.addMessage(Log.MessageType.ERROR, "Test not passed.");
            }
            return nErrors == 0;
        }
    }
}
