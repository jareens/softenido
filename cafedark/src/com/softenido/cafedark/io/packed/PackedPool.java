/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.cafedark.io.packed;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

/**
 *
 * @author franci
 */
public class PackedPool
{
    private static final boolean zipOpt = true;
    public PackedPool()
    {
    }

    private InputStream getEntryInputStream(InputStream in, String entryName) throws FileNotFoundException, IOException, ArchiveException
    {
        final ArchiveInputStream ais = asf.createArchiveInputStream(new BufferedInputStream(in));
        ArchiveEntry ae = null;
        while( (ae=ais.getNextEntry())!=null)
        {
            if(entryName.equals(ae.getName()))
            {
                return ais;
            }
        }
        return null;
    }
    private InputStream getEntryInputStream(File file, String entryName) throws FileNotFoundException, IOException, ArchiveException
    {
        if( zipOpt && file.toString().toLowerCase().endsWith(".zip"))
        {
            return getZipEntryInputStream(file, entryName);
        }
        final FileInputStream fis = new FileInputStream(file);
        return new FilterInputStream(getEntryInputStream(new FileInputStream(file),entryName))
        {
            @Override
            public void close() throws IOException
            {
                fis.close();
            }
        };
    }
    private InputStream getZipEntryInputStream(File file, String entryName) throws FileNotFoundException, IOException, ArchiveException
    {
        final ZipFile ze = new ZipFile(file);
        final ZipArchiveEntry zae = ze.getEntry(entryName);
        if(zae==null)
        {
            ze.close();
            return null;
        }
        return new FilterInputStream(ze.getInputStream(zae))
        {
            @Override
            public void close() throws IOException
            {
                ze.close();
            }
        };
    }

    public InputStream get(PackedFile file) throws FileNotFoundException, IOException, ArchiveException
    {
        return get(file.splitPath());
    }
    private static final ArchiveStreamFactory asf = new ArchiveStreamFactory();


    private InputStream get(String[] paths) throws FileNotFoundException, IOException, ArchiveException
    {
        if(paths.length==0)
        {
            return null;
        }
        if(paths.length==1)
        {
            return new FileInputStream(paths[0]);
        }
        final InputStream fin = getEntryInputStream(new File(paths[0]), paths[1]);
        InputStream in = fin;
        StringBuilder curPath = new StringBuilder(paths[0]).append(paths[1]);
        try
        {
            for(int i=2;i<paths.length;i++)
            {
                curPath.append(PackedFile.pathSeparator).append(paths[i]);
                in = getEntryInputStream(in, paths[i]);
                if(in==null)
                {
                    throw new FileNotFoundException("'"+curPath+"' not found");
                }
            }
            in = new FilterInputStream(in)
            {
                @Override
                public void close() throws IOException
                {
                    super.close();
                    fin.close();
                }
            };
            return in;
        }
        catch(IOException ex)
        {
            fin.close();
            throw ex;
        }
    }
}
