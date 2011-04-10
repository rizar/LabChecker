package com.github.rizar.labchecker.exceptions;

/**
 *
 * @author Rizar
 */
public class UnknownTagException extends TagProblemException
{
    /**
     * Constructs UnknownTagException.
     */
    public UnknownTagException()
    {
    }

    /**
     * Constructs UnknownTagException with given message.
     * @param message
     */
    public UnknownTagException(String message)
    {
        super(message);
    }

     /**
     * Constructs UnknownTagException with given line number, column number and tag name.
     * @param lineNumber
     * @param columnNumber
     * @param qName
     */
    public UnknownTagException(int lineNumber, int columnNumber, String tag)
    {
        super(lineNumber, columnNumber, tag, "unknown tag \"" + tag + "\"");
    }
}
