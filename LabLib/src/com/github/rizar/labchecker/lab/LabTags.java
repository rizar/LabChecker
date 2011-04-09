package com.github.rizar.labchecker.lab;

/**
 * Contains tags for lab config file.
 * @author Rizar
 */
public interface LabTags
{
    String LAB_TAG = "lab";

    String LAB_TAG_LAB_ATTRIBUTE = "lab";

    String MODULE_TAG = "module";

    String MODULE_TAG_MODULE_ATTRIBUTE = "mod";

    String SOLUTION_NAME_TAG = "solution_name";

    String SOLUTION_NAME_TAG_NAME_ATTRIBUTE = "name";

    String COLOR_SETS_TAG = "color_sets";

    String COLOR_SETS_TAG_NUMBER_OF_COLORS_IN_SET_ATTRIBUTE = "num_colors_in_set";

    String COLOR_SET_TAG = "color_set";

    String COLOR_SET_TAG_NUMBER_ATTRIBUTE = "num";

    String STEP_TAG = "step";

    String STEP_TAG_SCRIPT_ATTRIBUTE = "script";

    String STEP_TAG_FILE_ATTRIBUTE = "file";

    String MACRO_TAG = "macro";

    String MACRO_TAG_NAME_ATTRIBUTE = "name";

    String MACRO_TAG_DEFINITION_ATTRIBUTE = "def";

    String[] OBLIGATORY_TAGS = new String[]
    {
        LAB_TAG, MODULE_TAG, SOLUTION_NAME_TAG, COLOR_SETS_TAG, SOLUTION_NAME_TAG
    };
}
