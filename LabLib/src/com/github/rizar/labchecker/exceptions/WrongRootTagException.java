package com.github.rizar.labchecker.exceptions;

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
    public WrongRootTagException(int lineNumber, int columnNumber, String tag)
    {
        super(lineNumber, columnNumber, tag, "tag \"" + tag + "\" can't be root");
    }
}
