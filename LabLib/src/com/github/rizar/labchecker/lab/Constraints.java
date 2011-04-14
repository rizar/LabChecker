package com.github.rizar.labchecker.lab;

import java.io.File;
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

    Pattern NUMBER_PATTERN = Pattern.compile(
            "\\A" + NUMBER_PATTERN_STRING + "\\z");

    String HYPHEN_PATTERN_STRING = "\\s*[-,–]\\s*";

    String COLOR_PATTERN_STRING = NUMBER_PATTERN_STRING + RGB_SEPARATOR
            + NUMBER_PATTERN_STRING + RGB_SEPARATOR
            + NUMBER_PATTERN_STRING;

    Pattern COLOR_PATTERN = Pattern.compile("\\A" + COLOR_PATTERN_STRING + "\\z");

    String NUMBERED_COLOR_PATTERN_STRING = NUMBER_PATTERN_STRING
            + HYPHEN_PATTERN_STRING
            + COLOR_PATTERN_STRING;

    Pattern NUMBERED_COLOR_PATTERN = Pattern.compile(
            "\\A" + NUMBERED_COLOR_PATTERN_STRING + "\\z");

    String MOD_PATTERN_STRING = "mod";

    String REMAINDERS_SEPARATOR = " ";

    /*String REMAINDER_PATTERN_STRING = NUMBER_PATTERN_STRING + MOD_PATTERN_STRING + NUMBER_PATTERN_STRING;

    Pattern REMAINDER_PATTERN = Pattern.compile(
            "\\A" + REMAINDER_PATTERN_STRING + "\\z");*/

    String KILOBYTES_PATTERN_STRING = "kb";

    String KILOBYTES_SIZE_PATTERN_STRING = NUMBER_PATTERN_STRING + KILOBYTES_PATTERN_STRING;

    Pattern KILOBYTES_SIZE_PATTERN = Pattern.compile(
            "\\A" + KILOBYTES_SIZE_PATTERN_STRING + "\\z");

    String PERCENT_PATTERN_STRING = "per";

    String PERCENT_DEVIATION_PATTERN_STRING = NUMBER_PATTERN_STRING + PERCENT_PATTERN_STRING;

    Pattern PERCENT_DEVIATION_PATTERN = Pattern.compile(
            "\\A" + PERCENT_DEVIATION_PATTERN_STRING + "\\z");

    String POSITION_IN_FILE_PATTERN_STRING = "\\(" + NUMBER_PATTERN_STRING + "," + NUMBER_PATTERN_STRING + "\\)";

    String COLOR_IN_FILE_PATTERN_STRING = "(.*)" + POSITION_IN_FILE_PATTERN_STRING;

    Pattern COLOR_IN_FILE_PATTERN = Pattern.compile(
            "\\A" + COLOR_PATTERN_STRING + "\\z");

    //----------------------
    int BYTES_IN_KILOBYTE = 1024;

    int MASK8 = (1 << 8) - 1;

    int MASK16 = (1 << 16) - 1;

    //----------------------
    File MMPE = new File("D:\\bsu\\mmpe\\labs\\lab2a");

    File TESTS012 = new File("D:\\LabChecker\\Labs");

    boolean PRINT_TAGS = false;
}
