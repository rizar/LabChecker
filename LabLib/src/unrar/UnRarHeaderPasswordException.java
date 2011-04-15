package unrar;

public class UnRarHeaderPasswordException extends UnRarException
{
  public UnRarHeaderPasswordException() {super(RussianStrings.UNRARHEADERPASSWORDEX);}
  public UnRarHeaderPasswordException(String msg) {super(msg);}
}
