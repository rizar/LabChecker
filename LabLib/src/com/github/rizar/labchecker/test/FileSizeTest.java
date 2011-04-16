package com.github.rizar.labchecker.test;

import com.github.rizar.labchecker.lab.MacroProcessor;
import java.io.File;
import java.util.regex.Pattern;
import com.github.rizar.labchecker.exceptions.TestParseException;
import java.util.regex.Matcher;
import static com.github.rizar.labchecker.lab.Constraints.*;

/**
 *
 * @author Rizar
 */
public class FileSizeTest extends AbstractTest
{
    private String sizeMacro, deviationMacro;
    private long maxSize;

    public long getMaxSize()
    {
        return maxSize;
    }
    
    private void parseMacros(MacroProcessor macroProcessor)
    {
        String sizeString = macroProcessor.process(sizeMacro);
        String deviationString = macroProcessor.process(deviationMacro);

        Matcher kbSizeMatcher = KILOBYTES_SIZE_PATTERN.matcher(sizeString);
        if (!kbSizeMatcher.matches())
            throw new TestParseException(KILOBYTES_SIZE_PATTERN, sizeString);
        int size = Integer.parseInt(kbSizeMatcher.group(1));
        Matcher kbDeviationMatcher = KILOBYTES_SIZE_PATTERN.matcher(deviationString);
        if (kbDeviationMatcher.matches())
        {
            int dev = Integer.parseInt(kbDeviationMatcher.group(1));
            maxSize = size + dev;
        }
        else
        {
            Matcher perDeviationMatcher = PERCENT_DEVIATION_PATTERN.matcher(
                    deviationString);
            if (perDeviationMatcher.matches())
            {
                double dev = Double.parseDouble(perDeviationMatcher.group(1)) / 100.0;
                maxSize = (int)((1 + dev) * size) + 1;
            }
            else
            {
                Pattern [] patterns = {KILOBYTES_SIZE_PATTERN, PERCENT_DEVIATION_PATTERN};
                throw new TestParseException(patterns, deviationString);
            }
        }
    }

    public FileSizeTest(String sizeMacro, String deviationMacro)
    {
        this.sizeMacro = sizeMacro;
        this.deviationMacro = deviationMacro;
    }

    public boolean check(MacroProcessor macroProcessor, ImageLibrary library, File file)
    {
        log.format("Performing file size test to check %s.\n", file.getName());
        parseMacros(macroProcessor);
        log.format("Parsed size and deviation. Maximum file size is %dkb.\n", maxSize);
        log.format("%s size is %dkb.\n", file.getName(), (int)(file.length() / BYTES_IN_KILOBYTE));
        boolean result =  file.length() <= maxSize * BYTES_IN_KILOBYTE;
        log.format(result ? "Test passed.\n" : "Test not passed.\n");
        return result;
    }
}
