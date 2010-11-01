/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.softenido.cafe.io.virtual;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;

/**
 *
 * @author franci
 */
class CachedZipVirtualFileSystem extends ZipVirtualFileSystem
{
    private final long length;
    private final boolean directory;
    
    public CachedZipVirtualFileSystem(String[] paths,String path, ArchiveEntry entry)
    {
        super(paths,path);
        this.length = entry.getSize();
        this.directory = entry.isDirectory();
    }

    @Override
    public long length()
    {
        return length;
    }

    @Override
    public boolean canRead()
    {
        return true;
    }

    @Override
    public boolean canWrite()
    {
        return false;
    }

    @Override
    public boolean delete()
    {
        return false;
    }

    @Override
    public boolean exists()
    {
        return true;
    }

    @Override
    public boolean isDirectory()
    {
        return directory;
    }

    @Override
    public boolean isFile()
    {
        return !directory;
    }
}
