package com.github.rizar.labchecker.lab;

import java.util.regex.Pattern;

/**
 *
 * @author Rizar
 */
public interface Constraints
{
    int COLOR_SETS_NUMBER = 10;

    int MIN_MODULE = 3;

    int MAX_MODULE = 20;

    int MAX_NUMBER_OF_GROUPS = 26;

    String GROUP_SEPARATOR = " ";

    String RGB_SEPARATOR = ",";

    String COLOR_SEPARATOR = "[;\\.]";

    String NUMBER_PATTERN_STRING = "\\s*(\\d+)\\s*";

    String HYPHEN_PATTERN_STRING = "\\s*[-,–]\\s*";

    String COLOR_PATTERN_STRING = NUMBER_PATTERN_STRING + RGB_SEPARATOR
                                + NUMBER_PATTERN_STRING + RGB_SEPARATOR
                                + NUMBER_PATTERN_STRING;

    String NUMBERED_COLOR_PATTERN_STRING = "\\A" + NUMBER_PATTERN_STRING +
                                        HYPHEN_PATTERN_STRING +
                                        COLOR_PATTERN_STRING + "\\s*\\z";

    Pattern NUMBERED_COLOR_PATTERN = Pattern.compile(NUMBERED_COLOR_PATTERN_STRING);

    String MOD_PATTERN_STRING = "mod";

    String REMAINDER_PATTERN_STRING = NUMBER_PATTERN_STRING + MOD_PATTERN_STRING + NUMBER_PATTERN_STRING;

    Pattern REMAINDER_PATTERN = Pattern.compile(REMAINDER_PATTERN_STRING);
}
