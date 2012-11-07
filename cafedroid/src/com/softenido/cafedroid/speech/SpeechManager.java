/*
 * SpeechBuilder.java
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

package com.softenido.cafedroid.speech;

import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: franci
 * Date: 26/11/11
 * Time: 21:38
 * To change this template use File | Settings | File Templates.
 */
public class SpeechManager
{
    final static int DATA_CHECK_CODE = 1234;
    final static int RECOGNIZE_SPEECH= 1235;

    final Activity activity;
    final Locale locale;
    final SpeechSpeaker speaker;
    final SpeechHearer hearer;

//    private SpeechManager(Activity activity, Locale lang, SpeechSpeaker speaker, SpeechHearer hearer)
//    {
//        this.activity = activity;
//        this.lang = lang;
//        this.speaker = speaker;
//        this.hearer = hearer;
//    }

    public SpeechManager(Activity activity, Locale locale)
    {
        this.activity= activity;
        this.locale = locale;
        speaker = new SpeechSpeaker(this, activity, locale);
        hearer  = new SpeechHearer(this, activity, locale);
    }
    public SpeechManager(Activity activity)
    {
        this(activity, Locale.getDefault());
    }

    public SpeechSpeaker getSpeaker()
    {
        return speaker;
    }
    public SpeechHearer getHearer()
    {
        return hearer;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case DATA_CHECK_CODE:
                speaker.onActivityResult(requestCode, resultCode, data);
                break;
            case RECOGNIZE_SPEECH:
                hearer.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    protected void finalize() throws Throwable
    {
        shutdown();
        super.finalize();    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void onSpeekerInstallNeeded()
    {
        //does nothing, for overriding
    }

    protected void onSpeekerInitied(boolean success)
    {
        //does nothing, for overriding
    }
    public void onUtteranceCompleted(String utterance)
    {
        //does nothing, for overriding
    }

    public void onHearerRecognized(ArrayList<String> text)
    {
        //does nothing, for overriding
    }

    public void shutdown()
    {
        speaker.shutdown();
    }
}
