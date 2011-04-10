package com.github.rizar.labchecker.lab;

import com.github.rizar.labchecker.exceptions.MissedTagException;
import com.github.rizar.labchecker.exceptions.WrongConfigException;
import com.github.rizar.labchecker.exceptions.WrongNestedTagException;
import com.github.rizar.labchecker.exceptions.WrongAttributeValueException;
import com.github.rizar.labchecker.exceptions.WrongTagDataException;
import com.github.rizar.labchecker.exceptions.MissedAttributeException;
import com.github.rizar.labchecker.exceptions.DuplicateTagException;
import com.github.rizar.labchecker.exceptions.WrongRootTagException;
import static com.github.rizar.labchecker.lab.LabTags.*;
import static com.github.rizar.labchecker.lab.LabPredefinedMacros.*;
import static com.github.rizar.labchecker.lab.LabConstraints.*;

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
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * An class for accessing lab data.
 * @author Rizar
 */
public class Lab
{
    private File directory;

    private File config;

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
        this.config = config;
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

    //private String solutionNameMacro;
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

    /**
     * Get solution name macro.
     * @return solution name macro
     */
    /*public String getSolutionNameMacro()
    {
    return solutionNameMacro;
    }*/
    int getNumberOfColorsInSet()
    {
        return numberOfColorsInSet;
    }

    ColorSet[] getColorSets()
    {
        return colorSets;
    }

    List<Step> getSteps()
    {
        return steps;
    }

    MacroProcessor getMacroProcessor()
    {
        return macroProcessor;
    }

    private class ConfigHandler extends DefaultHandler
    {
        private Locator locator;

        private Stack<String> openedTags = new Stack<String>();

        private Set<String> allTags = new HashSet<String>();

        private StringBuilder colorSetStringBuiler;

        int colorSetNumber;

        @Override
        public void setDocumentLocator(Locator locator)
        {
            this.locator = locator;
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException
        {
            System.err.println("startElement " + qName);

            //check nesting tabs
            if (!openedTags.empty())
            {
                String currentTag = openedTags.peek();
                if (qName.equals(COLOR_SET_TAG))
                {
                    if (!currentTag.equals(COLOR_SETS_TAG))
                        throw new SAXException(new WrongNestedTagException(locator.
                                getLineNumber(), locator.getColumnNumber(),
                                COLOR_SET_TAG, true, COLOR_SETS_TAG));
                }
                else
                {
                    if (!currentTag.equals(LAB_TAG))
                        throw new SAXException(new WrongNestedTagException(locator.
                                getLineNumber(), locator.getColumnNumber(),
                                qName, false, currentTag));
                }
            }
            else
            {
                if (!qName.equals(LAB_TAG))
                    throw new SAXException(new WrongRootTagException(locator.
                            getLineNumber(), locator.getColumnNumber(), qName));
            }
            openedTags.push(qName);
            allTags.add(qName);

            //process attributes
            if (qName.equals(LAB_TAG))
            {
                try
                {
                    String lab = attributes.getValue(LAB_TAG_LAB_ATTRIBUTE);
                    macroProcessor.registerMacro(LAB_MACRO, lab);
                }
                catch (NullPointerException e)
                {
                    throw new SAXException(new MissedAttributeException(
                            locator.getLineNumber(), locator.getColumnNumber(),
                            LAB_TAG,
                            LAB_TAG_LAB_ATTRIBUTE));
                }
            }
            else if (qName.equals(MODULE_TAG))
            {
                try
                {
                    String moduleStr = attributes.getValue(
                            MODULE_TAG_MODULE_ATTRIBUTE);
                    if (moduleStr == null)
                        throw new SAXException(new MissedAttributeException(
                                locator.getLineNumber(),
                                locator.getColumnNumber(),
                                MODULE_TAG,
                                MODULE_TAG_MODULE_ATTRIBUTE));
                    module = Integer.parseInt(moduleStr);
                    if (module < MIN_MODULE || module > MAX_MODULE)
                        throw new SAXException(new WrongTagDataException(
                                locator.getLineNumber(),
                                locator.getColumnNumber(),
                                MODULE_TAG));
                    macroProcessor.setModule(module);
                }
                catch (NumberFormatException e)
                {
                    throw new SAXException(new WrongAttributeValueException(
                            locator.getLineNumber(), locator.getColumnNumber(),
                            MODULE_TAG,
                            MODULE_TAG_MODULE_ATTRIBUTE));
                }
            }
            /*else if (qName.equals(SOLUTION_NAME_TAG))
            {
            solutionNameMacro = attributes.getValue(
            SOLUTION_NAME_TAG_NAME_ATTRIBUTE);
            if (solutionNameMacro == null)
            throw new SAXException(new MissedAttributeException(
            locator.getLineNumber(), locator.getColumnNumber(),
            SOLUTION_NAME_TAG,
            SOLUTION_NAME_TAG_NAME_ATTRIBUTE));
            }*/
            else if (qName.equals(COLOR_SETS_TAG))
            {
                try
                {
                    if (colorSets != null)
                        throw new SAXException(new DuplicateTagException(
                                locator.getLineNumber(),
                                locator.getColumnNumber(),
                                COLOR_SETS_TAG));
                    colorSets = new ColorSet[COLOR_SETS_NUMBER];
                    String numberOfColorsInSetStr = attributes.getValue(
                            COLOR_SETS_TAG_NUMBER_OF_COLORS_IN_SET_ATTRIBUTE);
                    if (numberOfColorsInSetStr == null)
                        throw new SAXException(
                                new MissedAttributeException(
                                locator.getLineNumber(),
                                locator.getColumnNumber(),
                                COLOR_SETS_TAG,
                                COLOR_SETS_TAG_NUMBER_OF_COLORS_IN_SET_ATTRIBUTE));
                    numberOfColorsInSet = Integer.parseInt(
                            numberOfColorsInSetStr);
                }
                catch (NumberFormatException e)
                {
                    throw new SAXException(
                            new WrongAttributeValueException(locator.
                            getLineNumber(), locator.getColumnNumber(),
                            COLOR_SETS_TAG,
                            COLOR_SETS_TAG_NUMBER_OF_COLORS_IN_SET_ATTRIBUTE));
                }
            }
            else if (qName.equals(COLOR_SET_TAG))
            {
                try
                {
                    String colorSetNumberStr = attributes.getValue(
                            COLOR_SET_TAG_NUMBER_ATTRIBUTE);
                    if (colorSetNumberStr == null)
                        throw new SAXException(new MissedAttributeException(locator.
                                getLineNumber(), locator.getColumnNumber(),
                                COLOR_SET_TAG, COLOR_SET_TAG_NUMBER_ATTRIBUTE));
                    try
                    {
                        colorSetNumber = Integer.parseInt(colorSetNumberStr);
                    }
                    catch (IllegalArgumentException e)
                    {
                        throw new SAXException(new WrongTagDataException(locator.
                                getLineNumber(), locator.getColumnNumber(),
                                COLOR_SET_TAG, e.getMessage()));
                    }
                    colorSetStringBuiler = new StringBuilder();
                }
                catch (NumberFormatException e)
                {
                    throw new SAXException(new WrongAttributeValueException(
                            locator.getLineNumber(), locator.getColumnNumber(),
                            COLOR_SET_TAG, COLOR_SET_TAG_NUMBER_ATTRIBUTE));
                }
            }
            else if (qName.equals(STEP_TAG))
            {
                String script = attributes.getValue(STEP_TAG_SCRIPT_ATTRIBUTE);
                if (script == null)
                    throw new SAXException(new MissedAttributeException(locator.
                            getLineNumber(), locator.getColumnNumber(), STEP_TAG,
                            STEP_TAG_SCRIPT_ATTRIBUTE));
                String fileMacro = attributes.getValue(STEP_TAG_FILE_ATTRIBUTE);
                if (fileMacro == null)
                    throw new SAXException(new MissedAttributeException(locator.
                            getLineNumber(), locator.getColumnNumber(), STEP_TAG,
                            STEP_TAG_FILE_ATTRIBUTE));
                Step step = new Step(Lab.this, script, fileMacro);
                step.load();
                steps.add(step);
            }
            else if (qName.equals(MACRO_TAG))
            {
                String name = attributes.getValue(MACRO_TAG_NAME_ATTRIBUTE);
                if (name == null)
                    throw new SAXException(new MissedAttributeException(locator.
                            getLineNumber(), locator.getColumnNumber(),
                            MACRO_TAG, MACRO_TAG_NAME_ATTRIBUTE));
                String definition = attributes.getValue(
                        MACRO_TAG_DEFINITION_ATTRIBUTE);
                if (definition == null)
                    throw new SAXException(new MissedAttributeException(locator.
                            getLineNumber(), locator.getColumnNumber(),
                            MACRO_TAG, MACRO_TAG_DEFINITION_ATTRIBUTE));
                macroProcessor.registerMacro(name, definition);
            }
            else
            {
                /*throw new SAXException(new UnknownTagException(locator.
                getLineNumber(), locator.getColumnNumber(),
                qName));*/
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws
                SAXException
        {
            System.err.println("characters");

            if (colorSetStringBuiler != null)
                colorSetStringBuiler.append(new String(ch, start, length));
            /*String data = new String(ch, start, length).trim();
            if (data.equals(""))
            return;

            try
            {
            colorSetStringBuiler.append(
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
            System.err.println("endElement " + qName);

            if (colorSetStringBuiler != null)
            {
                try
                {
                    ColorSet cs = new ColorSet(
                            numberOfColorsInSet);
                    cs.parse(colorSetStringBuiler.toString());
                    colorSets[colorSetNumber] = cs;
                    colorSetStringBuiler = null;
                }
                catch (IllegalArgumentException e)
                {
                    throw new SAXException(new WrongTagDataException(locator.
                            getLineNumber(), locator.getColumnNumber(),
                            COLOR_SET_TAG, e.getMessage()));
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
                throw new SAXException(new MissedTagException(locator.
                        getLineNumber(), locator.getColumnNumber(),
                        mustSet.toArray(new String[0])[0]));
        }
    }

    /**
     * Load lab settings and probably images.
     */
    void load() throws WrongConfigException,
                       IOException,
                       SAXException
    {
        try
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(config, new ConfigHandler());
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
            /*throw cause.getClass().asSubclass(
            WrongConfigException.class).
            cast(cause);*/

            throw e;
        }
    }

    /**
     * Unload lab images.
     */
    void unload()
    {
    }

    /**
     * Get lab name.
     * @return lab name.
     */
    public String getLabName()
    {
        return directory.getName();
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
    StepChecker getStepChecker(File stepFile)
    {
        return null;
    }
}
