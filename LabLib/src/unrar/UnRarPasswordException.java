package unrar;

public class UnRarPasswordException extends UnRarException
{
  public UnRarPasswordException() {super(RussianStrings.UNRARPASSWORDEX);}
  public UnRarPasswordException(String msg) {super(msg);}
}
