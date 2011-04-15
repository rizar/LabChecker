package loadimg;

import unrar.RussianStrings;

public class LoadImgUnknownException extends LoadImgException
{
  public LoadImgUnknownException() {super(RussianStrings.LOADIMGUNKNOWNEX);}
  public LoadImgUnknownException(String msg) {super(msg);}
}