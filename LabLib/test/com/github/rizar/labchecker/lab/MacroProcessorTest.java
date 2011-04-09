package com.github.rizar.labchecker.lab;

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
        mc.registerMacro("ORIG", "orig/lab2a0var2(%MOD%k%REM_SUFF%)%GROUP%.BDV");
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void testProcess0()
    {
        mc.setCode(603);
        mc.setGroup(6);
        assertEquals(mc.process("%ORIG%"), "orig/lab2a0var2(3k)06.BDV");
    }

    @Test
    public void testProcess1()
    {
        mc.setCode(304);
        mc.setGroup(3);
        assertEquals(mc.process("%ORIG%"), "orig/lab2a0var2(3k+1)03.BDV");
    }

    @Test
    public void testProcess2()
    {
        mc.setCode(1007);
        mc.setGroup(10);
        assertEquals(mc.process("%ORIG%"), "orig/lab2a0var2(3k+2)10.BDV");
    }
}
