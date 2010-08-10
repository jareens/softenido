/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.softenido.cafe.io.packed;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveException;

/**
 *
 * @author franci
 */
interface PackedFileSystem
{
    boolean canRead();
    boolean canWrite();
    boolean delete();
    boolean exists();
    String getCanonicalPath() throws IOException;
    String getAbsolutePath() throws IOException;
    File getFile();
    InputStream getInputStream() throws IOException, ArchiveException;
    String getPath();
    long length();
}
