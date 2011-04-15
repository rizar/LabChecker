package com.github.rizar.labchecker.parser;

import com.github.rizar.labchecker.exceptions.TestParseException;
import static com.github.rizar.labchecker.lab.Constraints.GROUP_SEPARATOR;

/**
 *
 * @author Rizar
 */
public class GroupParser
{
    String groupString;

    public GroupParser(String groupString)
    {
        this.groupString = groupString;
    }

    public boolean isFor(int realGroup)
    {
        String lastGroup = null;
        try
        {
            String[] groups = groupString.split(GROUP_SEPARATOR);
            for (String group : groups)
            {
                lastGroup = group;
                int groupNumber = Integer.parseInt(group);
                if (groupNumber == realGroup)
                    return true;
            }
            return false;
        }
        catch (NumberFormatException e)
        {
            throw new TestParseException(lastGroup + " is not group number.");
        }
    }
}
