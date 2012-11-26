/*
 * WebActivity.java
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
import android.view.MotionEvent;
import android.view.View;
import android.webkit.*;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.softenido.cafedroid.R;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 17/11/12
 * Time: 23:54
 * To change this template use File | Settings | File Templates.
 */
public class WebActivity extends Activity
{
    WebConfig config = WebConfig.getSimpleInstance();

    private WebView wv;
    private String url;
    LinearLayout buttons;
    private View zoom;
    private ImageButton back;
    private ImageButton forward;
    private ImageButton home;
    private EditText searchEdit;
    private ImageButton searchGo;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(config.layout);

        Intent intent = getIntent();

        url = (intent != null) ? intent.getDataString() : null;
        wv = (WebView) findViewById(R.id.web_view);
        wv.loadUrl(url);

        if(config.hasBuiltIn())
        {
            wv.getSettings().setBuiltInZoomControls(true);
        }
        buttons = (LinearLayout) findViewById(R.id.web_buttons_layout);

        if( config.hasBar() || config.hasZoom())
        {
            buttons.setVisibility(View.VISIBLE);
            if(config.hasBack())
            {
                back = (ImageButton)findViewById(R.id.web_back);
                back.setOnClickListener(backOnClickListener);
                back.setVisibility(View.VISIBLE);
            }
            else
            {
                back.setVisibility(View.GONE);
            }
            if(config.hasForward())
            {
                forward = (ImageButton)findViewById(R.id.web_forward);
                forward.setOnClickListener(forwardOnClickListener);
                forward.setVisibility(View.VISIBLE);
            }
            else
            {
                forward.setVisibility(View.GONE);
            }
            if(config.hasHome())
            {
                home = (ImageButton)findViewById(R.id.web_home);
                home.setOnClickListener(homeOnClickListener);
                home.setVisibility(View.VISIBLE);
            }
            else
            {
                home.setVisibility(View.GONE);
            }
            if(config.hasSearch())
            {
                searchEdit = (EditText)findViewById(R.id.web_search_edit);
                searchEdit.setOnKeyListener(searchEditOnKeyListener);
                searchEdit.setVisibility(View.VISIBLE);

                searchGo = (ImageButton)findViewById(R.id.web_search_go);
                searchGo.setOnClickListener(searchGoOnClickListener);
                searchGo.setVisibility(View.VISIBLE);
            }
            else
            {
                searchEdit.setVisibility(View.GONE);
                searchGo.setVisibility(View.GONE);
            }
            if(config.hasZoomCtrl())
            {
                zoom = wv.getZoomControls();
                buttons.addView(zoom);
                zoom.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            buttons.setVisibility(View.GONE);
        }
        if(this.config.hasWideView())
        {
            wv.getSettings().setUseWideViewPort(true);
        }
    }

    static void startUrl(Activity parent, String url, Class<?> cls)
    {
        final Intent intent = new Intent(parent, cls);
        Uri uri = Uri.parse(url);
        intent.setData(uri);
        parent.startActivityForResult(intent, 0);
    }

    public static void startUrl(Activity parent, String url)
    {
        startUrl(parent, url, WebActivity.class);
    }

    public void setWebChromeClient(WebChromeClient client)
    {
        wv.setWebChromeClient(client);
    }

    public void setWebViewClient(WebViewClient client)
    {
        wv.setWebViewClient(client);
    }

    private View.OnClickListener backOnClickListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            onBack();
        }
    };
    private View.OnClickListener forwardOnClickListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            onForward();
        }
    };
    private View.OnClickListener homeOnClickListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            onHome();
        }
    };
    private View.OnKeyListener searchEditOnKeyListener = new View.OnKeyListener()
    {
        public boolean onKey(View v, int keyCode, KeyEvent event)
        {
            if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_UP)
            {
                onSearch(searchEdit.getText().toString());
                return true;
            }
            return false;
        }
    };
    private View.OnClickListener searchGoOnClickListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            onSearch(searchEdit.getText().toString());
        }
    };

    protected void onBack()
    {
        wv.goBack();
    }
    protected void onForward()
    {
        wv.goForward();
    }
    protected void onHome()
    {
        wv.loadUrl(url);
    }
    String lastSearch = null;
    protected void onSearch(String text)
    {
        if(text.equals(lastSearch))
        {
            wv.findNext(true);
        }
        else if(wv.findAll(text)>0)
        {
            lastSearch = text;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        //make zoom controls visible when is invisible and webview is moved
        if(zoom!=null && MotionEvent.ACTION_MOVE==event.getAction() && zoom.getVisibility()!=View.VISIBLE)
        {
            zoom.setVisibility(View.VISIBLE);
        }
        return super.onTouchEvent(event);
    }
}
