package com.github.rizar.labchecker.test;

import com.github.rizar.labchecker.lab.MacroProcessor;
import java.io.File;

/**
 *
 * @author Rizar
 */
abstract public class AbstractTest implements Test
{
    public boolean check(MacroProcessor macroProcessor, File file)
    {
        //TODO check
        return true;
    }

    public String getLog()
    {
        //TODO getLog
        return "abstract test future log\n";
    }
}
