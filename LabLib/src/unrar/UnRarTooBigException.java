package unrar;

public class UnRarTooBigException extends UnRarException
{
  public UnRarTooBigException() {super(RussianStrings.UNRARTOOBIGEX);}
  public UnRarTooBigException(String msg) {super(msg);}
}
