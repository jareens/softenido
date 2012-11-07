/*
 * SendEmail.java
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

package com.softenido.cafedroid.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: franci
 * Date: 23/02/12
 * Time: 20:24
 * To change this template use File | Settings | File Templates.
 */
public class SendEmail
{
    static final String[] emptyString = new String[0];
    static final File[] emptyFile= new File[0];

    final String chooserTitle;
    final Context context;
    final String type;
    final String[] to;
    final String[] cc;
    final String[] bcc;
    final String subject;
    final String body;
    final File[] attach;

    SendEmail(SendEmailBuilder builder)
    {
        this.chooserTitle = builder.chooserTitle;
        this.context = builder.context;
        this.type = builder.type;
        this.to = builder.to.toArray(emptyString);
        this.cc = builder.cc.toArray(emptyString);
        this.bcc = builder.bcc.toArray(emptyString);
        this.subject = builder.subject;
        this.body = builder.body;
        this.attach = builder.attach.toArray(emptyFile);
    }
    public void send()
    {
        Intent em = new Intent( (attach.length>1) ? Intent.ACTION_SEND_MULTIPLE : Intent.ACTION_SEND );
        em.setType(type);

        if(to!=null && to.length!=0)
        {
            em.putExtra(android.content.Intent.EXTRA_EMAIL, to);
        }
        if(cc!=null && cc.length!=0)
        {
            em.putExtra(android.content.Intent.EXTRA_CC, cc);
        }
        if(bcc!=null && cc.length!=0)
        {
            em.putExtra(android.content.Intent.EXTRA_BCC, bcc);
        }
        em.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        em.putExtra(android.content.Intent.EXTRA_TEXT, body);

        if(attach.length>1)
        {
            ArrayList<Uri> uris = new ArrayList<Uri>();
            for(int i =0;i<attach.length;i++)
            {
                uris.add(Uri.fromFile(attach[i]));
            }
            em.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris );
        }
        else if(attach.length==1)
        {
            em.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(attach[0]));
        }

        context.startActivity(Intent.createChooser(em, chooserTitle));
    }
}
