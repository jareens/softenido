/*
 *  MailToBuilder.java
 *
 *  Copyright (C) 2010-2011  Francisco Gómez Carrasco
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
package com.softenido.cafecore.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 *
 * @author franci
 */
public class MailToBuilder
{
    private final String sep;

    public MailToBuilder()
    {
        this.sep = ",";
    }

    public MailToBuilder(String sep)
    {
        this.sep = sep;
    }
    
    public String build(List<String> to,List<String> cc,List<String> bcc, String subject, String body)
    {
        StringBuilder mailto = new StringBuilder();

        mailto.append("mailto:").append(formatAddress(to)).append("?");
        if(cc!=null && !cc.isEmpty())
        {
            mailto.append("cc=").append(formatAddress(cc)).append("&");
        }
        if(bcc!=null && !bcc.isEmpty())
        {
            mailto.append("bcc=").append(formatAddress(bcc)).append("&");
        }
        mailto.append("subject=").append(urlEncode(subject)).append("&");
        mailto.append("body=").append(urlEncode(body));
        return mailto.toString();
    }
    public String build(List<String> to,String subject, String body)
    {
        return build(to,null,null,subject,body);
    }
    private static String urlEncode(String str)
    {
        try
        {
            return URLEncoder.encode(str, "UTF-8").replace("+", "%20");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
    }

    String formatAddress(Iterable<String> address)
    {
        StringBuilder url = new StringBuilder();
        boolean first=true;
        for(String item : address)
        {
            if(first)
            {
                first = false;
            }
            else
            {
                url.append(sep);
            }
            url.append(item);
        }
        return url.toString();
    }
}
