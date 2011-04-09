/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.rizar.labchecker.lablib;

/**
 *
 * @author Rizar
 */
public class WrongNestedTagException extends TagProblemException
{
    /**
     * Constructs WrongNestedTagException.
     */
    public WrongNestedTagException()
    {
    }

    /**
     * Constructs WrongNestedTagException with given message.
     * @param message
     */
    public WrongNestedTagException(String message)
    {
        super(message);
    }

    private String currentTag;

    /**
     * Get current tag for moment when exception was thrown.
     * @return current tag.
     */
    public String getCurrentTag()
    {
        return currentTag;
    }

    /**
     * Constructs WrongNestedTagException with given line number, column number, tag name, with message depending whether is should be nested tag or not.
     * @param lineNumber
     * @param columnNumber
     * @param tag
     * @param mustBeNested
     */
    public WrongNestedTagException(int lineNumber, int columnNumber, String tag,
                                   boolean mustBeNested, String currentTag)
    {
        super(lineNumber, columnNumber, tag, "tag \"" + tag
                + "\" should " + (mustBeNested ? "" : "not")
                + " be nested in tag \"" + currentTag + "\"");
        this.currentTag = currentTag;
    }
}
