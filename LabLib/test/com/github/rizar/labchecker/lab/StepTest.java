package com.github.rizar.labchecker.lab;

import com.github.rizar.labchecker.exceptions.MissedAttributeException;
import com.github.rizar.labchecker.exceptions.WrongNestedTagException;
import com.github.rizar.labchecker.exceptions.WrongRootTagException;
import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Rizar
 */
public class StepTest
{
    LabsManager testManager, realManager;

    public StepTest()
    {
        testManager = new LabsManager(new File("testdata"));
        realManager = new LabsManager(new File(".."));
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Test (expected = WrongRootTagException.class)
    public void testWrongRootTag() throws Exception
    {
        Lab lab = testManager.getLabByName("lab20");
        lab.load();
    }

    @Test (expected = MissedAttributeException.class)
    public void testMissedDeviationAttribute() throws Exception
    {
        Lab lab = testManager.getLabByName("lab21");
        lab.load();
    }

    @Test (expected = MissedAttributeException.class)
    public void testMissedWidthAttribute() throws Exception
    {
        Lab lab = testManager.getLabByName("lab22");
        lab.load();
    }

    @Test (expected = WrongNestedTagException.class)
    public void testWrongNestedAddColorTag() throws Exception
    {
        Lab lab = testManager.getLabByName("lab23");
        lab.load();
    }

    @Test
    public void testGroups() throws Exception
    {
        Lab lab = testManager.getLabByName("lab24");
        lab.load();

        TestsForGroup rootTest = lab.getSteps().get(0).getRootTest();
        assertEquals(rootTest.getOkMessage(), "script");

        TestFor [] tests = rootTest.getGroupTestsFor();
        assertEquals(tests.length, 2);
        assertEquals(tests[0].getOkMessage(), "group1");
        assertEquals(tests[1].getOkMessage(), "group2");
        assertTrue(tests[0] instanceof TestsForGroup);
        assertTrue(tests[1] instanceof TestsForGroup);

        TestFor [] tests0 = ((TestsForGroup)tests[0]).getGroupTestsFor();
        assertEquals(tests0.length, 2);
        assertEquals(tests0[0].getOkMessage(), "group1.1");
        assertEquals(tests0[1].getOkMessage(), "group1.2");
        assertTrue(tests0[0] instanceof TestsForGroup);
        assertTrue(tests0[1] instanceof TestsForGroup);

        TestFor [] tests01 = ((TestsForGroup)tests0[0]).getGroupTestsFor();
        assertEquals(tests01.length, 1);
        assertEquals(tests01[0].getOkMessage(), "test1");
        assertFalse(tests01[0] instanceof TestsForGroup);

        TestFor [] tests02 = ((TestsForGroup)tests0[1]).getGroupTestsFor();
        assertEquals(tests02.length, 1);
        assertEquals(tests02[0].getOkMessage(), "test2");
        assertFalse(tests02[0] instanceof TestsForGroup);

        TestFor [] tests20 = ((TestsForGroup)tests[1]).getGroupTestsFor();
        assertEquals(tests20.length, 1);
        assertEquals(tests20[0].getOkMessage(), "test3");
        assertEquals(tests20[0].getFailMessage(), "failtest3");
        assertEquals(tests20[0].getGroupMacro(), "3");
        assertEquals(tests20[0].getRemainderMacro(), "0 mod 2");
        assertFalse(tests20[0] instanceof TestsForGroup);
    }
}
