package com.github.rizar.labchecker.exceptions;

/**
 *
 * @author Rizar
 */
public class InfiniteCycleException extends RuntimeException
{
    public InfiniteCycleException(String message)
    {
    }

    public InfiniteCycleException()
    {
        super("infinite macro cycle");
    }
}
