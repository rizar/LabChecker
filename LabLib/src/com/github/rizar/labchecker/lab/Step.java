package com.github.rizar.labchecker.lab;

import static com.github.rizar.labchecker.lab.LabPredefinedMacros.*;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Rizar
 */
class Step
{
    private Lab lab;
    private String script;
    private Pattern filePattern;

    public String getScript()
    {
        return script;
    }
    
    public Step(Lab lab, String script, String fileMacro)
    {
        this.lab = lab;
        this.script = script;
        this.filePattern = Pattern.compile(lab.getMacroProcessor().process(fileMacro));
    }

    public void load()
    {
        
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
