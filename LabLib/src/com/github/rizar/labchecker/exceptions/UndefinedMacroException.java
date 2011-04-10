package com.github.rizar.labchecker.exceptions;

/**
 *
 * @author Rizar
 */
public class UndefinedMacroException extends IllegalArgumentException
{
    public UndefinedMacroException()
    {
    }

    public UndefinedMacroException(String message)
    {
        super(message);
    }

    private String macro;

    public String getMacro()
    {
        return macro;
    }

    public UndefinedMacroException(String message, String macro)
    {
        super(message + " undefined macro \"" + macro + "\"");
    }
}
