/*
 *  MailTo.java
 *
 *  Copyright (C) 2012  Francisco Gómez Carrasco
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
package com.softenido.cafedesk.net;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author franci
 */
public class MailTo
{

    static final String BODY = "body";
    static final String CC = "cc";
    static final String SUBJECT = "subject";
    static final String TO = "to";
    final Map<String, String> fields = Collections.synchronizedMap(new HashMap<String, String>());

    public MailTo()
    {
    }

    public MailTo(String to, String cc, String subject, String body)
    {
        fields.put(TO, to);
        fields.put(CC, cc);
        fields.put(SUBJECT, subject);
        fields.put(BODY, body);
    }

    public MailTo to(String val)
    {
        fields.put(TO, val);
        return this;
    }

    public MailTo cc(String val)
    {
        fields.put(CC, val);
        return this;
    }

    public MailTo subject(String val)
    {
        fields.put(SUBJECT, val);
        return this;
    }

    public MailTo body(String val)
    {
        fields.put(BODY, val);
        return this;
    }

    public MailTo header(String name, String val)
    {
        fields.put(name, val);
        return this;
    }

    public String buildString()
    {
        boolean qm = false; //Question Mark '?' true=have been added, false= not yet
        StringBuilder mailto = new StringBuilder("mailto:");
        final String to = fields.get(TO);
        final String cc = fields.get(CC);
        final String subject = fields.get(SUBJECT);
        final String body = fields.get(BODY);
        if (to != null && !to.isEmpty())
        {
            mailto.append(to);
        }
        if (cc != null && !cc.isEmpty())
        {
            mailto.append("?").append(CC).append("=").append(encode(cc));
            qm = true;
        }
        if (subject != null && !subject.isEmpty())
        {
            mailto.append(qm ? "&" : "?").append(SUBJECT).append("=").append(encode(subject));
        }
        if (body != null && !body.isEmpty())
        {
            mailto.append(qm ? "&" : "?").append(BODY).append("=").append(encode(body));
        }
        for (Entry<String, String> item : fields.entrySet())
        {
            String key = item.getKey();
            String val = item.getValue();
            if (key.equals(TO) || key.equals(CC) || key.equals(SUBJECT) || key.equals(BODY))
            {
                continue;
            }
            mailto.append(qm ? "&" : "?").append(key).append("=").append(encode(val));
        }
        return mailto.toString();
    }

    public URI buildURI()
    {
        return URI.create(buildString());
    }
    
    private static final String[][] MAP= 
        {
            {"%","%25"},
            {" ","%20"},
            {"?","%3F"},
            {"&","%26"},
            {"+","%2B"}
        };
    
    static String encode(String s)
    {
        for(String[] item : MAP)
        {
            s = s.replace(item[0], item[1]);
        }
        return s;
    }
}
