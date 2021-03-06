package com.github.rizar.labchecker.lab;

import com.github.rizar.labchecker.exceptions.EmptyMacroException;
import com.github.rizar.labchecker.exceptions.InfiniteCycleException;
import com.github.rizar.labchecker.exceptions.UndefinedMacroException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Rizar
 */
public class MacroProcessorTest
{
    private MacroProcessor mc;

    public MacroProcessorTest()
    {
    }

    @Before
    public void setUp()
    {
        mc = new MacroProcessor();
        mc.setModule(3);
        mc.registerMacro("ORIG", "orig/lab2a0var2(%MOD%k%REM_SUFF%)%GROUP00%.BDV");
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void testProcess0()
    {
        mc.setCode("603");
        mc.setGroup(6);
        assertEquals(mc.process("%ORIG%"), "orig/lab2a0var2(3k)06.BDV");
    }

    @Test
    public void testProcess1()
    {
        mc.setCode("304");
        mc.setGroup(3);
        assertEquals(mc.process("%ORIG%"), "orig/lab2a0var2(3k+1)03.BDV");
    }

    @Test
    public void testProcess2()
    {
        mc.setCode("a07");
        mc.setGroup(10);
        assertEquals(mc.process("%ORIG%"), "orig/lab2a0var2(3k+2)10.BDV");
    }

    @Test (expected = UndefinedMacroException.class)
    public void testUndefinedMacro()
    {
        mc.process("%UNDEFINED%");
    }

    @Test (expected = UndefinedMacroException.class)
    public void testUndefinedMacro2()
    {
        mc.process("%REM%");
    }

    @Test (expected = InfiniteCycleException.class)
    public void testInfiniteCycle()
    {
        mc.registerMacro("A", "%A%");
        mc.process("%A%");
    }

    @Test (expected = EmptyMacroException.class)
    public void testConditionalMacros()
    {
        mc.registerMacro("M");

        String code1 = "502";
        int group1 = 1;
        String def1 = "Group 1, code = 2 (mod 5)";
        mc.addDefinition("M", "1", "2 mod 5", def1);

        String code2 = "704";
        int group2 = 8;
        String def2 = "Group 8, code = 4 (mod 7)";
        mc.addDefinition("M", "8", "4 mod 7", def2);

        mc.setCode(code1);
        mc.setGroup(group1);
        assertEquals(mc.process("%M%"), def1);

        mc.setCode(code2);
        mc.setGroup(group2);
        assertEquals(mc.process("%M%"), def2);

        mc.setGroup(10);
        mc.process("%M%");
    }
}
