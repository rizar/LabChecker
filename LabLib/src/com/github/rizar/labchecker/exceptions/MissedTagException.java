package com.github.rizar.labchecker.exceptions;

import java.io.File;

/**
 *
 * @author Rizar
 */
public class MissedTagException extends TagProblemException
{
    /**
     * Constructs MissedTagException.
     */
    public MissedTagException()
    {

    }

    /**
     * Constructs MissedTagException with given message.
     * @param message
     */
    public MissedTagException(String message)
    {
        super(message);
    }

    /**
     * Constructs MissedTagException with given line number, column number, tag name.
     * @param lineNumber
     * @param columnNumber
     * @param tag
     */
    public MissedTagException(File file, int lineNumber, int columnNumber, String tag)
    {
        super(file, lineNumber, columnNumber, tag, "missed tag \"" + tag + "\"");
    }
}
