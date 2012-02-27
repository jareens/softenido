/*
 * SpeechSpeaker.java
 *
 * Copyright (c) 2011-2012  Francisco Gómez Carrasco
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

package com.softenido.droiddesk.speech;

import android.app.Activity;
import android.content.Intent;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: franci
 * Date: 27/11/11
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */
public class SpeechSpeaker implements TextToSpeech.OnInitListener
{
    final Activity activity;
    final Locale locale;
    final SpeechBuilder builder;

    volatile TextToSpeech tts;
    volatile boolean InstallNeeded =false;
    private boolean success;

    SpeechSpeaker(SpeechBuilder builder, Activity activity, Locale locale)
    {
        this.builder  = builder;
        this.activity = activity;
        this.locale   = locale;
    }

    public boolean start()
    {
        if(tts==null)
        {
            Intent checkIntent = new Intent();
            checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            activity.startActivityForResult(checkIntent, SpeechBuilder.DATA_CHECK_CODE);
            return true;
        }
        return false;
    }

    public void install()
    {
        Intent Intent = new Intent();
        Intent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        activity.startActivity(Intent);
    }

    void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(resultCode)
        {
            case TextToSpeech.Engine.CHECK_VOICE_DATA_PASS:
                InstallNeeded =false;
                tts = new TextToSpeech(activity, this);
                tts.setLanguage(locale);
                break;
            case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_DATA:
                InstallNeeded =true;
                builder.onSpeekerInstallNeeded();
                break;
            case TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL:
            case TextToSpeech.Engine.CHECK_VOICE_DATA_BAD_DATA:
            case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_VOLUME:
            default:
                InstallNeeded =false;
                break;
        }
    }

    public void onInit(int status)
    {
        if (status == TextToSpeech.SUCCESS)
        {
            success=true;
        }
        else if (status == TextToSpeech.ERROR)
        {
            success=false;
            shutdown();
        }
        builder.onSpeekerInitied(success);
    }

    public void shutdown()
    {
        if(tts!=null)
        {
            tts.shutdown();
            tts=null;
        }
    }

    public boolean isSpeaking()
    {
        return tts.isSpeaking();
    }

    public int stop()
    {
        return tts.stop();
    }

    public int setLanguage(Locale loc)
    {
        return tts.setLanguage(loc);
    }

    public Locale getLanguage()
    {
        return tts.getLanguage();
    }

    public int isLanguageAvailable(Locale loc)
    {
        return tts.isLanguageAvailable(loc);
    }

    public int speak(String text, boolean flush)
    {
        return tts.speak(text, flush?TextToSpeech.QUEUE_FLUSH:TextToSpeech.QUEUE_ADD, null);
    }

    public boolean isInstallNeeded()
    {
        return InstallNeeded;
    }
}
