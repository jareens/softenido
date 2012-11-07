/*
 * SpeechHearer.java
 *
 * Copyright (c) 2012-2012  Francisco Gómez Carrasco
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

package com.softenido.cafedroid.speech;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: franci
 * Date: 27/11/11
 * Time: 14:38
 * To change this template use File | Settings | File Templates.
 */
public class SpeechHearer
{
    final Activity activity;
    final Locale locale;
    final SpeechManager manager;
    ArrayList<String> text=null;
    volatile boolean hearing = false;

    SpeechHearer(SpeechManager manager, Activity activity, Locale locale)
    {
        this.manager = manager;
        this.activity = activity;
        this.locale = locale;
    }

    public void hear(String prompt)
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, prompt);
        activity.startActivityForResult(intent, SpeechManager.RECOGNIZE_SPEECH);
        hearing = true;
    }
    void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(resultCode)
        {
            case Activity.RESULT_OK:
                text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                break;
            default:
                text = null;
                break;

        }
        hearing = false;
        manager.onHearerRecognized(text);
    }
    public ArrayList<String> getText()
    {
        return text;
    }

    public boolean isHearing()
    {
        return hearing;
    }
}
