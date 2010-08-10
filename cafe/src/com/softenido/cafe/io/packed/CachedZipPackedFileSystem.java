/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.softenido.cafe.io.packed;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;

/**
 *
 * @author franci
 */
class CachedZipPackedFileSystem extends ZipPackedFileSystem
{
    private final long length;
    
    public CachedZipPackedFileSystem(String path,  ArchiveEntry entry)
    {
        super(path);
        this.length = entry.getSize();
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
    public InputStream getInputStream() throws IOException, ArchiveException
    {
        return super.getInputStream();
    }
}
