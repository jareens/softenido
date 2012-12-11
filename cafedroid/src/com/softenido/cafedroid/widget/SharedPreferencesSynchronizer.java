/*
 * SharedPreferencesUISynchronized.java
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

package com.softenido.cafedroid.widget;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.ToggleButton;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 9/12/12
 * Time: 1:42
 * To change this template use File | Settings | File Templates.
 */
public class SharedPreferencesSynchronizer implements SharedPreferences
{
    final SharedPreferences preferences;

    final HashMap<String,OnClickListener> listeners = new HashMap<String,OnClickListener>();

    abstract class OnClickListener implements View.OnClickListener
    {
        final static int TOGGLE_BUTTON = 1;
        final int type;
        final String name;
        final View.OnClickListener next;
        OnClickListener(int type, String name, View.OnClickListener listener)
        {
            this.type = type;
            this.name = name;
            this.next = listener;
        }
        public void onClick(View view)
        {
            if(next!=null)
            {
                next.onClick(view);
            }
        }
    }
    class ToggleButtonOnClickListener extends OnClickListener
    {
        final ToggleButton view;
        final boolean perform;

        ToggleButtonOnClickListener(String name, ToggleButton view, View.OnClickListener listener, boolean perform)
        {
            super(TOGGLE_BUTTON, name, listener);
            this.view = view;
            this.perform =perform;
        }
        public void onClick(View view)
        {
            Editor editor = preferences.edit();
            editor.putBoolean(this.name, this.view.isChecked());
            apply(editor);
            super.onClick(view);
        }
    }
    public void add(String name, ToggleButton view, View.OnClickListener listener, boolean clickOnSync)
    {
        ToggleButtonOnClickListener onclick = new ToggleButtonOnClickListener(name, view, listener, clickOnSync);
        view.setOnClickListener(onclick);
        this.listeners.put(name, onclick);
    }
    public void add(String name, ToggleButton view, View.OnClickListener listener)
    {
        add(name, view, listener, false);
    }
    public void add(String name, ToggleButton view)
    {
        add(name, view, null, false);
    }
    public void sync()
    {
        sync(false);
    }
    public void sync(boolean perform)
    {
        for(OnClickListener item: listeners.values())
        {
            switch (item.type)
            {
                case OnClickListener.TOGGLE_BUTTON:
                    boolean value = this.preferences.getBoolean(item.name, false);
                    ToggleButtonOnClickListener toggle = (ToggleButtonOnClickListener) item;
                    boolean checked = toggle.view.isChecked();
                    if(checked!=value)
                    {
                        toggle.view.setChecked(value);
                        if(perform && toggle.perform)
                        {
                            toggle.next.onClick(toggle.view);
                        }
                    }
                    break;
            }
        }
    }


    public SharedPreferencesSynchronizer(SharedPreferences preferences)
    {
        this.preferences = preferences;
    }

    public Map<String, ?> getAll()
    {
        return preferences.getAll();
    }

    public String getString(String s, String s2)
    {
        return preferences.getString(s, s2);
    }

    public Set<String> getStringSet(String s, Set<String> strings)
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getInt(String s, int i)
    {
        return preferences.getInt(s, i);
    }

    public long getLong(String s, long l)
    {
        return preferences.getLong(s, l);
    }

    public float getFloat(String s, float v)
    {
        return preferences.getFloat(s, v);
    }

    public boolean getBoolean(String s, boolean b)
    {
        return preferences.getBoolean(s, b);
    }

    public boolean contains(String s)
    {
        return preferences.contains(s);
    }

    public Editor edit()
    {
        return preferences.edit();
    }

    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener)
    {
        preferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener)
    {
        preferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    private static final Queue<Editor> queue = new ConcurrentLinkedQueue<Editor>();
    private static void apply(final Editor editor)
    {
        queue.add(editor);
        new Thread(new Runnable()
        {
            public void run()
            {
                loop();
            }
        }).start();
    }
    private static void loop()
    {
        Editor editor;
        synchronized (queue)
        {
            while( (editor=queue.poll())!=null)
            {
                editor.commit();
            }
        }
    }
}
