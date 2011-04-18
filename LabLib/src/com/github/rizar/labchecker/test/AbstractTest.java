package com.github.rizar.labchecker.test;

import com.github.rizar.labchecker.exceptions.TestException;
import com.github.rizar.labchecker.lab.MacroProcessor;
import java.io.File;
import java.util.Formatter;
import loadimg.LoadImgException;

/**
 *
 * @author Rizar
 */
abstract public class AbstractTest implements Test
{
    protected Log log = new Log();

    public Log getLog()
    {
        return log;
    }
}
