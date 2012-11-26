/*
 * WebHelpActivity.java
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

package com.softenido.cafedroid.web;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.*;
import android.widget.*;
import com.softenido.cafedroid.R;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 17/11/12
 * Time: 23:54
 * To change this template use File | Settings | File Templates.
 */
public class WebHelpActivity extends WebActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        this.config = WebConfig.getHelpInstance();
        super.onCreate(savedInstanceState);
    }
    public static void startUrl(Activity parent, String url)
    {
        startUrl(parent, url, WebHelpActivity.class);
    }
}
