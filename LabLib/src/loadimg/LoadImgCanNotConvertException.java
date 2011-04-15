package loadimg;

import unrar.RussianStrings;

public class LoadImgCanNotConvertException extends LoadImgException
{
  public LoadImgCanNotConvertException() {super(RussianStrings.LOADIMGCANNOTCONVERTEX);}
  public LoadImgCanNotConvertException(String msg) {super(msg);}
}