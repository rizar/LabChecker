package com.github.rizar.labchecker.test;

import com.github.rizar.labchecker.exceptions.TestException;
import com.github.rizar.labchecker.lab.MacroProcessor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static com.github.rizar.labchecker.lab.Constraints.*;

/**
 *
 * @author Rizar
 */
public class ColorSetTest extends AbstractTest
{
    private List<String> addFileMacros = new ArrayList<String>();

    private List<String> removeFileMacros = new ArrayList<String>();

    private List<String> addColorMacros = new ArrayList<String>();

    private List<String> removeColorMacros = new ArrayList<String>();

    public ColorSetTest()
    {
    }

    public void includeAddFileMacro(String addFileMacro)
    {
        addFileMacros.add(addFileMacro);
    }

    public void includeRemoveFileMacro(String removeFileMacro)
    {
        removeFileMacros.add(removeFileMacro);
    }

    public void includeAddColorMacro(String colorMacro)
    {
        addColorMacros.add(colorMacro);
    }

    public void includeRemoveColorMacro(String colorMacro)
    {
        removeColorMacros.add(colorMacro);
    }

    private ColorSet patternColorSet, testColorSet;

    private void parseMacros(MacroProcessor macroProcessor, ImageLibrary library)
            throws IOException
    {
        patternColorSet = new ColorSet();
        for (String macro : addColorMacros)
        {
            String string = macroProcessor.process(macro);
            patternColorSet.addColor(new ColorParser(string, library).parse());
        }
        for (String macro : addFileMacros)
        {
            String fileName = macroProcessor.process(macro);
            patternColorSet.addColorSet(
                    library.getLibraryImageColorSet(fileName));
        }
        for (String macro : removeColorMacros)
        {
            String string = macroProcessor.process(macro);
            patternColorSet.removeColor(new ColorParser(string, library).parse());
        }
        for (String macro : removeFileMacros)
        {
            String fileName = macroProcessor.process(macro);
            patternColorSet.removeColorSet(library.getLibraryImageColorSet(
                    fileName));
        }
    }

    public boolean check(MacroProcessor macroProcessor, ImageLibrary library,
                         File file) throws IOException,
                                           TestException
    {
        log.format("Performing color set test for %s.\n", file.getName());
        parseMacros(macroProcessor, library);
        log.format("Pattern color set:");
        for (int c : patternColorSet.getColors())
        {
            int mask = (1 << 8) - 1;
            log.format(" (%d, %d, %d)", c & mask, (c >> 8) & mask,
                    (c >> 16) & mask);
        }
        log.format("\n");

        log.format("Color set of %s:", file.getName());
        testColorSet = library.getImageColorSet(file);
        for (int c : testColorSet.getColors())
            log.format(" (%d, %d, %d)", c & MASK8, (c >> 8) & MASK8,
                    (c >> 16) & MASK8);
        log.format("\n");

        ColorSet inPatternNotInTest = new ColorSet(patternColorSet);
        inPatternNotInTest.removeColorSet(testColorSet);
        ColorSet inTestNotInPattern = new ColorSet(testColorSet);
        inTestNotInPattern.removeColorSet(patternColorSet);

        boolean result = true;
        for (int c : inPatternNotInTest.getColors())
        {
            log.format(
                    "Color (%d, %d, %d) from pattern set is not from test set.",
                    c & MASK8, (c >> 8) & MASK8, (c >> 16) & MASK8);
            String fileName = inPatternNotInTest.getFileName(c);
            int x = inPatternNotInTest.getX(c);
            int y = inPatternNotInTest.getY(c);
            if (fileName != null)
                log.format(" You can find it at (%d, %d) in file %s.\n", x, y,
                        fileName);
            else
                log.format("\n");
            result = false;
        }
        for (int c : inTestNotInPattern.getColors())
        {
            log.format(
                    "Color (%d, %d, %d) from test set is not from pattern set. You can find it at (%d, %d).\n",
                    c & MASK8, (c >> 8) & MASK8, (c >> 16) & MASK8,
                    inTestNotInPattern.getX(c), inTestNotInPattern.getY(c));
            result = false;
        }
        log.format(result ? "Test passed.\n" : "Test not passed.\n");

        return result;
    }
}
