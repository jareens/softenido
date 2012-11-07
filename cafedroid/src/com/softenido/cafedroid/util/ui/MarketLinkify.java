/*
 * MarketLinkify.java
 *
 * Copyright (c) 2012 Francisco Gómez Carrasco
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

package com.softenido.cafedroid.util.ui;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 13/07/12
 * Time: 18:56
 * To change this template use File | Settings | File Templates.
 */
public class MarketLinkify
{
    public final static void addLinks(TextView text)
    {
        CharSequence cs = text.getText();

        Spannable spannable;
        if(cs instanceof Spannable)
        {
            spannable = (Spannable)cs;
        }
        else
        {
            spannable = SpannableString.valueOf(cs);
            text.setText(spannable);
        }
        URLSpan span = new URLSpan(spannable.toString());
        spannable.setSpan(span, 0, spannable.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setMovementMethod(LinkMovementMethod.getInstance());

    }

}
