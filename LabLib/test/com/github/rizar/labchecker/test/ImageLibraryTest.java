/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.rizar.labchecker.test;

import java.awt.image.BufferedImage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.github.rizar.labchecker.lab.Constraints.*;

/**
 *
 * @author Rizar
 */
public class ImageLibraryTest
{
    ImageLibrary library;

    public ImageLibraryTest()
    {
        library = new ImageLibrary(MMPE);
    }

    @Test
    public void testGetImage() throws Exception
    {
        BufferedImage image1 = library.getLibraryImage("lab2a4-304-v1.png");
        assertEquals(image1.getWidth(), 940);
        assertEquals(image1.getHeight(), 590);
        BufferedImage image2 = library.getLibraryImage("lab2a4-304-v1.png");
        assertTrue(image1 == image2);
    }
}
