package com.github.rizar.labchecker.parser;

import com.github.rizar.labchecker.exceptions.TestParseException;
import static com.github.rizar.labchecker.lab.Constraints.*;

/**
 *
 * @author Rizar
 */
public class RemainderParser
{
    private String remainderString;

    public RemainderParser(String remainderString)
    {
        this.remainderString = remainderString;
    }

    public boolean isFor(int dividend)
    {
        String[] remaindersAndDivisor = remainderString.split(
                MOD_PATTERN_STRING);
        if (remaindersAndDivisor.length != 2)
            throw new TestParseException(
                    "Can't parse remainders in \"" + remainderString + "\"");
        try
        {
            String[] remainders = remaindersAndDivisor[0].split(
                    REMAINDERS_SEPARATOR);
            int divisor = Integer.parseInt(remaindersAndDivisor[1].trim());
            for (String rem : remainders)
            {
                int remainder = Integer.parseInt(rem.trim());
                if (dividend % divisor == remainder)
                    return true;
            }
            return false;
        }
        catch (NumberFormatException e)
        {
            throw new TestParseException(
                    "Can't parse remainders in \"" + remainderString + "\"");
        }
    }
}
