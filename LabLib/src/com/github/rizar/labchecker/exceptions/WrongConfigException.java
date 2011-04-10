package com.github.rizar.labchecker.exceptions;

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
     * Construct a WrongLabConfigException
     */
    public WrongConfigException()
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
     * Constructs a WrongLabConfigException storing <code>lineNumber</code> where error occured.
     */
    public WrongConfigException(int lineNumber, int columnNumber, String message)
    {
        this("At line " + lineNumber + " at column " + columnNumber + " : " + message);
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }
}
