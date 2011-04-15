package loadimg;

import java.awt.image.BufferedImage;
import java.io.File;

public interface LoadImg
{
  public BufferedImage getImage(File image, File tempFolder, boolean memorize) throws LoadImgException,
                      LoadImgCanNotConvertException, LoadImgCanNotLoadException,
                      LoadImgCanNotSaveException, LoadImgUnknownException;
}
