package unrar;

import java.io.File;

public interface UnRar
{
  public boolean isRar(File archive);
  public File[] unRar(File archive, File destinationFolder) throws UnRarException,
                UnRarCanNotOpenException, UnRarExtractException,
                UnRarHeaderPasswordException, UnRarPasswordException,
                UnRarTooBigException, UnRarUnknownException;
}
