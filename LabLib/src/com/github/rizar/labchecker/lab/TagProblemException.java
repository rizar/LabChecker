package com.github.rizar.labchecker.lab;

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
     * Constructs TagProblemException with given line number, column number, tag name and message.
     * @param lineNumber
     * @param columnNumber
     * @param message
     */
    public TagProblemException(int lineNumber, int columnNumber, String tag, String message)
    {
        super(lineNumber, columnNumber, message);
        this.tag = tag;
    }
}
