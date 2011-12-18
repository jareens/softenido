/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.softenido.cafedark.util.zip;

import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;

/**
 *
 * @author franci
 */
public class IgnoreExArchiveInputStream extends ArchiveInputStream
{

    public IgnoreExArchiveInputStream()
    {
        super();
    }
    
    @Override
    public ArchiveEntry getNextEntry() throws IOException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
