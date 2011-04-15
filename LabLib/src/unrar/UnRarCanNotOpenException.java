package unrar;

public class UnRarCanNotOpenException extends UnRarException
{
  public UnRarCanNotOpenException() {super(RussianStrings.UNRARCANNOTOPENEX);}
  public UnRarCanNotOpenException(String msg) {super(msg);}
}
