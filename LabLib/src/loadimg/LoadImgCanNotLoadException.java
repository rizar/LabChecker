package loadimg;

import unrar.RussianStrings;

public class LoadImgCanNotLoadException extends LoadImgException
{
  public LoadImgCanNotLoadException() {super(RussianStrings.LOADIMGCANNOTLOADEX);}
  public LoadImgCanNotLoadException(String msg) {super(msg);}
}
