package unrar;

public class UnRarException extends RuntimeException
{
  public UnRarException() {super(RussianStrings.UNRAREX);}
  public UnRarException(String msg) {super(msg);}
}
