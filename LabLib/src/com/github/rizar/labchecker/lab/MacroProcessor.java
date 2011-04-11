package com.github.rizar.labchecker.lab;

import com.github.rizar.labchecker.exceptions.InfiniteCycleException;
import com.github.rizar.labchecker.exceptions.UndefinedMacroException;
import static com.github.rizar.labchecker.lab.Macros.*;

import java.util.HashMap;
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
        init();
    }

    public MacroProcessor(MacroProcessor macroProcessor)
    {
        macros = new HashMap(macroProcessor.macros);
        code = macroProcessor.code;
        group = macroProcessor.group;
        module = macroProcessor.module;
        init();
    }

    private void init()
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
        registerMacro(GROUP_TWO_CHARACTERS_MACRO, def);
        char c = (char)(group < 10 ? '0' + group : 'a' + group - 10);
        registerMacro(GROUP_ONE_CHARACTER_MACRO, Character.toString(c));
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

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder ();
        for (Map.Entry<String, String> entry : macros.entrySet())
        {
            sb.append(entry.getKey());
            sb.append(" -> ");
            sb.append(entry.getValue());
            sb.append("\n");
        }
        return sb.toString();
    }

    private String processMacro(String macro)
    {
        try
        {
            if (macro.equals(REMAINDER_MACRO))
            {
                int rem = Lab.codeInteger(code) % module;
                return Integer.toString(rem);
            }
            else if (macro.equals(REMAINDER_SUFFIX_MACRO))
            {
                int rem = Lab.codeInteger(code) % module;
                return rem != 0 ? ("+" + Integer.toString(rem)) : "";
            }
            else
            {
                String ret = macros.get(macro);
                return ret.toString();
            }
        }
        catch (Exception e)
        {
            throw new UndefinedMacroException("", macro);
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
