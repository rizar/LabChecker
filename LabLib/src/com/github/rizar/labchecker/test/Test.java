package com.github.rizar.labchecker.test;

import java.io.File;

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
    boolean check(File file);

    /**
     * Get check message.
     * @return check message.
     */
    String getMessage();

    /**
     * Get check verbose log.
     * @return check log.
     */
    String getLog();
}
