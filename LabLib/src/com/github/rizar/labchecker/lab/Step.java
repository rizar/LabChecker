package com.github.rizar.labchecker.lab;

import com.github.rizar.labchecker.exceptions.MissedAttributeException;
import com.github.rizar.labchecker.exceptions.WrongConfigException;
import com.github.rizar.labchecker.exceptions.WrongNestedTagException;
import com.github.rizar.labchecker.exceptions.WrongRootTagException;
import com.github.rizar.labchecker.test.ColorSetTest;
import static com.github.rizar.labchecker.lab.PredefinedMacros.*;
import static com.github.rizar.labchecker.lab.Tags.*;
import com.github.rizar.labchecker.test.FileSizeTest;
import com.github.rizar.labchecker.test.ImageSizeTest;
import com.github.rizar.labchecker.test.PatternTest;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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

    private class ConfigHandler extends DefaultHandler
    {
        private Locator locator;

        Stack<String> openedTags = new Stack<String>();

        Stack<TestsForGroup> stack = new Stack<TestsForGroup>();

        private TestFor newTestFor;

        private ColorSetTest newColorSetTest;

        private PatternTest newPatternTest;

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

            //check nested tags
            if (!openedTags.empty())
            {
                String currentTag = openedTags.peek();
                if (qName.equals(PATTERN_TEST_COLOR_TAG) || qName.equals(
                        PATTERN_TEST_PATTERN_RECTANGLE_TAG) || qName.equals(
                        PATTERN_TEST_TEST_RECTANGLE_TAG))
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
                stack.add(new TestsForGroup(lab.getMacroProcessor(), attributes));
            }
            else if (qName.equals(FILE_SIZE_TEST_TAG))
            {
                newTestFor = new TestFor(lab.getMacroProcessor(),
                        attributes);
                String sizeMacro = mustGet(attributes, FILE_SIZE_TEST_TAG,
                        FILE_SIZE_TEST_SIZE_ATTRIBUTE);
                String devMacro = mustGet(attributes, FILE_SIZE_TEST_TAG,
                        FILE_SIZE_TEST_DEVIATION_ATTRIBUTE);
                newTestFor.setTest(new FileSizeTest(lab.getMacroProcessor(),
                        sizeMacro, devMacro));
                stack.peek().addTestFor(newTestFor);
            }
            else if (qName.equals(IMAGE_SIZE_TEST_TAG))
            {
                newTestFor = new TestFor(lab.getMacroProcessor(),
                        attributes);
                String heightMacro = mustGet(attributes, IMAGE_SIZE_TEST_TAG,
                        IMAGE_SIZE_TEST_HEIGHT_TAG);
                String widthMacro = mustGet(attributes, IMAGE_SIZE_TEST_TAG,
                        IMAGE_SIZE_TEST_WIDTH_TAG);
                newTestFor.setTest(new ImageSizeTest(lab.getMacroProcessor(),
                        heightMacro, widthMacro));
                stack.peek().addTestFor(newTestFor);
            }
            else if (qName.equals(COLOR_SET_TEST_TAG))
            {
                newTestFor = new TestFor(lab.getMacroProcessor(),
                        attributes);
                newColorSetTest = new ColorSetTest(lab.getMacroProcessor());
                newTestFor.setTest(newColorSetTest);
            }
            else if (qName.equals(ADD_COLOR_TAG))
            {
                String file = attributes.getValue(FILE_ATTRIBUTE);
                if (file != null)
                    newColorSetTest.includeAddColorMacro(file);
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
                newTestFor = new TestFor(lab.getMacroProcessor(), attributes);
                newPatternTest = new PatternTest(lab.getMacroProcessor(), attributes.
                        getValue(PATTERN_TEST_SEEK_ATTRIBUTE));
                newTestFor.setTest(newPatternTest);
            }
            else if (qName.equals(PATTERN_TEST_PATTERN_RECTANGLE_TAG))
            {
                String x1 = mustGet(attributes, PATTERN_TEST_PATTERN_RECTANGLE_TAG, X1_ATTRIBUTE);
                String y1 = mustGet(attributes, PATTERN_TEST_PATTERN_RECTANGLE_TAG, Y1_ATTRIBUTE);
                String x2 = mustGet(attributes, PATTERN_TEST_PATTERN_RECTANGLE_TAG, X2_ATTRIBUTE);
                String y2 = mustGet(attributes, PATTERN_TEST_PATTERN_RECTANGLE_TAG, Y2_ATTRIBUTE);
                newPatternTest.setPatternRectangleMacro(x1 + " " + y1 + " " + x2 + " " + y2);
            }
            else if (qName.equals(PATTERN_TEST_TEST_RECTANGLE_TAG))
            {
                String x1 = mustGet(attributes, PATTERN_TEST_TEST_RECTANGLE_TAG, X1_ATTRIBUTE);
                String y1 = mustGet(attributes, PATTERN_TEST_TEST_RECTANGLE_TAG, Y1_ATTRIBUTE);
                String x2 = mustGet(attributes, PATTERN_TEST_TEST_RECTANGLE_TAG, X2_ATTRIBUTE);
                String y2 = mustGet(attributes, PATTERN_TEST_TEST_RECTANGLE_TAG, Y2_ATTRIBUTE);
                newPatternTest.setTestRectangleMacro(x1 + " " + y1 + " " + x2 + " " + y2);
            }
            else if (qName.equals(PATTERN_TEST_COLOR_TAG))
            {
                String patternColor = mustGet(attributes, PATTERN_TEST_COLOR_TAG, PATTERN_COLOR_ATTRIBUTE);
                String testColor = mustGet(attributes, PATTERN_TEST_COLOR_TAG, TEST_COLOR_ATTRIBUTE);
                newPatternTest.addColorMapping(patternColor, testColor);
            }
            else if (qName.equals(GROUP_TAG))
            {
                stack.add(new TestsForGroup(lab.getMacroProcessor(), attributes));
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

        private String mustGet(Attributes attributes, String tag, String attr)
                throws SAXException
        {
            String object = attributes.getValue(attr);
            if (object == null)
                doThrow(MissedAttributeException.class, tag, attr);
            return object;
        }

        private void doThrow(Class<? extends Exception> c, Object... args)
                throws
                SAXException
        {
            try
            {
                Object[] args2 = new Object[args.length + 2];
                args2[0] = locator.getLineNumber();
                args2[1] = locator.getColumnNumber();
                System.arraycopy(args, 0, args2, 2, args.length);
                Class[] classes2 = new Class[args2.length];
                classes2[0] = classes2[1] = int.class;
                for (int i = 0; i < args.length; i++)
                    classes2[i + 2] = args[i].getClass();
                Constructor<? extends Exception> constr = c.getConstructor(
                        classes2);
                throw new SAXException(constr.newInstance(args2));
            }
            catch (Exception e)
            {
                if (e instanceof SAXException)
                    throw (SAXException) e;
                else
                    e.printStackTrace();
            }
        }
    }

    public void load() throws WrongConfigException,
                              SAXException,
                              IOException
    {
        //TODO insert file name in error message.

        try
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(scriptFile, new ConfigHandler());
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
        return filePattern.matcher(file.getName()).matches();
    }

    public String getSolutionName(File file)
    {
        Matcher matcher = filePattern.matcher(file.getName());
        if (!matcher.matches())
            return null;
        MacroProcessor mp = lab.getMacroProcessor();
        String code = matcher.group(1);
        mp.setCode(code);
        String var = matcher.group(2);
        mp.registerMacro(VARIANT_MACRO, var);
        return mp.process("%" + SOLUTION_MACRO + "%");
    }
}
