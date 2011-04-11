package com.github.rizar.labchecker.lab;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;

/**
 *
 * @author Rizar
 */
public class TestsForGroup extends TestFor
{
    private List<TestFor> testsFor = new ArrayList<TestFor> ();

    public void addTestFor(TestFor testFor)
    {
        testsFor.add(testFor);
    }

    TestFor [] getGroupTestsFor()
    {
        return testsFor.toArray(new TestFor [0]);
    }

    public TestsForGroup(MacroProcessor macroProcessor, Attributes attributes)
    {
        super(macroProcessor, attributes);
    }
}
