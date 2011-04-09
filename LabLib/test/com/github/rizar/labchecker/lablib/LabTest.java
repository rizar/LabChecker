package com.github.rizar.labchecker.lablib;

import java.awt.Color;
import static com.github.rizar.labchecker.lablib.LabTags.*;

import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;

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

    @Test
    public void testColorSetsTagWithoutNumberOfColorsInSetAttribute() throws Exception
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
            assertEquals(e.getAttribute(), COLOR_SETS_TAG_NUMBER_OF_COLORS_IN_SET_ATTRIBUTE);
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

    @Test
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
            assertEquals(e.getTag(), "a");
        }
        assertTrue(catched);
    }

    @Test(expected = WrongTagDataException.class)
    public void testWrongColorData() throws Exception
    {
        Lab lab = testManager.getLabByName("lab8");
        lab.load();
    }

    @Test
    public void testSuccessfulLabLoad() throws Exception
    {
        Lab lab = realManager.getLabByName("famcs_2011_course2");
        lab.load();
        assertEquals(lab.getModule(), 3);
        assertEquals(lab.getSolutionNameMacro(), "%LAB%-%CODE%-%VAR%");
        assertEquals(lab.getNumberOfColorsInSet(), 16);

        ColorSet [] colorSets = lab.getColorSets();
        assertEquals(colorSets[1].getColor(3), new Color(128, 0, 0));
        assertEquals(colorSets[5].getColor(7), new Color(0, 90, 90));
        assertEquals(colorSets[0].getColor(16), new Color(255, 128, 64));

        Step [] steps = lab.getSteps().toArray(new Step[0]);
        assertEquals(steps[0].getScript(), "%LAB%1.xml");
        assertEquals(steps[2].getScript(), "%LAB%3.xml");
        assertEquals(steps[1].getFileMacro(), "%LAB%2-%GET_CODE%-v%GET_VAR%.gif");
        assertEquals(steps[3].getFileMacro(), "%LAB%4-%GET_CODE%-v%GET_VAR%.png");
    }
}
