package com.github.rizar.labchecker.test;

import com.github.rizar.labchecker.exceptions.TestException;
import com.github.rizar.labchecker.lab.MacroProcessor;
import java.io.File;
import java.io.IOException;
import com.github.rizar.labchecker.loadimage.LoadImgException;

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
    boolean check(MacroProcessor macroProcessor, ImageLibrary library, File file) throws LoadImgException,
                                                                   TestException;

    /**
     * Get check verbose log.
     * @return check log.
     */
    Log getLog();
}
