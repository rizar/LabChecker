package com.github.rizar.labchecker.lab;

/**
 *
 * @author Rizar
 */
class Step
{
    private Lab lab;
    private String script;
    private String fileMacro;

    public String getFileMacro()
    {
        return fileMacro;
    }

    public String getScript()
    {
        return script;
    }
    
    public Step(Lab lab, String script, String fileMacro)
    {
        this.lab = lab;
        this.script = script;
        this.fileMacro = fileMacro;
    }

    public void load()
    {
        
    }
}
