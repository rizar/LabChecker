package com.github.rizar.labchecker.exceptions;

import java.util.Formatter;

/**
 *
 * @author Rizar
 */
public class MissedAttributeException extends AttributeProblemException
{
    /**
     * Constructs MissedAttributeException.
     */
    public MissedAttributeException()
    {
    }

    /**
     * Constructs MissedAttributeException with given message.
     * @param message
     */
    public MissedAttributeException(String message)
    {
    }

    /**
     * Constructs MissedAttributeException with given line number, column number, tag name, attribute name and message.
     * @param lineNumber
     * @param columnNumber
     * @param tag
     * @param attribute
     * @param message
     */
    public MissedAttributeException(int lineNumber, int columnNumber, String tag,
                                    String attribute)
    {
        super(lineNumber, columnNumber, tag, attribute, buildMessage(tag, attribute));
    }

    private static String buildMessage(String tag, String attribute)
    {
        StringBuilder sb = new StringBuilder();
        Formatter fmt = new Formatter(sb);
        fmt.format("missed attribute \"%s\" for tag \"%s\"", attribute, tag);
        return sb.toString();
    }
}
