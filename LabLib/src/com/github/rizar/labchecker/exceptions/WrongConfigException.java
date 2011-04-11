package com.github.rizar.labchecker.exceptions;

import java.io.File;

/**
 * An exception to signal, that config is wrong.
 * @author Rizar
 */
public class WrongConfigException extends Exception
{
    /**
     * Consructs a WrongLabConfigException with specified message.
     * @param message
     */
    public WrongConfigException(String message)
    {
        super(message);
    }

    /**
     * Constructs WrongLabConfigException
     */
    public WrongConfigException()
    {
    }

    /**
     * Constructs WrongLabConfigException with given cause.
     * @param cause
     */
    public WrongConfigException(Throwable cause)
    {
    }

    /**
     * Constructs WrongLabConfigException with given cause and message.
     * @param message
     * @param cause
     */
    public WrongConfigException(String message, Throwable cause)
    {
    }

    private int lineNumber = -1;
    private int columnNumber = -1;

    /**
     * Get line number where error occured.
     * @return line number if it was given at construction, -1 otherwise.
     */
    public int getLineNumber()
    {
        return lineNumber;
    }

    /**
     * Get column number where error occured.
     * @return column number if it was given at construction, -1 otherwise
     */
    public int getColumnNumber()
    {
        return columnNumber;
    }

    /**
     * Constructs WrongConfigException with given file, line number, column number and message.
     * @param file
     * @param lineNumber
     * @param columnNumber
     * @param message
     */
    public WrongConfigException(File file, int lineNumber, int columnNumber, String message)
    {
        this("In file " + file.getName() + " at line " + lineNumber + " at column " + columnNumber + " : " + message);
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }
}
