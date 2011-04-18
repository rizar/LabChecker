package com.github.rizar.labchecker.lab;

import com.github.rizar.labchecker.test.Log;
import com.github.rizar.labchecker.parser.RemainderParser;
import com.github.rizar.labchecker.exceptions.InfiniteCycleException;
import com.github.rizar.labchecker.exceptions.TestException;
import com.github.rizar.labchecker.exceptions.TestParseException;
import com.github.rizar.labchecker.exceptions.UndefinedMacroException;
import com.github.rizar.labchecker.parser.GroupParser;
import com.github.rizar.labchecker.test.ImageLibrary;
import java.io.File;
import org.xml.sax.Attributes;
import com.github.rizar.labchecker.test.Test;
import loadimg.LoadImgException;
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

    protected Log messageLog = new Log(), log = new Log();

    protected void clearMessageAndLog()
    {
        messageLog.clear();
        log.clear();
    }

    protected boolean isFor(MacroProcessor macroProcessor)
    {
        if (groupMacro != null)
        {
            if (!new GroupParser(macroProcessor.process(groupMacro)).isFor(macroProcessor.
                    getGroup()))
                return false;
        }

        if (remainderMacro != null)
        {
            if (!new RemainderParser(macroProcessor.process(remainderMacro)).
                    isFor(Lab.codeInteger(
                    macroProcessor.getCode())))
                return false;
            /*Matcher matcher = REMAINDER_PATTERN.matcher(macroProcessor.process(
            remainderMacro));
            matcher.matches();
            int dividend = Lab.codeInteger(macroProcessor.process(
            "%" + CODE_MACRO + "%"));
            int remainder = Integer.parseInt(matcher.group(1));
            int divisor = Integer.parseInt(matcher.group(2));
            if (dividend % divisor != remainder)
            return false;*/
        }

        return true;
    }

    public boolean check(MacroProcessor macroProcessor, ImageLibrary library,
                         File file) throws
            LoadImgException, TestException
    {
        clearMessageAndLog();
        if (!isFor(macroProcessor))
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

        String toAppend = result ? getOkMessage() : getFailMessage();
        if (toAppend != null)
        {
            messageLog.startMessage(result ? Log.MessageType.OK : Log.MessageType.ERROR);
            messageLog.appendToMessage(getMessagePrefix());
            messageLog.appendToMessage(toAppend);
            messageLog.finishMessage();
        }

        log = getTest().getLog();
        return result;
    }

    public Log getMessage()
    {
        return messageLog;
    }

    public Log getLog()
    {
        return log;
    }
};
