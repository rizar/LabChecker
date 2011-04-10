package com.github.rizar.labchecker.exceptions;

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
     * @param lineNumber
     * @param columnNumber
     * @param tag
     */
    public WrongTagDataException(int lineNumber, int columnNumber, String tag)
    {
        super(lineNumber, columnNumber, tag, "wrong data for tag \"" + tag + "\"");
    }

    /**
     * Constructs WrongTagDataException with given line number, column number, tag name and error reason.
     * @param lineNumber
     * @param columnNumber
     * @param tag
     * @param reason
     */
    public WrongTagDataException(int lineNumber, int columnNumber, String tag, String reason)
    {
        super(lineNumber, columnNumber, tag, "wrong data for tag \"" + tag + "\" : " + reason);
    }
}
