/*
 *  FilenameFilterRegExp.java
 *
 *  Copyright (C) 2007  Francisco Gómez Carrasco
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
package com.softenido.cafedark.misc;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Instances of this class are used to filter filenames 
 * with a regexp (regular expresion)
 * @author franci
 */
public final class FilenameFilterRegExp implements FilenameFilter
{
  private String regexp;
  private boolean matchcase;
  
  /**
   * 
   * @param dir 
   * @param name 
   * @return 
   */
  public boolean accept(File dir,String name)
  {
    return name.toLowerCase().matches(regexp);
  }

  /**
   * 
   * @param re 
   */
  public FilenameFilterRegExp(String re)
  {
    this.regexp = re;
    this.matchcase =false;
  }
  /**
   * 
   * @param re 
   * @param mcase 
   */
  public FilenameFilterRegExp(String re,boolean mcase)
  {
    this.regexp = re;
    this.matchcase = mcase;
  }
}
