package scaleimage;

import com.softenido.cafe.imageio.FixedRatiosScaleDimension;
import com.softenido.cafe.imageio.ScaleImage;
import java.io.File;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author franci
 */
public class Gray
{
    public static void main(String[] args) throws Exception
    {

        File img = new File(args[0]).getAbsoluteFile();
        File parent = img.getParentFile();
        File img2 = new File(parent,"gray-"+img.getName());
        String fmt = ScaleImage.getFormat(img);
        new ScaleImage(new FixedRatiosScaleDimension(640, 60) ,true, true,true).filter(img, img2, fmt);
    }
}

