package com.github.rizar.labchecker.test;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Rizar
 */
public class ColorSet
{
    private class Location
    {
        private String fileName;
        private int x, y;

        public Location(String fileName, int x, int y)
        {
            this.fileName = fileName;
            this.x = x;
            this.y = y;
        }
    }

    private HashMap<Integer, Location> colorsMap = new HashMap<Integer, Location> ();

    public ColorSet()
    {
    }

    public ColorSet(ColorSet set)
    {
        colorsMap = new HashMap<Integer, Location> (set.colorsMap);
    }

    void addColor(Integer color)
    {
        colorsMap.put(color, null);
    }

    void addColorFromFile(Integer color, String fileName, int x, int y)
    {
        colorsMap.put(color, new Location(fileName, x, y));
    }

    void addColorSet(ColorSet set)
    {
        for (Integer c : set.colorsMap.keySet())
            if (!colorsMap.containsKey(c))
                colorsMap.put(c, set.colorsMap.get(c));
    }

    void removeColor(Integer color)
    {
        colorsMap.remove(color);
    }

    void removeColorSet(ColorSet set)
    {
        for (Integer c : set.colorsMap.keySet())
            colorsMap.remove(c);
    }

    Set<Integer> getColors()
    {
        return colorsMap.keySet();
    }

    String getFileName(int c)
    {
        Location location = colorsMap.get(c);
        return location != null ? location.fileName : null;
    }

    int getX(int c)
    {
        Location location = colorsMap.get(c);
        return location != null ? location.x : -1;
    }

    int getY(int c)
    {
        Location location = colorsMap.get(c);
        return location != null ? location.y : -1;
    }
}
