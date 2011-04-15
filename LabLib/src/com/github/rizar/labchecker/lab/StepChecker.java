package com.github.rizar.labchecker.lab;

import com.github.rizar.labchecker.exceptions.TestException;
import java.io.File;
import java.io.IOException;
import loadimg.LoadImgException;

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
    boolean check() throws LoadImgException,
                           TestException;

    /**
     * Returns true, if step was checked, false otherwise.
     * @return true, if step was checked, false otherwise.
     */
    boolean isChecked();

    /**
     * Get result of last check.
     * @return true, if last check was successfull, false if it failed.
     */
    boolean getCheckResult();

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
