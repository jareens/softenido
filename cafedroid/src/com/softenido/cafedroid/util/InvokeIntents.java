/*
 * Intents.java
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

package com.softenido.cafedroid.util;

import android.app.Activity;
import android.app.SearchableInfo;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 31/10/12
 * Time: 15:00
 * To change this template use File | Settings | File Templates.
 */
public class InvokeIntents
{
    static enum SearchEngine
    {
        GOOGLE, BING, ASK, DUCKDUCKGO, YAHOO;
    }
    public static void getContent(Activity activity, String mimeType, String chooserTitle, int requestCode, boolean opennable)
    {
        final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(mimeType);
        if(opennable)
        {
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        Intent chooser = Intent.createChooser(intent, chooserTitle);
        activity.startActivityForResult(chooser, requestCode);
    }
    public static void getContent(Activity activity, String mimeType, int chooserTitleResId, int requestCode, boolean opennable)
    {
        String chooserTitle = activity.getString(chooserTitleResId, mimeType);
        getContent(activity, mimeType, chooserTitle, requestCode, opennable);
    }
    public static void getContent(Activity activity, String mimeType, String title, int requestCode)
    {
        getContent(activity, mimeType, title, requestCode, false);
    }
    public static void getContent(Activity activity, String mimeType, int titleResId, int requestCode)
    {
        getContent(activity, mimeType, titleResId, requestCode, false);
    }
    public static void send(Activity activity, String mimeType, String chooserTitle, String subject, String text)
    {
        Intent send = new Intent(Intent.ACTION_SEND);
        send.setType(mimeType);
        send.putExtra(Intent.EXTRA_SUBJECT, subject);
        send.putExtra(Intent.EXTRA_TEXT, text);
        Intent chooser = Intent.createChooser(send, chooserTitle);
        activity.startActivityForResult(chooser, 0);
    }
    public static void send(Activity activity, String mimeType, int chooserTitleResId, String subject, String text)
    {
        String chooserTitle = activity.getString(chooserTitleResId, mimeType);
        send(activity, mimeType, chooserTitle, subject, text);
    }


//    public static void invokeWebBrowser(Activity activity, String httpURL)
//    {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse(httpURL));
//        activity.startActivity(intent);
//    }
//    public static void invokeWebSearch(Activity activity, SearchEngine engine)
//    {
//        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//        String url;
//        switch (engine)
//        {
//            case BING:
//                url = "http://www.bing.com";
//                break;
//            case ASK:
//                url = "http://www.ask.com";
//                break;
//            case DUCKDUCKGO:
//                url = "http://www.duckduckgo.com";
//                break;
//            case YAHOO:
//                url = "http://search.yahoo.com";
//                break;
//            case GOOGLE:
//            default:
//                url = "http://www.google.com";
//                break;
//        }
//        intent.setData(Uri.parse(url));
//        activity.startActivity(intent);
//    }
//
//    public static void dial(Activity activity)
//    {
//        Intent intent = new Intent(Intent.ACTION_DIAL);
//        activity.startActivity(intent);
//    }
//
//    public static void call(Activity activity, String telUrl)
//    {
//        Intent intent = new Intent(Intent.ACTION_CALL);
//        if(!telUrl.trim().toLowerCase().startsWith("tel:"))
//        {
//            telUrl = "tel:"+telUrl;
//        }
//        //tel:904-905-5646
//        intent.setData(Uri.parse(telUrl));
//        activity.startActivity(intent);
//    }
//    public static void showMapAtLatLong(Activity activity)
//    {
//        showMapAtLatLong(activity, null);
//    }
//    public static void showMapAtLatLong(Activity activity, String url)
//    {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        //geo:lat,long?z=zoomlevel&q=question-string
//        //"geo:0,0?z=4&q=business+near+city"
//        if(url!=null)
//        {
//            intent.setData(Uri.parse(url));
//        }
//        activity.startActivity(intent);
//    }

}
