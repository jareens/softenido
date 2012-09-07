package com.softenido.droiddesk.util.ui;

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
