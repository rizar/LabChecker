package com.github.rizar.labchecker.lab;

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
    Map<String, String> macros = new HashMap<String, String> ();

    public MacroProcessor()
    {
        registerMacro(GET_CODE_MACRO, GET_CODE_MACRO_DEFINITION);
        registerMacro(GET_VAR_MACRO, GET_VAR_MACRO_DEFINITION);
    }

    private int module, group, code;

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        registerMacro(CODE_MACRO, Integer.toString(code));
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
        List<String> macrosList = new ArrayList<String> ();
        for (Map.Entry<String, String> entry : macros.entrySet())
            macrosList.add(entry.getKey() + " " + entry.getValue());
        return macrosList.toArray(new String [0]);
    }

    private String processMacro(String macro)
    {
        if (macro.equals(REMAINDER_MACRO))
            return Integer.toString(code % module);
        else if (macro.equals(REMAINDER_SUFFIX_MACRO))
        {
            int rem = code % module;
            return rem != 0 ? ("+" + Integer.toString(rem)) : "";
        }
        else
            return macros.get(macro);
    }

    private Pattern macroPattern = Pattern.compile("%(.*?)%");

    public String process(String text)
    {
        boolean noMacros = false;
        do
        {
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
