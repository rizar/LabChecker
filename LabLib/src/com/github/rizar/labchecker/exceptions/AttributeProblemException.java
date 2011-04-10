package com.github.rizar.labchecker.exceptions;

import java.util.Formatter;

/**
 *
 * @author Rizar
 */
public class AttributeProblemException extends TagProblemException
{
    /**
     * Constructs AttributeProblemException.
     */
    public AttributeProblemException()
    {
    }

    /**
     * Constructs AttributeProblemException with given message.
     * @param message
     */
    public AttributeProblemException(String message)
    {
        super(message);
    }

    private String tag;
    private String attribute;

    /**
     * Get attribute name.
     * @return attribute name.
     */
    public String getAttribute()
    {
        return attribute;
    }

    /**
     * Constructs AttributeProblemException with given line number, column number, tag name, attribute name and message.
     * @param lineNumber - line number where error occured
     * @param tag - tag name
     * @param attribute - attrubute name
     */
    public AttributeProblemException(int lineNumber, int columnNumber, String tag, String attribute, String message)
    {
        super(lineNumber, columnNumber, tag, message);
        this.attribute = attribute;
    }
}
