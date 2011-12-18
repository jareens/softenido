/*
 *  VirtualFileSystem.java
 *
 *  Copyright (C) 2010  Francisco Gómez Carrasco
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  Report bugs or new features to: flikxxi@gmail.com
 *
 */
package com.softenido.cafedark.io.virtual;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveException;

/**
 *
 * @author franci
 */
interface VirtualFileSystem //extends Comparable<VirtualFileSystem>
{
    static final String pathSeparator   = "!";
    static final char pathSeparatorChar = '!';

    boolean canRead();
    boolean canWrite();
    boolean delete();
    boolean exists();
    String getCanonicalPath() throws IOException;
    VirtualFileSystem getCanonicalFile() throws IOException;
    String getAbsolutePath() throws IOException;
    VirtualFileSystem getAbsoluteFile() throws IOException;
    File getBaseFile();
    InputStream getInputStream() throws IOException, ArchiveException;
    String getName();
    String getPath();
    String[] splitPath();
    boolean isHidden();
    boolean isFile();
    boolean isDirectory();
    long length();
    boolean isLink() throws IOException;
    boolean isLink(boolean path) throws IOException;
    String getLastPath();
    boolean isComplex();
    VirtualFileSystem getParentFile();
}
