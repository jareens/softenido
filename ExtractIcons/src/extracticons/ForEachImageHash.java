/*
 *  ForEachImageHash.java
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
package extracticons;

import java.io.File;
import java.util.Set;
import org.fjtk.ce.Forks;
import org.fjtk.se.FileHash;

/**
 *
 * @author franci
 */
public class ForEachImageHash extends ForEachFileHash
{
    private Set<ImageHash> imageSet;
    private boolean ignoreAlpha = false;
    private int percent = 100;

    public ForEachImageHash(File file, int recursive, Set<FileHash> fileSet, Set<ImageHash> imageSet, boolean ignoreAlpha, int percent, Forks fork)
    {
        super(file, recursive, fileSet, fork);
        this.imageSet = imageSet;
        this.ignoreAlpha = ignoreAlpha;
        this.percent = percent;
    }

    @Override
    protected boolean addHash(File file)
    {
        if (!super.addHash(file))
        {
            return imageSet.add(new ImageHash(file, ignoreAlpha, percent));
        }
        return true;
    }
}
