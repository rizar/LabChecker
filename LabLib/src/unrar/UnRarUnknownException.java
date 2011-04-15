package unrar;

public class UnRarUnknownException extends UnRarException
{
  public UnRarUnknownException() {super(RussianStrings.UNRARUNKNOWNEX);}
  public UnRarUnknownException(String msg) {super(msg);}
}
