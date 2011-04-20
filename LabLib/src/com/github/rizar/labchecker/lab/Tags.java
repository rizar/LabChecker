package com.github.rizar.labchecker.lab;

/**
 * Contains tags for lab config file.
 * @author Rizar
 */
public interface Tags
{
    //lab config tags
    String LAB_TAG = "lab";

    String LAB_TAG_LAB_ATTRIBUTE = "lab";

    String MODULE_TAG = "module";

    String MODULE_TAG_MODULE_ATTRIBUTE = "mod";

    /*String SOLUTION_NAME_TAG = "solution_name";

    String SOLUTION_NAME_TAG_NAME_ATTRIBUTE = "name";*/
    String COLOR_SETS_TAG = "color_sets";

    String COLOR_SETS_TAG_NUMBER_OF_COLORS_IN_SET_ATTRIBUTE = "num_colors_in_set";

    String COLOR_SET_TAG = "color_set";

    String COLOR_SET_TAG_NUMBER_ATTRIBUTE = "num";

    String STEP_TAG = "step";

    String STEP_TAG_SCRIPT_ATTRIBUTE = "script";

    String STEP_TAG_FILE_ATTRIBUTE = "file";

    String MACRO_TAG = "macro";

    String DEFINITION_TAG = "def";

    String NAME_ATTRIBUTE = "name";

    String DEFINITION_ATTRIBUTE = "def";

    String TEACHERS_TAG = "teachers";

    String TEACHER_TAG = "teacher";

    String[] OBLIGATORY_TAGS = new String[]
    {
        LAB_TAG, MODULE_TAG, COLOR_SETS_TAG, TEACHERS_TAG
    };

    //script tags
    String GROUP_ATTRIBUTE = "group";

    String REMAINDER_ATTRIBUTE = "rem";

    String OK_MESSAGE_ATTRIBTE = "ok_mess";

    String FAIL_MESSAGE_ATTRIBUTE = "fail_mess";

    String SCRIPT_TAG = "script";

    String FILE_SIZE_TEST_TAG = "file_size_test";

    String FILE_SIZE_TEST_SIZE_ATTRIBUTE = "size";

    String FILE_SIZE_TEST_DEVIATION_ATTRIBUTE = "dev";

    String IMAGE_SIZE_TEST_TAG = "image_size_test";

    String IMAGE_SIZE_TEST_HEIGHT_TAG = "height";

    String IMAGE_SIZE_TEST_WIDTH_TAG = "width";

    String PATTERN_TEST_TAG = "pat_test";

    String PATTERN_TEST_COLOR_TAG = "color";

    String IGNORE_TAG = "ignore";

    String PATTERN_TEST_PATTERN_RECTANGLE_TAG = "pat_rect";

    String PATTERN_TEST_TEST_RECTANGLE_TAG = "test_rect";

    String PATTERN_TEST_SEEK_ATTRIBUTE = "seek";

    String COLOR_SET_TEST_TAG = "color_set_test";

    String ADD_COLOR_TAG = "add_color";

    String REMOVE_COLOR_TAG = "rem_color";

    String COLOR_ATTRIBUTE = "color";

    String FILE_ATTRIBUTE = "file";

    String X1_ATTRIBUTE = "x1";

    String Y1_ATTRIBUTE = "y1";

    String X2_ATTRIBUTE = "x2";

    String Y2_ATTRIBUTE = "y2";

    String PATTERN_COLOR_ATTRIBUTE = "pat_color";

    String TEST_COLOR_ATTRIBUTE = "test_color";

    String MAXIMUM_ERROR_NUMBER_ATTRIBUTE = "max_err";

    String GROUP_TAG = "group";

    String ANY_ATTRIBUTE = "any";
}
