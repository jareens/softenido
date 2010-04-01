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
