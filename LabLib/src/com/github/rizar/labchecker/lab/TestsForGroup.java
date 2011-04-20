package com.github.rizar.labchecker.lab;

import com.github.rizar.labchecker.exceptions.TestException;
import com.github.rizar.labchecker.test.ImageLibrary;
import com.github.rizar.labchecker.test.Log;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.github.rizar.labchecker.loadimage.LoadImgException;
import org.xml.sax.Attributes;
import static com.github.rizar.labchecker.lab.Tags.*;

/**
 *
 * @author Rizar
 */
public class TestsForGroup extends TestFor
{
    private List<TestFor> testsFor = new ArrayList<TestFor>();
    private String anyMacro;

    public void addTestFor(TestFor testFor)
    {
        testsFor.add(testFor);
    }

    TestFor[] getGroupTestsFor()
    {
        return testsFor.toArray(new TestFor[0]);
    }

    TestFor getTestFor(int index)
    {
        return testsFor.get(index);
    }

    @Override
    public boolean check(MacroProcessor macroProcessor, ImageLibrary library, File file) throws
            LoadImgException, TestException
    {
        clearMessageAndLog();
        if (!isFor(macroProcessor))
            return true;

        boolean any = anyMacro != null ? macroProcessor.process(anyMacro).equals("true") : false;

        boolean allSucceded = true;
        boolean someSucceded = false;
        for (TestFor testFor : testsFor)
        {
            testFor.setMessagePrefix(
                    ONE_LEVEL_MESSAGE_PREFIX + getMessagePrefix());
            boolean returnedResult = testFor.check(macroProcessor, library, file);
            allSucceded &= returnedResult;
            someSucceded |= returnedResult;
            messageLog.addAllMessages(testFor.getMessage());
            log.addAllMessages(testFor.getLog());
        }
        boolean result = allSucceded || (any && someSucceded);

        String toAppend = result ? getOkMessage() : getFailMessage();
        if (toAppend != null)
        {
            messageLog.startMessage(result ? Log.MessageType.OK : Log.MessageType.ERROR);
            messageLog.appendToMessage(getMessagePrefix());
            messageLog.appendToMessage(toAppend);
            messageLog.finishMessage();
        }

        return result;
    }

    public TestsForGroup(Attributes attributes)
    {
        super(attributes);
        anyMacro = attributes.getValue(ANY_ATTRIBUTE);
    }
}
