package com.github.rizar.labchecker.lab;

import com.github.rizar.labchecker.exceptions.InfiniteCycleException;
import com.github.rizar.labchecker.exceptions.TestException;
import com.github.rizar.labchecker.exceptions.TestParseException;
import com.github.rizar.labchecker.exceptions.UndefinedMacroException;
import com.github.rizar.labchecker.test.ImageLibrary;
import java.util.regex.Matcher;
import java.io.File;
import java.io.IOException;
import org.xml.sax.Attributes;
import com.github.rizar.labchecker.test.Test;
import static com.github.rizar.labchecker.lab.Tags.*;
import static com.github.rizar.labchecker.lab.Constraints.*;
import static com.github.rizar.labchecker.lab.Macros.*;

/**
 * 
 * @author Rizar
 */
public class TestFor implements Test
{
    public static final String ONE_LEVEL_MESSAGE_PREFIX = "  ";

    private Test test;

    private String groupMacro;

    private String remainderMacro;

    private String okMessage, failMessage, messagePrefix = "";

    public String getMessagePrefix()
    {
        return messagePrefix;
    }

    public void setMessagePrefix(String messagePrefix)
    {
        this.messagePrefix = messagePrefix;
    }

    public Test getTest()
    {
        return test;
    }

    public void setTest(Test test)
    {
        this.test = test;
    }

    String getGroupMacro()
    {
        return groupMacro;
    }

    String getRemainderMacro()
    {
        return remainderMacro;
    }

    String getFailMessage()
    {
        return failMessage;
    }

    String getOkMessage()
    {
        return okMessage;
    }

    public TestFor(Attributes attributes)
    {
        groupMacro = attributes.getValue(GROUP_ATTRIBUTE);
        remainderMacro = attributes.getValue(REMAINDER_ATTRIBUTE);
        okMessage = attributes.getValue(OK_MESSAGE_ATTRIBTE);
        failMessage = attributes.getValue(FAIL_MESSAGE_ATTRIBUTE);
    }

    protected StringBuilder messageBuilder, logBuilder;

    protected void clearMessageAndLog()
    {
        messageBuilder = new StringBuilder();
        logBuilder = new StringBuilder();
    }

    protected boolean forThis(MacroProcessor macroProcessor)
    {
        if (groupMacro != null)
        {
            String[] groups = macroProcessor.process(groupMacro).split(
                    GROUP_SEPARATOR);
            String realGroup = macroProcessor.process(
                    "%" + GROUP_ONE_CHARACTER_MACRO + "%");
            boolean found = false;
            for (String group : groups)
                if (group.equals(realGroup))
                    found = true;
            if (!found)
                return false;
        }

        if (remainderMacro != null)
        {
            /*Matcher matcher = REMAINDER_PATTERN.matcher(macroProcessor.process(
            remainderMacro));
            matcher.matches();
            int dividend = Lab.codeInteger(macroProcessor.process(
            "%" + CODE_MACRO + "%"));
            int remainder = Integer.parseInt(matcher.group(1));
            int divisor = Integer.parseInt(matcher.group(2));
            if (dividend % divisor != remainder)
            return false;*/
            String remainderString = macroProcessor.process(remainderMacro);
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
                int dividend = Lab.codeInteger(macroProcessor.process(
                        "%" + CODE_MACRO + "%"));
                for (String rem : remainders)
                {
                    int remainder = Integer.parseInt(rem.trim());
                    if (dividend % divisor == remainder)
                        return true;
                }
            }
            catch (NumberFormatException e)
            {
                throw new TestParseException(
                        "Can't parse remainders in \"" + remainderString + "\"");
            }

            return false;
        }

        return true;
    }

    public boolean check(MacroProcessor macroProcessor, ImageLibrary library,
                         File file) throws
            IOException, TestException
    {
        clearMessageAndLog();
        if (!forThis(macroProcessor))
            return true;

        boolean result;
        try
        {
            result = getTest().check(macroProcessor, library, file);
        }
        catch (UndefinedMacroException e)
        {
            throw new TestException(e.getMessage(), e);
        }
        catch (InfiniteCycleException e)
        {
            throw new TestException(e.getMessage(), e);
        }
        catch (TestParseException e)
        {
            throw new TestException(e.getMessage(), e);
        }

        String message = result ? getOkMessage() : getFailMessage();
        if (message != null)
        {
            messageBuilder.append(getMessagePrefix());
            messageBuilder.append(result ? getOkMessage() : getFailMessage());
            messageBuilder.append("\n");
        }
        logBuilder.append(getTest().getLog());
        return result;
    }

    public String getMessage()
    {
        return messageBuilder.toString();
    }

    public String getLog()
    {
        return logBuilder.toString();
    }
};
