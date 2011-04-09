/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.rizar.labchecker.lab;

import java.util.Formatter;

/**
 *
 * @author Rizar
 */
public class WrongAttributeValueException extends AttributeProblemException
{

    public WrongAttributeValueException(int lineNumber, int columnNumber,
                                        String tag,
                                        String attribute)
    {
        super(lineNumber, columnNumber, tag, attribute, buildMessage(tag, attribute));
    }

    private static String buildMessage(String tag, String attribute)
    {
        StringBuilder sb = new StringBuilder ();
        Formatter fmt = new Formatter(sb);
        fmt.format("wrong value for attribute %s of tag %s", attribute, tag);
        return sb.toString();
    }
}
