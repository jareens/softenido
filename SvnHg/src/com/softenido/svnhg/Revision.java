/*
 *  Revision.java
 *
 *  Copyright (C) 2010-2011 Francisco Gómez Carrasco
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
package com.softenido.svnhg;

import java.util.Date;

public class Revision
{

    private final int revision;
    private final String user;
    private final Date time;
    private final String comment;

    public Revision(int revision, String user, Date time, String comment)
    {
        this.revision = revision;
        this.user = user;
        this.comment = comment;
        this.time = time;
    }

    public String getComment()
    {
        return comment;
    }

    public int getRevision()
    {
        return revision;
    }

    public Date getTime()
    {
        return time;
    }

    public String getUser()
    {
        return user;
    }
}
