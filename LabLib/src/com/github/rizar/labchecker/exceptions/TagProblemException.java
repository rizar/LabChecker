package com.github.rizar.labchecker.exceptions;

import java.io.File;

/**
 *
 * @author Rizar
 */
public class TagProblemException extends WrongConfigException
{
    /**
     * Constructs TagProblemException
     */
    public TagProblemException()
    {
    }

    /**
     * Constructs TagProblemException with given message
     * @param message
     */
    public TagProblemException(String message)
    {

    }

    private String tag;

    /**
     * Get tag name.
     * @return tag name.
     */
    public String getTag()
    {
        return tag;
    }

    /**
     * Constructs TagProblemException with given file, line number, column number, tag name and message.
     * @param file
     * @param lineNumber
     * @param columnNumber
     * @param message
     */
    public TagProblemException(File file, int lineNumber, int columnNumber, String tag, String message)
    {
        super(file, lineNumber, columnNumber, message);
        this.tag = tag;
    }
}
