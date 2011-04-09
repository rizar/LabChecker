package com.github.rizar.labchecker.lab;

/**
 *
 * @author Rizar
 */
class MissedTagException extends TagProblemException
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
    public MissedTagException(int lineNumber, int columnNumber, String tag)
    {
        super(lineNumber, columnNumber, tag, "missed tag \"" + tag + "\"");
    }
}
