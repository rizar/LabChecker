package com.github.rizar.labchecker.lab;

import com.github.rizar.labchecker.exceptions.InfiniteCycleException;
import com.github.rizar.labchecker.exceptions.UndefinedMacroException;
import static com.github.rizar.labchecker.lab.LabPredefinedMacros.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Rizar
 */
public class MacroProcessor
{
    public static final int MAX_ITERATION_NUMBER = 100;

    Map<String, String> macros = new HashMap<String, String>();

    public MacroProcessor()
    {
        registerMacro(GET_CODE_MACRO, GET_CODE_MACRO_DEFINITION);
        registerMacro(GET_VAR_MACRO, GET_VAR_MACRO_DEFINITION);
    }

    private int module, group;

    private String code;

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        registerMacro(CODE_MACRO, code);
        this.code = code;
    }

    public int getGroup()
    {
        return group;
    }

    public void setGroup(int group)
    {
        String def = Integer.toString(group);
        if (def.length() == 1)
            def = "0" + def;
        registerMacro(GROUP_MACRO, def);
        this.group = group;
    }

    public int getModule()
    {
        return module;
    }

    public void setModule(int module)
    {
        registerMacro(MODULE_MACRO, Integer.toString(module));
        this.module = module;
    }

    final public void registerMacro(String macro, String definition)
    {
        if (definition == null)
            throw new NullPointerException();
        macros.put(macro, definition);
    }

    String[] getMacros()
    {
        List<String> macrosList = new ArrayList<String>();
        for (Map.Entry<String, String> entry : macros.entrySet())
            macrosList.add(entry.getKey() + " " + entry.getValue());
        return macrosList.toArray(new String[0]);
    }

    private int codeInteger()
    {
        return 100 * Character.digit(code.charAt(0), 26) + 10 * Character.digit(code.
                charAt(1), 10) + Character.digit(code.charAt(2), 10);
    }

    private String processMacro(String macro)
    {
        if (macro.equals(REMAINDER_MACRO))
        {
            int rem = codeInteger() % module;
            return Integer.toString(rem);
        }
        else if (macro.equals(REMAINDER_SUFFIX_MACRO))
        {
            int rem = codeInteger() % module;
            return rem != 0 ? ("+" + Integer.toString(rem)) : "";
        }
        else
        {
            String ret = macros.get(macro);
            if (ret == null)
                throw new UndefinedMacroException("", macro);
            return ret;
        }
    }

    private Pattern macroPattern = Pattern.compile("%(.*?)%");

    public String process(String text)
    {
        boolean noMacros = false;
        int iterationNumber = 0;
        do
        {
            iterationNumber++;
            if (iterationNumber > MAX_ITERATION_NUMBER)
                throw new InfiniteCycleException();

            noMacros = true;
            StringBuilder sb = new StringBuilder();
            Matcher matcher = macroPattern.matcher(text);
            int index = 0;
            while (matcher.find())
            {
                noMacros = false;
                sb.append(text, index, matcher.start());
                String macro = matcher.group(1);
                sb.append(processMacro(macro));
                index = matcher.end();
            }
            sb.append(text, index, text.length());
            text = sb.toString();
        }
        while (!noMacros);
        return text;
    }
}
