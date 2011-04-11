package com.github.rizar.labchecker.lab;

import com.github.rizar.labchecker.exceptions.MissedAttributeException;
import java.io.File;
import java.lang.reflect.Constructor;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Rizar
 */
public class ExtendedHandler extends DefaultHandler
{
    private Locator locator;

    private File file;

    public ExtendedHandler(File file)
    {
        this.file = file;
    }

    @Override
    public void setDocumentLocator(Locator locator)
    {
        this.locator = locator;
    }

    protected String mustGet(Attributes attributes, String tag, String attr)
            throws SAXException
    {
        String object = attributes.getValue(attr);
        if (object == null)
            doThrow(MissedAttributeException.class, tag, attr);
        return object;
    }

    protected void doThrow(Class<? extends Exception> c, Object... args)
            throws
            SAXException
    {
        try
        {
            Object[] args3 = new Object[args.length + 3];
            args3[0] = file;
            args3[1] = locator.getLineNumber();
            args3[2] = locator.getColumnNumber();
            System.arraycopy(args, 0, args3, 3, args.length);

            Class[] classes3 = new Class[args3.length];
            classes3[0] = File.class;
            classes3[1] = classes3[2] = int.class;
            for (int i = 0; i < args.length; i++)
                classes3[i + 3] = args[i].getClass();
            
            Constructor<? extends Exception> constr = c.getConstructor(
                    classes3);
            throw new SAXException(constr.newInstance(args3));
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
