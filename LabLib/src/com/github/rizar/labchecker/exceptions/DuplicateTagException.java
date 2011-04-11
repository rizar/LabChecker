package com.github.rizar.labchecker.exceptions;

import java.io.File;

/**
 *
 * @author Rizar
 */
public class DuplicateTagException  extends TagProblemException
{
    /**
     * Constructs DuplicateTagException.
     */
    public DuplicateTagException()
    {
    }

    /**
     * Constructs DuplicateTagException with given message.
     * @param message
     */
    public DuplicateTagException(String message)
    {
        super(message);
    }

    public DuplicateTagException(File file, int lineNumber, int columnNumber, String tag)
    {
        super(file, lineNumber, columnNumber, tag, "duplicate tag \"" + tag + "\"");
    }
}
