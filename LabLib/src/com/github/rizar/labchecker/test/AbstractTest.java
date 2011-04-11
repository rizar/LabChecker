package com.github.rizar.labchecker.test;

import java.io.File;

/**
 *
 * @author Rizar
 */
abstract public class AbstractTest implements Test
{
    public boolean check(File file)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getLog()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getMessage()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
