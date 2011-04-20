package com.github.rizar.labchecker.lab;

/**
 * Containts names for predefined macros.
 * @author Rizar
 */
public interface Macros
{
    String LAB_MACRO = "LAB";

    String CODE_MACRO = "CODE";

    String MODULE_MACRO = "MOD";

    String REMAINDER_MACRO = "REM";

    String REMAINDER_SUFFIX_MACRO = "REM_SUFF";

    String GROUP_TWO_CHARACTERS_MACRO = "GROUP00";

    String GROUP_ONE_CHARACTER_MACRO = "GROUPX";

    String GET_CODE_MACRO = "GET_CODE";

    String GET_VAR_MACRO = "GET_VAR";

    String VARIANT_MACRO = "VAR";

    String SOLUTION_MACRO = "SOL";

    String TEACHER_MACRO = "TEACH";

    String GET_CODE_MACRO_DEFINITION = "([\\d[a-z]]\\d\\d)";

    String GET_VAR_MACRO_DEFINITION = "(\\d)";

    String COLOR_MACRO_PREFIX = "C";

    String INVERTED_COLOR_MACRO_SUFFIX = "-";
}
