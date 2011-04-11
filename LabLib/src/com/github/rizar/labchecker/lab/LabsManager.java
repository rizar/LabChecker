package com.github.rizar.labchecker.lab;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LabManager handles list of available labs.
 * @author Rizar
 */
public class LabsManager
{
    private File directory;

    /**
     * Get root directory of this LabsManager
     * @return root directory.
     */
    public File getDirectory()
    {
        return directory;
    }
    
    /**
     * Constructs LabsManagers for a given directory.
     * @param directory
     */
    public LabsManager(File directory)
    {
        this.directory = directory;
    }

    /**
     * Get all available labs.
     * @return array containing all availabe labs.
     */
    public Lab[] getAvailableLabs()
    {
        List<Lab> labsList = new ArrayList<Lab> ();
        File [] files = directory.listFiles();
        for (File file : files)
        {
            Lab lab = Lab.getInstance(file);
            if (lab != null)
                labsList.add(lab);
        }
        return labsList.toArray(new Lab [0]);
    }

    /**
     * Get lab by it's name.
     * @param name - lab name.
     * @return Lab object.
     */
    public Lab getLabByName(String name)
    {
        Lab [] labs = getAvailableLabs();
        for (Lab lab : labs)
            if (lab.getLabName().equals(name))
                return lab;
        return null;
    }
}
