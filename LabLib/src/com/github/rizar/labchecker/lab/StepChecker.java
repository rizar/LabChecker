package com.github.rizar.labchecker.lab;

import java.io.File;
import java.io.IOException;

/**
 * An interface to check step file and store check results.
 * @author Rizar
 */
public interface StepChecker
{
    /**
     * Check step file running all corresponding tests.
     * @return <code>true</code> if check succeeded, <code>false</code> otherwise.
     */
    boolean check() throws IOException;

    /**
     * Get step file to be checked.
     * @return step file reference.
     */
    File getStepFile();

    /**
     * Get check message.
     * @return check message, if <code>check</code> was run, <code>null</code> otherwise.
     */
    String getMessage();

    /**
     * Get check verbose log.
     * @return check log, if <code>check</code> was run <code>null</code> otherwise.
     */
    String getLog();
}
