/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.softenido.cafe.io.packed;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
    File getFile();
    InputStream getInputStream() throws IOException;
    String getPath();
    long length();
}
