package com.github.rizar.labchecker.exceptions;

import java.io.File;

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
     * Constructs AttributeProblemException with given file, line number, column number, tag name, attribute name and message.
     * @param file
     * @param lineNumber - line number where error occured
     * @param tag - tag name
     * @param attribute - attrubute name
     */
    public AttributeProblemException(File file, int lineNumber, int columnNumber, String tag, String attribute, String message)
    {
        super(file, lineNumber, columnNumber, tag, message);
        this.attribute = attribute;
    }
}
