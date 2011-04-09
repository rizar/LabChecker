/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.rizar.labchecker.lablib;

import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Rizar
 */
public class LabsManagerTest
{
    LabsManager manager;

    public LabsManagerTest()
    {
        manager = new LabsManager(new File("testdata"));
    }

    @Test
    public void testGetAvailableLabs()
    {
        Lab [] labs = manager.getAvailableLabs();
        assertEquals(labs.length, 2);
        assertEquals(labs[0].getLabName(), "lab1");
        assertEquals(labs[1].getLabName(), "lab3");
    }

    @Test
    public void testGetLabByName()
    {
        Lab lab1 = manager.getLabByName("lab1");
        assertEquals(lab1.getLabName(), "lab1");
        Lab lab2 = manager.getLabByName("lab2");
        assertNull(lab2);
        Lab lab3 = manager.getLabByName("lab3");
        assertEquals(lab3.getLabName(), "lab3");
    }
}
