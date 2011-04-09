package com.github.rizar.labchecker.lab;

/**
 *
 * @author Rizar
 */
class DuplicateTagException  extends TagProblemException
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

    public DuplicateTagException(int lineNumber, int columnNumber, String tag)
    {
        super(lineNumber, columnNumber, tag, "duplicate tag \"" + tag + "\"");
    }
}
