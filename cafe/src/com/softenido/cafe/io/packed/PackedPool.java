/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.cafe.io.packed;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 *
 * @author franci
 */
public class PackedPool
{
    public PackedPool()
    {
    }

    public InputStream get(PackedFile file) throws FileNotFoundException, IOException
    {
        return get(file.splitPath());
    }
    private InputStream get(String[] paths) throws FileNotFoundException, IOException
    {
        if(paths.length==0)
        {
            return null;
        }
        if(paths.length==1)
        {
            return new FileInputStream(paths[0]);
        }

        final ZipFile zf = new ZipFile(paths[0]);
        try
        {
            String curPath = paths[0]+"!"+paths[1];
            ZipEntry ze = zf.getEntry(paths[1]);
            if(ze==null)
            {
                throw new FileNotFoundException("'"+curPath+"' not found");
            }
            
            InputStream ret = zf.getInputStream(ze);
            try
            {
                for(int i=2;i<paths.length;i++)
                {
                    curPath += PackedFile.pathSeparator+paths[i];
                    ZipInputStream zip = new ZipInputStream(ret);
                    ZipEntry ent;

                    while( (ent = zip.getNextEntry())!=null)
                    {
                        if(ent.getName().equals(paths[i]))
                        {
                            ret = zip;
                            break;
                        }
                    }
                    if(ent==null)
                    {
                        throw new FileNotFoundException("'"+curPath+"' not found");
                    }
                }
                ret = new FilterInputStream(ret)
                {
                    @Override
                    public void close() throws IOException
                    {
                        super.close();
                        zf.close();
                    }

                };
                return ret;
            }
            catch(IOException ex)
            {
                ret.close();
                throw ex;
            }
        }
        catch(IOException ex)
        {
            zf.close();
            throw ex;
        }
    }
}
