package com.github.rizar.labchecker.test;

import java.util.Arrays;
import com.github.rizar.labchecker.exceptions.TestException;
import com.github.rizar.labchecker.lab.MacroProcessor;
import com.github.rizar.labchecker.loadimage.LoadImgException;
import java.io.File;
import java.util.Collections;
import static com.github.rizar.labchecker.lab.Constraints.*;

/**
 *
 * @author Rizar
 */
public class DepthTest extends AbstractTest
{
    private String depthMacro;

    public DepthTest(String depthMacro)
    {
        this.depthMacro = depthMacro;
    }

    private void parseMacros(MacroProcessor macroProcessor)
    {
        String depthString = macroProcessor.process(depthMacro);
        okDepthes = depthString.split(DEPTH_SEPARATOR);
    }

    private String [] okDepthes;

    public boolean check(MacroProcessor macroProcessor, ImageLibrary library,
                         File file) throws LoadImgException, TestException
    {
        parseMacros(macroProcessor);

        log = new Log ();
        log.addMessage(Log.MessageType.INIT, "Performing color depth test to check file %s.", file.getName());
        log.startMessage(Log.MessageType.INFO, "Depth can be");
        for (String depth : okDepthes)
            log.appendToMessage(" %s", depth);
        log.finishMessage(".");
        String realDepth = library.getDepth(file);
        log.addMessage(Log.MessageType.INFO, "Real depth is %s.", realDepth);
        boolean result = Arrays.asList(okDepthes).contains(realDepth);
        if (result)
            log.addMessage(Log.MessageType.OK, "Test passed.");
        else
            log.addMessage(Log.MessageType.ERROR, "Test not passed.");
        return result;
    }
}

