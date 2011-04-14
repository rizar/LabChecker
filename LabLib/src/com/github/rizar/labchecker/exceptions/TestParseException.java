package com.github.rizar.labchecker.exceptions;

import java.util.regex.Pattern;

/**
 *
 * @author Rizar
 */
public class TestParseException extends RuntimeException
{
    private Pattern pattern;
    private  String string;

    public Pattern getPattern()
    {
        return pattern;
    }

    public String getString()
    {
        return string;
    }

    public TestParseException()
    {
    }

    public TestParseException(String message)
    {
        super(message);
    }

    public TestParseException(Pattern pattern, String string)
    {
        super("String " + string + " doesn't match pattern " + pattern);
        this.pattern = pattern;
        this.string = string;
    }

    public TestParseException(Pattern [] patterns, String string)
    {
        super("String " + string + " doesn't match patterns " + String.format(
                "%s, %s", patterns[0], patterns[1]));
    }
}
