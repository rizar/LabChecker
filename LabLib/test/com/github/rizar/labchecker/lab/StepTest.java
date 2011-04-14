package com.github.rizar.labchecker.lab;

import com.github.rizar.labchecker.test.FileSizeTest;
import com.github.rizar.labchecker.exceptions.MissedAttributeException;
import com.github.rizar.labchecker.exceptions.TestException;
import com.github.rizar.labchecker.exceptions.WrongNestedTagException;
import com.github.rizar.labchecker.exceptions.WrongRootTagException;
import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import static com.github.rizar.labchecker.lab.Constraints.*;

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

    @Test
    public void testGetSolutionNameDoesntModifyMacroProcessor() throws Exception
    {
        Lab lab = realManager.getLabByName("famcs_2011_course2");
        lab.load();
        String before = lab.getMacroProcessor().toString();
        System.err.println(before);
        assertEquals(lab.getSolutionName(new File("lab2a1-304-v1.pcx")), "lab2a-304-v1");
        assertEquals(before, lab.getMacroProcessor().toString());
    }

    @Test
    public void testInnerStepCheckerMacroProcessor() throws Exception
    {
        Lab lab = realManager.getLabByName("famcs_2011_course2");
        lab.load();
        Step.InnerStepChecker checker = (Step.InnerStepChecker)lab.getStepChecker(new File("lab2a1-304-v1.pcx"));
        MacroProcessor mp = checker.getMacroProcessor();
        assertEquals(mp.process("%C1%"), "0,0,0");
        assertEquals(mp.process("%C4%"), "255,255,20");
        assertEquals(mp.process("%C16%"), "255,128,64");
        assertEquals(mp.process("%ORIG%"), "orig/lab2a0var2(3k+1)03BDV.tga");
        /*checker.check();
        System.err.println(checker.getMessage());
        System.err.println(checker.getLog());*/
    }

    /*@Test
    public void testGroupAndRemainderMacros() throws Exception
    {
        Lab lab = testManager.getLabByName("lab25");
        lab.load();

        StepChecker checker = lab.getStepChecker(new File("lab2a4-304-v2.tif"));
        checker.check();
        String [] messages = checker.getMessage().split("\n");
        assertEquals(messages[0].trim(), "file_size_test");
        assertEquals(checker.getMessage().indexOf("image_size_test"), -1);

        StepChecker checker2 = lab.getStepChecker(new File("lab2a2-a05-v3.tif"));
        checker2.check();
        String [] messages2 = checker2.getMessage().split("\n");
        assertEquals(messages2[0].trim(), "image_size_test");
        assertEquals(checker2.getMessage().indexOf("file_size_test"), -1);

        StepChecker checker3 = lab.getStepChecker(new File("lab2a2-a11-v1.tif"));
        checker3.check();
        String [] messages3 = checker3.getMessage().split("\n");
        assertEquals(messages3[0].trim(), "color_set_test");
    }*/

    @Test
    public void testFileSizeTestParsing() throws Exception
    {
        Lab lab = testManager.getLabByName("lab26");
        lab.load();

        StepChecker checker = lab.getStepChecker(new File(MMPE, "lab2a3-304-v2.gif"));
        assertFalse(checker.isChecked());
        checker.check();
        assertTrue(checker.isChecked());

        Step.InnerStepChecker ic = (Step.InnerStepChecker)checker;
        TestsForGroup root = ic.getRootTest();
        FileSizeTest fsTest = (FileSizeTest)root.getTestFor(0).getTest();
        assertEquals(fsTest.getMaxSize(), 150);
    }

    @Test (expected = TestException.class)
    public void testWrongDeviationMacroInFileSizeTest() throws Exception
    {
        Lab lab = testManager.getLabByName("lab27");
        lab.load();

        StepChecker checker = lab.getStepChecker(new File(MMPE, "lab2a1-304-v1.pcx"));
        checker.check();
    }
}
