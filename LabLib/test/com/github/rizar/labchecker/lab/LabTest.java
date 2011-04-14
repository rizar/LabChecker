package com.github.rizar.labchecker.lab;

import com.github.rizar.labchecker.exceptions.WrongNestedTagException;
import com.github.rizar.labchecker.exceptions.MissedAttributeException;
import com.github.rizar.labchecker.exceptions.DuplicateTagException;
import com.github.rizar.labchecker.exceptions.WrongTagDataException;
import com.github.rizar.labchecker.exceptions.MissedTagException;
import com.github.rizar.labchecker.exceptions.WrongAttributeValueException;
import com.github.rizar.labchecker.exceptions.AttributeProblemException;
import com.github.rizar.labchecker.exceptions.WrongRootTagException;
import java.util.Arrays;
import java.awt.Color;
import static com.github.rizar.labchecker.lab.Tags.*;

import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.github.rizar.labchecker.lab.Constraints.*;

/**
 *
 * @author Rizar
 */
public class LabTest
{
    LabsManager testManager;

    LabsManager realManager;

    public LabTest()
    {
        testManager = new LabsManager(new File("testdata"));
        realManager = new LabsManager(new File(".."));
    }

    @Test(expected = AttributeProblemException.class)
    public void testLabTagWithoutNameThrowsException() throws Exception
    {
        Lab lab = testManager.getLabByName("lab1");
        lab.load();
    }

    @Test(expected = AttributeProblemException.class)
    public void testModuleTagWithoutModuleAttribute() throws Exception
    {
        Lab lab = testManager.getLabByName("lab3");
        lab.load();
    }

    @Test(expected = WrongAttributeValueException.class)
    public void testModuleTagWithWrongValue() throws Exception
    {
        Lab lab = testManager.getLabByName("lab4");
        lab.load();
    }

    @Test(expected = WrongNestedTagException.class)
    public void testWrongNestedTag2() throws Exception
    {
        Lab lab = testManager.getLabByName("lab11");
        lab.load();
    }

    @Test
    public void testColorSetsTagWithoutNumberOfColorsInSetAttribute() throws
            Exception
    {
        Lab lab = testManager.getLabByName("lab6");
        boolean catched = false;
        try
        {
            lab.load();
        }
        catch (MissedAttributeException e)
        {
            catched = true;
            assertEquals(e.getTag(), COLOR_SETS_TAG);
            assertEquals(e.getAttribute(),
                    COLOR_SETS_TAG_NUMBER_OF_COLORS_IN_SET_ATTRIBUTE);
        }
        assertTrue(catched);
    }

    @Test
    public void testDuplicateColorSetsTag() throws Exception
    {
        Lab lab = testManager.getLabByName("lab5");
        boolean catched = false;
        try
        {
            lab.load();
        }
        catch (DuplicateTagException e)
        {
            catched = true;
            assertEquals(e.getTag(), COLOR_SETS_TAG);
        }
        assertTrue(catched);
    }

    /*@Test
    public void testWrongCharacterData() throws Exception
    {
    Lab lab = testManager.getLabByName("lab7");
    boolean catched = false;
    try
    {
    lab.load();
    }
    catch (WrongTagDataException e)
    {
    catched = true;
    assertEquals(e.getTag(), "module");
    }
    assertTrue(catched);
    }*/
    @Test(expected = WrongTagDataException.class)
    public void testWrongColorData() throws Exception
    {
        Lab lab = testManager.getLabByName("lab8");
        lab.load();
    }

    @Test(expected = WrongRootTagException.class)
    public void testWrongRootTag() throws Exception
    {
        Lab lab = testManager.getLabByName("lab12");
        lab.load();
    }

    /*@Test
    public void testUnknownTag() throws Exception
    {
    Lab lab = testManager.getLabByName("lab9");
    boolean catched = false;
    try
    {
    lab.load();
    }
    catch (UnknownTagException e)
    {
    assertEquals(e.getTag(), "unknown");
    catched = true;
    }
    assertTrue(catched);
    }*/
    @Test(expected = MissedTagException.class)
    public void testTagMissed() throws Exception
    {
        Lab lab = testManager.getLabByName("lab10");
        lab.load();
    }

    @Test
    public void testSuccessfulLabLoad() throws Exception
    {
        Lab lab = realManager.getLabByName("famcs_2011_course2");
        lab.load();
        assertEquals(lab.getModule(), 3);
        //assertEquals(lab.getSolutionNameMacro(), "%LAB%-%CODE%-%VAR%");
        assertEquals(lab.getNumberOfColorsInSet(), 16);

        ColorSet[] colorSets = lab.getColorSets();
        assertEquals(colorSets[1].getColor(3), new Color(128, 0, 0));
        assertEquals(colorSets[5].getColor(7), new Color(0, 90, 90));
        assertEquals(colorSets[0].getColor(16), new Color(255, 128, 64));

        Step[] steps = lab.getSteps().toArray(new Step[0]);
        assertEquals(steps[0].getScriptFile().getName(), "lab2a1.xml");
        assertEquals(steps[2].getScriptFile().getName(), "lab2a3.xml");
        //assertTrue(steps[1].isStepFile(new File("lab2a2-304-v1.tif")));
        assertTrue(steps[3].isStepFile(new File("lab2a4-304-v1.png")));
        //assertEquals(steps[1].getFileMacro(), "%LAB%2-%GET_CODE%-v%GET_VAR%.gif");
        //assertEquals(steps[3].getFileMacro(), "%LAB%4-%GET_CODE%-v%GET_VAR%.png");

        assertEquals(lab.getSolutionName(new File("lab2a3-304-v3.gif")),
                "lab2a-304-v3");
        //assertEquals(lab.getSolutionName(new File("lab2a2-a05-v2.tif")), "lab2a-a05-v2");

        //System.err.println(lab.getMacroProcessor());
    }

    @Test
    public void testMyLab() throws Exception
    {
        Lab lab = realManager.getLabByName("famcs_2011_course2");
        lab.load();

        File mmpe = new File("D:\\bsu\\mmpe\\labs\\lab2a");

        /*File step1 = new File(mmpe, "lab2a1-304-v2.pcx");
        StepChecker checker1 = lab.getStepChecker(step1);
        assertTrue(checker1.check());
        System.err.println(checker1.getMessage());
        System.err.println(checker1.getLog());*/

        /*File step2 = new File(mmpe, "lab2a2-304-v2.tif");
        StepChecker checker2 = lab.getStepChecker(step2);
        assertTrue(checker2.check());
        System.err.println(checker2.getMessage());
        System.err.println(checker2.getLog());*/

        File step3 = new File(mmpe, "lab2a3-304-v2.gif");
        StepChecker checker3 = lab.getStepChecker(step3);
        assertTrue(checker3.check());
        System.out.println(checker3.getMessage());
        System.err.println(checker3.getLog());

        File step4 = new File(mmpe, "lab2a4-304-v2.png");
        StepChecker checker4 = lab.getStepChecker(step4);
        assertTrue(checker4.check());
        System.out.println(checker4.getMessage());
        System.err.println(checker4.getLog());
    }

    @Test
    public void testThreeLabs() throws Exception
    {
        doTest("606");
        assertTrue(doTest("a43"));
        doTest("205");
    }

    public boolean doTest(String code) throws Exception
    {
        Lab lab = realManager.getLabByName("famcs_2011_course2");
        lab.load();

        int rem = (100 * Character.digit(code.charAt(0), MAX_NUMBER_OF_GROUPS) + 10 * Character.
                digit(code.charAt(1), 10) + Character.digit(code.charAt(
                2), 10)) % 3;
        boolean ok = true;

        File s1 = new File(TESTS012,
                "\\" + rem + "\\" + "lab2a1-" + code + "-v1.png");
        StepChecker c1 = lab.getStepChecker(s1);
        ok &= c1.check();
        System.out.print(c1.getMessage());
        System.out.flush();
        System.err.println(c1.getLog());

        File s2 = new File(TESTS012,
                "\\" + rem + "\\" + "lab2a2-" + code + "-v1.png");
        StepChecker c2 = lab.getStepChecker(s2);
        ok &= c2.check();
        System.out.print(c2.getMessage());
        System.out.flush();
        System.err.println(c2.getLog());

        File s3 = new File(TESTS012,
                "\\" + rem + "\\" + "lab2a3-" + code + "-v1.gif");
        StepChecker c3 = lab.getStepChecker(s3);
        ok &= c3.check();
        System.out.print(c3.getMessage());
        System.out.flush();
        System.err.println(c3.getLog());

        File s4 = new File(TESTS012,
                "\\" + rem + "\\" + "lab2a4-" + code + "-v1.png");
        StepChecker c4 = lab.getStepChecker(s4);
        ok &= c4.check();
        System.out.print(c4.getMessage());
        System.out.flush();
        System.err.println(c4.getLog());

        return ok;
    }
}
