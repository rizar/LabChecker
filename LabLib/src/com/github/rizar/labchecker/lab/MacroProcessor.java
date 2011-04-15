package com.github.rizar.labchecker.lab;

import com.github.rizar.labchecker.exceptions.EmptyMacroException;
import com.github.rizar.labchecker.exceptions.InfiniteCycleException;
import com.github.rizar.labchecker.exceptions.UndefinedMacroException;
import java.util.Collections;
import static com.github.rizar.labchecker.lab.Macros.*;
import com.github.rizar.labchecker.parser.GroupParser;
import com.github.rizar.labchecker.parser.RemainderParser;
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

    private static class Definition
    {
        private String groupMacro, remainderMacro, definition;

        public Definition(String groupMacro, String remainderMacro,
                          String definitionMacro)
        {
            this.groupMacro = groupMacro;
            this.remainderMacro = remainderMacro;
            this.definition = definitionMacro;
        }

        public Definition(String definitionMacro)
        {
            this(null, null, definitionMacro);
        }

        public boolean isFor(MacroProcessor macroProcessor)
        {
            String code = macroProcessor.getCode();
            int group = macroProcessor.getGroup();

            if (groupMacro != null)
            {
                if (!new GroupParser(macroProcessor.process(groupMacro)).
                        isFor(group))
                    return false;
            }

            if (remainderMacro != null)
            {
                if (!new RemainderParser(macroProcessor.process(
                        remainderMacro)).isFor(Lab.codeInteger(
                        code)))
                    return false;
            }

            return true;
        }

        @Override
        public String toString()
        {
            return "Definition{" + "groupMacro=" + groupMacro + "remainderMacro=" + remainderMacro + "definitionMacro=" + definition + '}';
        }
    }

    Map<String, List<Definition>> macros = new HashMap<String, List<Definition>>();

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
        //TODO 943
        String def = Integer.toString(group);
        if (def.length() == 1)
            def = "0" + def;
        registerMacro(GROUP_TWO_CHARACTERS_MACRO, def);
        char c = (char) (group < 10 ? '0' + group : 'a' + group - 10);
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

    final public void registerMacro(String macro)
    {
        List<Definition> list = new ArrayList<Definition>();
        macros.put(macro, list);
    }

    final public void registerMacro(String macro, String definition)
    {
        if (definition == null)
        {
            registerMacro(macro);
            return;
        }
        
        List<Definition> list = new ArrayList<Definition>();
        list.add(new Definition(definition));
        macros.put(macro, list);
    }

    public void addDefinition(String macro, String groupMacro,
                              String remainderMacro, String definition)
    {
        macros.get(macro).add(new Definition(groupMacro, remainderMacro,
                definition));
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<Definition>> entry : macros.entrySet())
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
                List<Definition> defs = macros.get(macro);
                for (Definition def : defs)
                    if (def.isFor(this))
                        return def.definition;
                throw new EmptyMacroException(String.format(
                        "Empty macro \"%s\"", macro));
            }
        }
        catch (NullPointerException e)
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
