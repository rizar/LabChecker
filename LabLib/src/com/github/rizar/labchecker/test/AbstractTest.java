package com.github.rizar.labchecker.test;

import com.github.rizar.labchecker.lab.MacroProcessor;
import java.io.File;
import java.util.Formatter;

/**
 *
 * @author Rizar
 */
abstract public class AbstractTest implements Test
{
    protected Formatter log = new Formatter ();

    public String getLog()
    {
        return log.toString();
    }
}
