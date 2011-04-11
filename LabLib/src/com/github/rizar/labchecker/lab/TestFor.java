package com.github.rizar.labchecker.lab;

import org.xml.sax.Attributes;
import com.github.rizar.labchecker.test.Test;
import static com.github.rizar.labchecker.lab.Tags.*;

/**
 * 
 * @author Rizar
 */
public class TestFor
{
    private Test test;

    private MacroProcessor macroProcessor;

    private String groupMacro;

    private String remainderMacro;

    private String okMessage, failMessage;

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

    public TestFor(MacroProcessor macroProcessor, Attributes attributes)
    {
        this.macroProcessor = macroProcessor;
        this.groupMacro = attributes.getValue(GROUP_ATTRIBUTE);
        this.remainderMacro = attributes.getValue(REMAINDER_ATTRIBUTE);
        this.okMessage = attributes.getValue(OK_MESSAGE_ATTRIBTE);
        this.failMessage = attributes.getValue(FAIL_MESSAGE_ATTRIBUTE);
    }
};
