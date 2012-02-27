/*
 * SendEmailBuilder.java
 *
 * Copyright (c) 2012  Francisco Gómez Carrasco
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Report bugs or new features to: flikxxi@gmail.com
 */

package com.softenido.droiddesk.app;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: franci
 * Date: 23/02/12
 * Time: 20:06
 * To change this template use File | Settings | File Templates.
 */
public class SendEmailBuilder
{
    final String chooserTitle;
    final Context context;
    ArrayList<String> to = new ArrayList<String>();
    ArrayList<String> cc = new ArrayList<String>();
    ArrayList<String> bcc = new ArrayList<String>();
    String subject;
    String body;
    ArrayList<File> attach = new ArrayList<File>();
    String type = "message/rfc822";

    public SendEmailBuilder(Context context, String chooserTitle)
    {
        this.context = context;
        this.chooserTitle = chooserTitle;
    }

    public SendEmailBuilder addAttach(Collection<File> attach)
    {
        this.attach.addAll(attach);
        return this;
    }
    public SendEmailBuilder addAttach(File attach)
    {
        this.attach.add(attach);
        return this;
    }
    public SendEmailBuilder addAttach(File[] attach)
    {
        this.attach.addAll(Arrays.asList(attach));
        return this;
    }

    public SendEmailBuilder addBcc(Collection<String> bcc)
    {
        this.bcc.addAll(bcc);
        return this;
    }
    public SendEmailBuilder addBcc(String bcc)
    {
        this.bcc.add(bcc);
        return this;
    }
    public SendEmailBuilder addBcc(String[] bcc)
    {
        this.bcc.addAll(Arrays.asList(bcc));
        return this;
    }

    public SendEmailBuilder setBody(String body)
    {
        this.body = body;
        return this;
    }

    public SendEmailBuilder addCc(Collection<String> cc)
    {
        this.cc.addAll(cc);
        return this;
    }
    public SendEmailBuilder addCc(String cc)
    {
        this.cc.add(cc);
        return this;
    }
    public SendEmailBuilder addCc(String[] cc)
    {
        this.cc.addAll(Arrays.asList(cc));
        return this;
    }

    public SendEmailBuilder setSubject(String subject)
    {
        this.subject = subject;
        return this;
    }

    public SendEmailBuilder addTo(Collection<String> to)
    {
        this.to.addAll(to);
        return this;
    }
    public SendEmailBuilder addTo(String to)
    {
        this.to.add(to);
        return this;
    }
    public SendEmailBuilder addTo(String[] to)
    {
        this.to.addAll(Arrays.asList(to));
        return this;
    }
    public SendEmail build()
    {
        return new SendEmail(this);
    }

    public void setType(String type)
    {
        this.type = type;
    }
}
