package com.github.rizar.labchecker.exceptions;

import java.io.File;

/**
 *
 * @author Rizar
 */
public class WrongTagDataException extends TagProblemException
{
    /**
     * Constructs WrongTagDataException
     */
    public WrongTagDataException()
    {
    }

    /**
     * Constructs WrongTagDataException with given message
     * @param message
     */
    public WrongTagDataException(String message)
    {
        super(message);
    }

    /**
     * Constructs WrongTagDataException with given line number, column number, tag name.
     * @param file
     * @param lineNumber
     * @param columnNumber
     * @param tag
     */
    public WrongTagDataException(File file, int lineNumber, int columnNumber, String tag)
    {
        super(file, lineNumber, columnNumber, tag, "wrong data for tag \"" + tag + "\"");
    }

    /**
     * Constructs WrongTagDataException with given line number, column number, tag name and error reason.
     * @param lineNumber
     * @param columnNumber
     * @param tag
     * @param reason
     */
    public WrongTagDataException(File file, int lineNumber, int columnNumber, String tag, String reason)
    {
        super(file, lineNumber, columnNumber, tag, "wrong data for tag \"" + tag + "\" : " + reason);
    }
}
