package com.github.rizar.labchecker.test;

import com.github.rizar.labchecker.lab.MacroProcessor;
import java.io.File;
import java.io.IOException;

/**
 * A common interface for all test classes.
 * @author Rizar
 */
public interface Test
{
    /**
     * Check given file
     * @return true if check succeded, false if check failed
     */
    boolean check(MacroProcessor macroProcessor, File file) throws IOException;

    /**
     * Get check verbose log.
     * @return check log.
     */
    String getLog();
}
