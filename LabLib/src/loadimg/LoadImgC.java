package loadimg;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class LoadImgC implements LoadImg
{
  static private HashMap<String, BufferedImage> imgsInMem = new HashMap<String, BufferedImage>();
  
  protected void finalize() throws Throwable
  {
    imgsInMem.clear();
    super.finalize();
  }
  
  public BufferedImage getImage(File image, File tempFolder, boolean memorize) throws LoadImgException,
                      LoadImgCanNotConvertException, LoadImgCanNotLoadException,
                      LoadImgCanNotSaveException, LoadImgUnknownException
  {
    try
    {
      //System.err.println(image.exists() + " " + image.getCanonicalPath());

      if (imgsInMem.containsKey(image.getCanonicalPath()))
        return imgsInMem.get(image.getCanonicalPath());
      
      ProcessBuilder procBuilder = new ProcessBuilder("EEM.exe", "GetPic", image.getCanonicalPath(), tempFolder.getCanonicalPath());  
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
      if (lines.get(0).equals( "true"))
      {
        File imgFile = new File(lines.get(1));
        BufferedImage bufIm = ImageIO.read(imgFile); 
        imgFile.delete();
        if (bufIm == null) throw new LoadImgException();
        if (memorize) 
          imgsInMem.put(image.getCanonicalPath(), bufIm);
        return bufIm;
      }
      else
      {
        line = lines.get(1);
        //����������� ��������� ��������� ����� �� ���������� �����
         // CanNotLoad
         // CanNotConvert
         // CanNotSave
         // UnknownError
        if (line.equals("CanNotLoad")) throw new LoadImgCanNotLoadException();
        else if (line.equals("CanNotConvert")) throw new LoadImgCanNotConvertException();
        else if (line.equals("CanNotSave")) throw new LoadImgCanNotSaveException();
        else if (line.equals( "UnknownError")) throw new LoadImgUnknownException();
        else throw new LoadImgException();
      }
    }
    catch (IOException e)
      {throw new LoadImgException();}
    catch (InterruptedException e)
      {throw new LoadImgException();} 
  }
}
