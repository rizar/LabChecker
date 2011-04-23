package com.github.rizar.labchecker.lab;

import com.github.rizar.labchecker.exceptions.MissedTagException;
import com.github.rizar.labchecker.exceptions.WrongConfigException;
import com.github.rizar.labchecker.exceptions.WrongNestedTagException;
import com.github.rizar.labchecker.exceptions.WrongAttributeValueException;
import com.github.rizar.labchecker.exceptions.WrongTagDataException;
import com.github.rizar.labchecker.exceptions.DuplicateTagException;
import com.github.rizar.labchecker.exceptions.UnknownTagException;
import com.github.rizar.labchecker.exceptions.WrongRootTagException;
import static com.github.rizar.labchecker.lab.Tags.*;
import static com.github.rizar.labchecker.lab.Macros.*;
import static com.github.rizar.labchecker.lab.Constraints.*;
import com.github.rizar.labchecker.test.ImageLibrary;
import java.awt.Color;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import java.util.Set;
import java.util.Stack;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * An class for accessing lab data.
 * @author Rizar
 */
public class Lab
{
    private File directory;

    private File configFile;

    private String labName;

    public File getConfigFile()
    {
        return configFile;
    }

    public File getDirectory()
    {
        return directory;
    }

    public static final FilenameFilter CONFIG_FILTER = new FilenameFilter()
    {
        public boolean accept(File dir, String name)
        {
            return name.equals("lab_config.xml");
        }
    };

    private Lab(File directory, File config)
    {
        this.directory = directory;
        this.configFile = config;
        this.library = new ImageLibrary(directory);
    }

    static Lab getInstance(File directory)
    {
        File[] configs = directory.listFiles(CONFIG_FILTER);
        try
        {
            return new Lab(directory, configs[0]);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            return null;
        }
        catch (NullPointerException e)
        {
            return null;
        }
    }

    // Config related stuff
    private MacroProcessor macroProcessor = new MacroProcessor();

    private List<Step> steps = new ArrayList<Step>();

    private int module;

    private int numberOfColorsInSet;

    ColorSet[] colorSets;

    /**
     * Get module according to which variant is chosen.
     * @return module
     */
    public int getModule()
    {
        return module;
    }

    int getNumberOfColorsInSet()
    {
        return numberOfColorsInSet;
    }

    ColorSet[] getColorSets()
    {
        return colorSets;
    }

    Color getColor(int colorSetNumber, int colorNumber)
    {
        return colorSets[colorSetNumber].getColor(colorNumber);
    }

    List<Step> getSteps()
    {
        return steps;
    }

    MacroProcessor getMacroProcessor()
    {
        return macroProcessor;
    }

    private class ConfigHandler extends ExtendedHandler
    {
        private Stack<String> openedTags = new Stack<String>();

        private Set<String> allTags = new HashSet<String>();

        private StringBuilder colorSetStringBuilder;

        private String currentMacro;

        int colorSetNumber;

        private String teacherName;

        private StringBuilder teacherBuilder;

        public ConfigHandler(File file)
        {
            super(file);
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException
        {
            if (PRINT_TAGS) System.err.println("startElement " + qName);

            //check nesting tabs
            if (!openedTags.empty())
            {
                String currentTag = openedTags.peek();
                if (qName.equals(COLOR_SET_TAG))
                {
                    if (!currentTag.equals(COLOR_SETS_TAG))
                        doThrow(WrongNestedTagException.class, COLOR_SET_TAG,
                                true, COLOR_SETS_TAG);
                }
                else if (qName.equals(DEFINITION_TAG))
                {
                    if (!currentTag.equals(MACRO_TAG))
                        doThrow(WrongNestedTagException.class, DEFINITION_TAG,
                                true, MACRO_TAG);
                }
                else if (qName.equals(TEACHER_TAG))
                {
                    if (!currentTag.equals(TEACHERS_TAG))
                        doThrow(WrongNestedTagException.class, TEACHER_TAG, true, TEACHERS_TAG);
                }
                else
                {
                    if (!currentTag.equals(LAB_TAG))
                        doThrow(WrongNestedTagException.class, qName, false,
                                currentTag);
                }
            }
            else
            {
                if (!qName.equals(LAB_TAG))
                    doThrow(WrongRootTagException.class, qName);
            }
            openedTags.push(qName);
            allTags.add(qName);

            //process attributes
            if (qName.equals(LAB_TAG))
            {
                String lab = mustGet(attributes, LAB_TAG, LAB_TAG_LAB_ATTRIBUTE);
                macroProcessor.registerMacro(LAB_MACRO, lab);
                labName = attributes.getValue(NAME_ATTRIBUTE);
            }
            else if (qName.equals(MODULE_TAG))
            {
                try
                {
                    String moduleStr = mustGet(attributes, MODULE_TAG,
                            MODULE_TAG_MODULE_ATTRIBUTE);
                    module = Integer.parseInt(moduleStr);
                    if (module < MIN_MODULE || module > MAX_MODULE)
                        doThrow(WrongTagDataException.class, MODULE_TAG);
                    macroProcessor.setModule(module);
                }
                catch (NumberFormatException e)
                {
                    doThrow(WrongAttributeValueException.class,
                            MODULE_TAG,
                            MODULE_TAG_MODULE_ATTRIBUTE);
                }
            }
            else if (qName.equals(COLOR_SETS_TAG))
            {
                try
                {
                    if (colorSets != null)
                        doThrow(DuplicateTagException.class,
                                COLOR_SETS_TAG);
                    colorSets = new ColorSet[COLOR_SETS_NUMBER];
                    String numberOfColorsInSetStr = mustGet(attributes,
                            COLOR_SETS_TAG,
                            COLOR_SETS_TAG_NUMBER_OF_COLORS_IN_SET_ATTRIBUTE);
                    numberOfColorsInSet = Integer.parseInt(
                            numberOfColorsInSetStr);
                }
                catch (NumberFormatException e)
                {
                    doThrow(WrongAttributeValueException.class,
                            COLOR_SETS_TAG,
                            COLOR_SETS_TAG_NUMBER_OF_COLORS_IN_SET_ATTRIBUTE);
                }
            }
            else if (qName.equals(COLOR_SET_TAG))
            {
                try
                {
                    String colorSetNumberStr = mustGet(attributes, COLOR_SET_TAG,
                            COLOR_SET_TAG_NUMBER_ATTRIBUTE);
                    colorSetNumber = Integer.parseInt(colorSetNumberStr);
                    colorSetStringBuilder = new StringBuilder();
                }
                catch (NumberFormatException e)
                {
                    doThrow(WrongAttributeValueException.class,
                            COLOR_SET_TAG, COLOR_SET_TAG_NUMBER_ATTRIBUTE);
                }
            }
            else if (qName.equals(STEP_TAG))
            {
                String script = mustGet(attributes, STEP_TAG,
                        STEP_TAG_SCRIPT_ATTRIBUTE);
                String fileMacro = mustGet(attributes, STEP_TAG,
                        STEP_TAG_FILE_ATTRIBUTE);
                Step step = new Step(Lab.this, script, fileMacro);
                steps.add(step);
            }
            else if (qName.equals(MACRO_TAG))
            {
                String name = mustGet(attributes, MACRO_TAG,
                        NAME_ATTRIBUTE);
                currentMacro = name;
                String definition = attributes.getValue(DEFINITION_ATTRIBUTE);
                macroProcessor.registerMacro(name, definition);
            }
            else if (qName.equals(DEFINITION_TAG))
            {
                String definitionMacro = mustGet(attributes, DEFINITION_TAG, DEFINITION_ATTRIBUTE);
                String groupMacro = attributes.getValue(GROUP_ATTRIBUTE);
                String remainderMacro = attributes.getValue(REMAINDER_ATTRIBUTE);
                macroProcessor.addDefinition(currentMacro, groupMacro, remainderMacro, definitionMacro);
            }
            else if (qName.equals(TEACHERS_TAG))
            {
            }
            else if (qName.equals(TEACHER_TAG))
            {
                teacherName = mustGet(attributes, TEACHER_TAG, NAME_ATTRIBUTE);
                teacherBuilder = new StringBuilder();
            }
            else
            {
                doThrow(UnknownTagException.class, qName);
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws
                SAXException
        {
            if (PRINT_TAGS) System.err.println("characters");

            if (colorSetStringBuilder != null)
                colorSetStringBuilder.append(new String(ch, start, length));
            if (teacherBuilder != null)
                teacherBuilder.append(new String(ch, start, length));

            /*String data = new String(ch, start, length).trim();
            if (data.equals(""))
            return;

            try
            {
            colorSetStringBuilder.append(
            new String(ch, start, length));
            }
            catch (NullPointerException e)
            {
            throw new SAXException(new WrongTagDataException(locator.
            getLineNumber(), locator.getColumnNumber(),
            openedTags.peek()));
            }*/
        }

        @Override
        public void endElement(String uri, String localName,
                               String qName)
                throws SAXException
        {
            if (PRINT_TAGS) System.err.println("endElement " + qName);

            if (colorSetStringBuilder != null)
            {
                try
                {
                    ColorSet cs = new ColorSet(
                            numberOfColorsInSet);
                    cs.parse(colorSetStringBuilder.toString());
                    colorSets[colorSetNumber] = cs;
                    colorSetStringBuilder = null;
                }
                catch (IllegalArgumentException e)
                {
                    doThrow(WrongTagDataException.class,
                            COLOR_SET_TAG, e.getMessage());
                }
            }

            if (teacherBuilder != null)
            {
                String lastCodeStr = "";
                try
                {
                    String [] codes = teacherBuilder.toString().split(CODES_SEPARATOR);
                    for (String codeStr : codes)
                        if (!codeStr.equals(""))
                        {
                            lastCodeStr = codeStr;
                            int code = Lab.codeInteger(codeStr);
                            if (code < 100 || code >= 10000)
                                doThrow(WrongTagDataException.class,
                                        TEACHER_TAG, String.format("Wrong values for code: %d", code));
                            macroProcessor.setTeacher(codeStr, teacherName);
                        }
                    teacherBuilder = null;
                }
                catch (NumberFormatException e)
                {
                    doThrow(WrongTagDataException.class, TEACHER_TAG, String.format("Wrong value for code: %s", lastCodeStr));
                }
            }

            openedTags.pop();
        }

        @Override
        public void endDocument() throws SAXException
        {
            Set<String> mustSet = new HashSet<String>(Arrays.asList(
                    OBLIGATORY_TAGS));
            mustSet.removeAll(allTags);
            if (!mustSet.isEmpty())
                doThrow(MissedTagException.class,
                        mustSet.toArray(new String[0])[0]);
        }
    }

    /**
     * Load lab settings and probably images.
     */
    public void load() throws WrongConfigException,
                              IOException,
                              SAXException
    {
        try
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(configFile, new ConfigHandler(configFile));

            for (Step step : steps)
                step.load();
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        catch (SAXException e)
        {
            Throwable cause = e.getCause();
            if (cause instanceof WrongConfigException)
                throw (WrongConfigException) cause;

            throw e;
        }
    }

    private ImageLibrary library;

    public ImageLibrary getImageLibrary()
    {
        return library;
    }

    /**
     * Unload lab images.
     */
    public void unload()
    {
        library.unload();
    }

    /**
     * Get lab name.
     * @return lab name.
     */
    public String getLabName()
    {
        return labName != null ? labName : directory.getName();
    }

    /**
     * Get solution name for step file according to lab settings.
     * @param <code>stepFile</code> - reference to step file.
     * @return solution name, if <code>stepFile</code> is well-named step file, <code>null</code> otherwise.
     */
    public String getSolutionName(File stepFile)
    {
        for (Step step : steps)
            if (step.isStepFile(stepFile))
                return step.getSolutionName(stepFile);
        return null;
    }

    /**
     * Get <code>StepChecker</code> object to check <code>stepFile</code>
     * @return <code>StepChecker</code> object is <code>stepFile</code> is well-named step file, <code>null</code> otherwise.
     */
    public StepChecker getStepChecker(File stepFile)
    {
        for (Step step : steps)
            if (step.isStepFile(stepFile))
                return step.getStepChecker(stepFile);
        return null;
    }

    public static int codeInteger(String code)
    {
        return 100 * Character.digit(code.charAt(0), MAX_NUMBER_OF_GROUPS) + 10 * Character.digit(code.
                charAt(1), 10) + Character.digit(code.charAt(2), 10);
    }

    public static int codeGroup(String code)
    {
        if (code.charAt(0) < '9')
            return Character.digit(code.charAt(0), 10);
        else
        {
            int number = Integer.parseInt(code.substring(1));
            if (number < 30)
                return 9;
            else if (number < 70)
                return 10;
            else
                return 11;
        }
    }

    @Override
    public String toString()
    {
        return getLabName();
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof Lab && directory.equals(((Lab)obj).directory);
    }

        @Override
    public int hashCode()
    {
        return directory.hashCode();
    }
}
