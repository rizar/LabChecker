package unrar;

public class UnRarExtractException extends UnRarException
{
  public UnRarExtractException() {super(RussianStrings.UNRAREXTRACTEEX);}
  public UnRarExtractException(String msg) {super(msg);}
}
