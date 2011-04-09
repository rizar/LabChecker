package com.github.rizar.labchecker.lablib;

/**
 *
 * @author Rizar
 */
public class MacroProcessor
{
    public void registerMacro(String macro, String definition)
    {
        if (definition == null)
            throw new NullPointerException("MacroProcessor: definition must be not null.");
    }
}
