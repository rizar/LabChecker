package loadimg;

import unrar.RussianStrings;

public class LoadImgException extends Exception
{
  public LoadImgException() {super(RussianStrings.LOADIMGEX);}
  public LoadImgException(String msg) {super(msg);}
}
