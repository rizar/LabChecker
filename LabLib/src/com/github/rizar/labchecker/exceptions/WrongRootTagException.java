package com.github.rizar.labchecker.exceptions;

import java.io.File;

/**
 *
 * @author Rizar
 */
public class WrongRootTagException extends TagProblemException
{
    /**
     * Constructs WrongRootTagException
     */
    public WrongRootTagException()
    {
    }

    /**
     * Constructs WrongRootTagException with given message.
     * @param message
     */
    public WrongRootTagException(String message)
    {
        super(message);
    }

    /**
     * Constructs WrongRootTagException with given line number, column number, tag name.
     * @param lineNumber
     * @param columnNumber
     * @param tag
     */
    public WrongRootTagException(File file, int lineNumber, int columnNumber, String tag)
    {
        super(file, lineNumber, columnNumber, tag, "tag \"" + tag + "\" can't be root");
    }
}
