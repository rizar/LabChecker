package com.github.rizar.labchecker.lab;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.rizar.labchecker.lab.Constraints.*;

/**
 *
 * @author Rizar
 */
public class ColorSet
{
    private int numberOfColors;

    private Map<Integer, Color> colorMap = new HashMap<Integer, Color>();

    public ColorSet(int numberOfColors)
    {
        this.numberOfColors = numberOfColors;
    }

    public void parse(String colorsString)
    {
        String[] strings = colorsString.split(COLOR_SEPARATOR);
        if (strings.length < numberOfColors)
            throw new IllegalArgumentException("wrong number of colors in set");
        for (int i = 0; i < numberOfColors; i++)
        {
            String string = strings[i];
            Matcher matcher = NUMBERED_COLOR_PATTERN.matcher(string);
            if (!matcher.matches())
                throw new IllegalArgumentException(
                        string + " doesn't match color pattern");
            int num = Integer.parseInt(matcher.group(1));
            int red = Integer.parseInt(matcher.group(2));
            int green = Integer.parseInt(matcher.group(3));
            int blue = Integer.parseInt(matcher.group(4));
            if (num < 1 || num > numberOfColors)
                throw new IllegalArgumentException("wrong color number " + num);
            try
            {
                colorMap.put(num, new Color(red, green, blue));
            }
            catch (IllegalArgumentException e)
            {
                throw new IllegalArgumentException(
                        "wrong color components " + red + " " + green + " " + blue);
            }
        }
    }

    public Color getColor(int num)
    {
        return colorMap.get(num);
    }
}
