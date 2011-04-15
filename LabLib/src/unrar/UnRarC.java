package unrar;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class UnRarC implements UnRar
{
  public boolean isRar(File archive)
  {
    try
    {
      ProcessBuilder procBuilder = new ProcessBuilder("EEM.exe", "TestRar", archive.getCanonicalPath());  
      procBuilder.redirectErrorStream(true);
      Process process = procBuilder.start();
      InputStream stdout = process.getInputStream();
      InputStreamReader isrStdout = new InputStreamReader(stdout);
      BufferedReader brStdout = new BufferedReader(isrStdout);  
      String line = brStdout.readLine();
      process.waitFor();
      if (line == "true") return true; 
    }
    catch (IOException e)
      {return false;}
    catch (InterruptedException e)
      {return false;}    
    return false;
  }
  
  public File[] unRar(File archive, File destinationFolder) throws UnRarException,
                UnRarCanNotOpenException, UnRarExtractException,
                UnRarHeaderPasswordException, UnRarPasswordException,
                UnRarTooBigException, UnRarUnknownException
  {
    try
    {
      ProcessBuilder procBuilder = new ProcessBuilder("EEM.exe", "UnRar", archive.getCanonicalPath(), destinationFolder.getCanonicalPath());  
      procBuilder.redirectErrorStream(true);
      Process process = procBuilder.start();
      InputStream stdout = process.getInputStream();
      InputStreamReader isrStdout = new InputStreamReader(stdout);
      BufferedReader brStdout = new BufferedReader(isrStdout);
      ArrayList<String> lines = new ArrayList<String>();      
      String line = null;
      while((line = brStdout.readLine()) != null)
        lines.add(line);
      process.waitFor();
      if (lines.get(0) == "true")
      {
        File[] files = new File[lines.size() - 1]; 
        for (int i = 0; i < lines.size() - 1; i++)
          files[i] = new File(lines.get(i + 1));
        return files;
      }
      else
      {
        line = lines.get(1);
         // CanNotOpen
         // HeaderPassword
         // TooBig
         // ExtractError
         // Password
         // UnknownError
        if (line == "CanNotOpen") throw new UnRarCanNotOpenException();
        else if (line == "HeaderPassword") throw new UnRarHeaderPasswordException();
        else if (line == "TooBig") throw new UnRarTooBigException();
        else if (line == "ExtractError") throw new UnRarExtractException();
        else if (line == "Password") throw new UnRarPasswordException();
        else if (line == "UnknownError") throw new UnRarUnknownException();
        else throw new UnRarException();
      }
    }
    catch (IOException e)
      {throw new UnRarException();}
    catch (InterruptedException e)
      {throw new UnRarException();}        
  }
}
