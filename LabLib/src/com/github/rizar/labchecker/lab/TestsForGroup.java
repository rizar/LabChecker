package com.github.rizar.labchecker.lab;

import com.github.rizar.labchecker.exceptions.TestException;
import com.github.rizar.labchecker.test.ImageLibrary;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import loadimg.LoadImgException;
import org.xml.sax.Attributes;

/**
 *
 * @author Rizar
 */
public class TestsForGroup extends TestFor
{
    private List<TestFor> testsFor = new ArrayList<TestFor>();

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

        boolean result = true;
        for (TestFor testFor : testsFor)
        {
            testFor.setMessagePrefix(
                    ONE_LEVEL_MESSAGE_PREFIX + getMessagePrefix());
            result &= testFor.check(macroProcessor, library, file);
            messageBuilder.append(testFor.getMessage());
            logBuilder.append(testFor.getLog());
        }
        String message = result ? getOkMessage() : getFailMessage();
        if (message != null)
        {
            messageBuilder.append(getMessagePrefix());
            messageBuilder.append(message);
            messageBuilder.append("\n");
        }
        return result;
    }

    public TestsForGroup(Attributes attributes)
    {
        super(attributes);
    }
}
