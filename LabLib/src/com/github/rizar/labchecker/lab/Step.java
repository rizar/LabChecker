package com.github.rizar.labchecker.lab;

import com.github.rizar.labchecker.exceptions.TestException;
import com.github.rizar.labchecker.exceptions.WrongConfigException;
import com.github.rizar.labchecker.exceptions.WrongNestedTagException;
import com.github.rizar.labchecker.exceptions.WrongRootTagException;
import com.github.rizar.labchecker.test.ColorSetTest;
import static com.github.rizar.labchecker.lab.Macros.*;
import static com.github.rizar.labchecker.lab.Tags.*;
import static com.github.rizar.labchecker.lab.Constraints.*;
import com.github.rizar.labchecker.test.FileSizeTest;
import com.github.rizar.labchecker.test.ImageSizeTest;
import com.github.rizar.labchecker.test.Log;
import com.github.rizar.labchecker.test.PatternTest;
import java.awt.Color;

import java.io.File;
import java.io.IOException;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import loadimg.LoadImgException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Rizar
 */
class Step
{
    private Lab lab;

    private File scriptFile;

    private Pattern filePattern;

    public File getScriptFile()
    {
        return scriptFile;
    }

    public Step(Lab lab, String scriptMacro, String fileMacro)
    {
        this.lab = lab;
        this.scriptFile = new File(lab.getDirectory(), lab.getMacroProcessor().
                process(scriptMacro));
        this.filePattern = Pattern.compile(lab.getMacroProcessor().process(
                fileMacro));
    }

    private TestsForGroup rootTest;

    TestsForGroup getRootTest()
    {
        return rootTest;
    }

    private class ConfigHandler extends ExtendedHandler
    {
        Stack<String> openedTags = new Stack<String>();

        Stack<TestsForGroup> stack = new Stack<TestsForGroup>();

        private TestFor newTestFor;

        private ColorSetTest newColorSetTest;

        private PatternTest newPatternTest;

        public ConfigHandler(File file)
        {
            super(file);
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException
        {
            if (PRINT_TAGS)
                System.err.println("startElement " + qName);

            //check nested tags
            if (!openedTags.empty())
            {
                String currentTag = openedTags.peek();
                if (qName.equals(PATTERN_TEST_COLOR_TAG)
                        || qName.equals(
                        PATTERN_TEST_PATTERN_RECTANGLE_TAG)
                        || qName.equals(
                        PATTERN_TEST_TEST_RECTANGLE_TAG)
                        || qName.equals(
                        IGNORE_TAG))
                {
                    if (!currentTag.equals(PATTERN_TEST_TAG))
                        doThrow(WrongNestedTagException.class, qName, true,
                                PATTERN_TEST_TAG);
                }
                else if (qName.equals(ADD_COLOR_TAG) || qName.equals(
                        REMOVE_COLOR_TAG))
                {
                    if (!currentTag.equals(COLOR_SET_TEST_TAG))
                        doThrow(WrongNestedTagException.class, qName, true,
                                COLOR_SET_TEST_TAG);
                }
                else if (!currentTag.equals(SCRIPT_TAG) && !currentTag.equals(
                        GROUP_TAG))
                    doThrow(WrongNestedTagException.class, qName, false,
                            currentTag);
            }
            else if (!qName.equals(SCRIPT_TAG))
                doThrow(WrongRootTagException.class, qName);
            openedTags.add(qName);

            //process attributes
            if (qName.equals(SCRIPT_TAG))
            {
                stack.add(new TestsForGroup(attributes));
            }
            else if (qName.equals(FILE_SIZE_TEST_TAG))
            {
                newTestFor = new TestFor(attributes);
                String sizeMacro = mustGet(attributes, FILE_SIZE_TEST_TAG,
                        FILE_SIZE_TEST_SIZE_ATTRIBUTE);
                String devMacro = mustGet(attributes, FILE_SIZE_TEST_TAG,
                        FILE_SIZE_TEST_DEVIATION_ATTRIBUTE);
                newTestFor.setTest(new FileSizeTest(sizeMacro, devMacro));
                stack.peek().addTestFor(newTestFor);
            }
            else if (qName.equals(IMAGE_SIZE_TEST_TAG))
            {
                newTestFor = new TestFor(attributes);
                String heightMacro = mustGet(attributes, IMAGE_SIZE_TEST_TAG,
                        IMAGE_SIZE_TEST_HEIGHT_TAG);
                String widthMacro = mustGet(attributes, IMAGE_SIZE_TEST_TAG,
                        IMAGE_SIZE_TEST_WIDTH_TAG);
                newTestFor.setTest(new ImageSizeTest(heightMacro, widthMacro));
                stack.peek().addTestFor(newTestFor);
            }
            else if (qName.equals(COLOR_SET_TEST_TAG))
            {
                newTestFor = new TestFor(attributes);
                newColorSetTest = new ColorSetTest();
                newTestFor.setTest(newColorSetTest);
            }
            else if (qName.equals(ADD_COLOR_TAG))
            {
                String file = attributes.getValue(FILE_ATTRIBUTE);
                if (file != null)
                    newColorSetTest.includeAddFileMacro(file);
                else
                {
                    newColorSetTest.includeAddColorMacro(mustGet(attributes,
                            ADD_COLOR_TAG, COLOR_ATTRIBUTE));
                }
            }
            else if (qName.equals(REMOVE_COLOR_TAG))
            {
                String file = attributes.getValue(FILE_ATTRIBUTE);
                if (file != null)
                    newColorSetTest.includeRemoveColorMacro(file);
                else
                {
                    newColorSetTest.includeRemoveColorMacro(mustGet(attributes,
                            REMOVE_COLOR_TAG, COLOR_ATTRIBUTE));
                }
            }
            else if (qName.equals(PATTERN_TEST_TAG))
            {
                newTestFor = new TestFor(attributes);
                newPatternTest = new PatternTest(mustGet(attributes,
                        PATTERN_TEST_TAG, FILE_ATTRIBUTE), attributes.getValue(
                        PATTERN_TEST_SEEK_ATTRIBUTE));
                newTestFor.setTest(newPatternTest);
            }
            else if (qName.equals(PATTERN_TEST_PATTERN_RECTANGLE_TAG))
            {
                String x1 = mustGet(attributes,
                        PATTERN_TEST_PATTERN_RECTANGLE_TAG, X1_ATTRIBUTE);
                String y1 = mustGet(attributes,
                        PATTERN_TEST_PATTERN_RECTANGLE_TAG, Y1_ATTRIBUTE);
                String x2 = mustGet(attributes,
                        PATTERN_TEST_PATTERN_RECTANGLE_TAG, X2_ATTRIBUTE);
                String y2 = mustGet(attributes,
                        PATTERN_TEST_PATTERN_RECTANGLE_TAG, Y2_ATTRIBUTE);
                newPatternTest.setPatternRectangleMacro(
                        x1 + " " + y1 + " " + x2 + " " + y2);
            }
            else if (qName.equals(PATTERN_TEST_TEST_RECTANGLE_TAG))
            {
                String x1 = mustGet(attributes, PATTERN_TEST_TEST_RECTANGLE_TAG,
                        X1_ATTRIBUTE);
                String y1 = mustGet(attributes, PATTERN_TEST_TEST_RECTANGLE_TAG,
                        Y1_ATTRIBUTE);
                String x2 = mustGet(attributes, PATTERN_TEST_TEST_RECTANGLE_TAG,
                        X2_ATTRIBUTE);
                String y2 = mustGet(attributes, PATTERN_TEST_TEST_RECTANGLE_TAG,
                        Y2_ATTRIBUTE);
                newPatternTest.setTestRectangleMacro(
                        x1 + " " + y1 + " " + x2 + " " + y2);
            }
            else if (qName.equals(PATTERN_TEST_COLOR_TAG))
            {
                String patternColor = mustGet(attributes, PATTERN_TEST_COLOR_TAG,
                        PATTERN_COLOR_ATTRIBUTE);
                String testColor = mustGet(attributes, PATTERN_TEST_COLOR_TAG,
                        TEST_COLOR_ATTRIBUTE);
                newPatternTest.addColorMapping(patternColor, testColor);
            }
            else if (qName.equals(IGNORE_TAG))
            {
                String file = mustGet(attributes, IGNORE_TAG, FILE_ATTRIBUTE);
                String color = mustGet(attributes, IGNORE_TAG, COLOR_ATTRIBUTE);
                newPatternTest.addIgnoreMask(file, color);
            }
            else if (qName.equals(GROUP_TAG))
            {
                stack.add(new TestsForGroup(attributes));
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException
        {
            String currentTag = openedTags.peek();
            if (currentTag.equals(COLOR_SET_TEST_TAG))
            {
                stack.peek().addTestFor(newTestFor);
            }
            else if (currentTag.equals(PATTERN_TEST_TAG))
            {
                stack.peek().addTestFor(newTestFor);
            }
            else if (currentTag.equals(GROUP_TAG))
            {
                TestsForGroup group = stack.pop();
                stack.peek().addTestFor(group);
            }

            openedTags.pop();
        }

        @Override
        public void endDocument() throws SAXException
        {
            rootTest = stack.pop();
        }
    }

    public void load() throws WrongConfigException,
                              SAXException,
                              IOException
    {
        try
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(scriptFile, new ConfigHandler(scriptFile));
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

    public boolean isStepFile(File file)
    {
        //Lower case matching is important.
        return filePattern.matcher(file.getName().toLowerCase()).matches();
    }

    public String getSolutionName(File file)
    {
        Matcher matcher = filePattern.matcher(file.getName().toLowerCase());
        if (!matcher.matches())
            return null;
        MacroProcessor mp = new MacroProcessor(lab.getMacroProcessor());
        String code = matcher.group(1);
        mp.setCode(code);
        String var = matcher.group(2);
        mp.registerMacro(VARIANT_MACRO, var);
        return mp.process("%" + SOLUTION_MACRO + "%");
    }

    class InnerStepChecker implements StepChecker
    {
        private File stepFile;

        private MacroProcessor macroProcessor;

        MacroProcessor getMacroProcessor()
        {
            return macroProcessor;
        }

        public InnerStepChecker(File stepFile)
        {
            this.stepFile = stepFile;

            macroProcessor = new MacroProcessor(lab.getMacroProcessor());
            Matcher matcher = filePattern.matcher(stepFile.getName().toLowerCase());
            matcher.matches();
            String code = matcher.group(1);
            macroProcessor.setCode(matcher.group(1));
            macroProcessor.setGroup(Lab.codeGroup(code));
            macroProcessor.registerMacro(VARIANT_MACRO, matcher.group(2));
            int colorSetNumber = Character.digit(code.charAt(2), 10);
            for (int colorNumber = 1; colorNumber <= lab.getNumberOfColorsInSet(); colorNumber++)
            {
                Color color = lab.getColor(colorSetNumber, colorNumber);
                String colorStr = color.getRed() + "," + color.getGreen() + "," + color.
                        getBlue();
                macroProcessor.registerMacro(COLOR_MACRO_PREFIX + colorNumber,
                        colorStr);
                String invertedColorStr = (255 - color.getRed()) + "," + (255 - color.
                        getGreen()) + "," + (255 - color.getBlue());
                macroProcessor.registerMacro(
                        COLOR_MACRO_PREFIX + colorNumber + INVERTED_COLOR_MACRO_SUFFIX,
                        invertedColorStr);
            }
        }

        boolean checkResult;

        Log message, log;

        public boolean check() throws LoadImgException,
                                      TestException
        {
            checkResult = rootTest.check(macroProcessor, lab.getImageLibrary(),
                    stepFile);

            message = rootTest.getMessage();
            log = rootTest.getLog();
            isCheckedFlag = true;

            lab.getImageLibrary().releaseImage(stepFile);

            return checkResult;
        }

        public boolean getCheckResult()
        {
            return checkResult;
        }

        private boolean isCheckedFlag;

        public boolean isChecked()
        {
            return isCheckedFlag;
        }

        public Log getLog()
        {
            return log;
        }

        public Log getMessage()
        {
            return message;
        }

        public File getStepFile()
        {
            return stepFile;
        }

        TestsForGroup getRootTest()
        {
            return rootTest;
        }
    }

    StepChecker getStepChecker(File stepFile)
    {
        return new InnerStepChecker(stepFile);
    }
}
