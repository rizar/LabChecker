package com.github.rizar.labchecker.test;

import com.github.rizar.labchecker.exceptions.TestException;
import com.github.rizar.labchecker.lab.MacroProcessor;
import com.github.rizar.labchecker.loadimage.LoadImgException;
import java.io.File;

/**
 *
 * @author Rizar
 */
public class FormatTest extends AbstractTest
{
    private String formatMacro;

    public FormatTest(String formatMacro)
    {
        this.formatMacro = formatMacro;
    }

    private void parseMacros(MacroProcessor macroProcessor)
    {
        formatName = macroProcessor.process(formatMacro);
    }

    private String formatName;

    public boolean check(MacroProcessor macroProcessor, ImageLibrary library,
                         File file) throws LoadImgException, TestException
    {
        parseMacros(macroProcessor);

        log = new Log();
        log.addMessage(Log.MessageType.INIT, "Peforming format test to check %s.", file.getName());
        log.addMessage(Log.MessageType.INFO, "Format must be %s.", formatName);
        String realFormat = library.getFormat(file);
        log.addMessage(Log.MessageType.INFO, "Real format is %s.", realFormat);
        boolean result = formatName.equals(realFormat);
        if (result)
            log.addMessage(Log.MessageType.OK, "Test passed.");
        else
            log.addMessage(Log.MessageType.ERROR, "Test not passed.");
        return result;
    }

}
