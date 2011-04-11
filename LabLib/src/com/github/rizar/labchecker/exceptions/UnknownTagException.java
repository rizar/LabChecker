package com.github.rizar.labchecker.exceptions;

import java.io.File;

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
     * Constructs UnknownTagException with given file, line number, column number and tag name.
     * @param file
     * @param lineNumber
     * @param columnNumber
     * @param qName
     */
    public UnknownTagException(File file, int lineNumber, int columnNumber, String tag)
    {
        super(file, lineNumber, columnNumber, tag, "unknown tag \"" + tag + "\"");
    }
}
