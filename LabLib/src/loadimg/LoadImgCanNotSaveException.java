package loadimg;

import unrar.RussianStrings;

public class LoadImgCanNotSaveException extends LoadImgException
{
  public LoadImgCanNotSaveException() {super(RussianStrings.LOADIMGCANNOTSAVEEX);}
  public LoadImgCanNotSaveException(String msg) {super(msg);}
}