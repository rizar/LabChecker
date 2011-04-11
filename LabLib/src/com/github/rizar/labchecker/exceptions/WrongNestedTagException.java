/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.rizar.labchecker.exceptions;

import java.io.File;

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
    public WrongNestedTagException(File file, int lineNumber, int columnNumber, String tag,
                                   Boolean mustBeNested, String currentTag)
    {
        super(file, lineNumber, columnNumber, tag, "tag \"" + tag
                + "\" should " + (mustBeNested ? "" : "not")
                + " be nested in tag \"" + currentTag + "\"");
        this.currentTag = currentTag;
    }
}
