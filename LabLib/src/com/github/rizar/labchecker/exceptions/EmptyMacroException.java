package com.github.rizar.labchecker.exceptions;

/**
 *
 * @author Rizar
 */
public class EmptyMacroException extends RuntimeException
{
    public EmptyMacroException()
    {
    }

    public EmptyMacroException(String message)
    {
        super(message);
    }
}
