package com.softenido.cafe.util.zip;

import com.softenido.cafe.io.ForEachFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author franci
 */
public class FixZipInputStream extends ZipInputStream
{
    public FixZipInputStream(InputStream in)
    {
        super(in);
    }

    @Override
    public ZipEntry getNextEntry() throws IOException
    {
        IllegalArgumentException fiae = null;
        for(int i=0;i<999999;i++)
        {
            try
            {
                ZipEntry ent = super.getNextEntry();
                return ent;
            }
            catch(IllegalArgumentException iae)
            {
                Logger.getLogger(FixZipInputStream.class.getName()).log(Level.WARNING, "an entry was ignored in zip file");
                fiae = iae;
            }
            
        }
        if(fiae!=null)
            throw fiae;
        return null;
    }

}
