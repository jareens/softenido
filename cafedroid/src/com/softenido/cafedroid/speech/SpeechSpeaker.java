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

package com.softenido.cafedroid.speech;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import com.softenido.cafecore.util.RepeatedMap;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: franci
 * Date: 27/11/11
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */
public class SpeechSpeaker implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener
{
    public static final int FLAG_ALLOW_RINGER_MODES = AudioManager.FLAG_ALLOW_RINGER_MODES;
    public static final int FLAG_PLAY_SOUND = AudioManager.FLAG_PLAY_SOUND;
    public static final int FLAG_REMOVE_SOUND_AND_VIBRATE = AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE;
    public static final int FLAG_SHOW_UI = AudioManager.FLAG_SHOW_UI;
    public static final int FLAG_VIBRATE = AudioManager.FLAG_VIBRATE;

    public static final int ADJUST_LOWER = AudioManager.ADJUST_LOWER;
    public static final int ADJUST_RAISE = AudioManager.ADJUST_RAISE;
    public static final int ADJUST_SAME = AudioManager.ADJUST_SAME;

    final static String tag = SpeechSpeaker.class.getSimpleName();
    final String SILENCE="[SILENCE]=";
    final Activity activity;
    final Locale locale;
    final SpeechManager manager;

    volatile TextToSpeech tts;
    volatile boolean InstallNeeded =false;
    private boolean success;
    private final Object lock = new Object();

    private static enum Status
    {
        IDLE, PLAYING, PAUSED, STOPPED;
    }
    private volatile Status status = Status.IDLE;

    public static final int LANG_AVAILABLE = TextToSpeech.LANG_AVAILABLE;
    public static final int LANG_COUNTRY_AVAILABLE = TextToSpeech.LANG_COUNTRY_AVAILABLE;
    public static final int LANG_COUNTRY_VAR_AVAILABLE = TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE;

    static interface OnSpeakingListener
    {
        void onSpeaking();
    }
    private final List<OnSpeakingListener> listener = Collections.synchronizedList(new ArrayList<OnSpeakingListener>());

    SpeechSpeaker(SpeechManager manager, Activity activity, Locale locale)
    {
        this.manager = manager;
        this.activity = activity;
        this.locale   = locale;
    }

    public boolean start()
    {
        synchronized (lock)
        {
            if(tts==null)
            {
                Intent checkIntent = new Intent();
                checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
                activity.startActivityForResult(checkIntent, SpeechManager.DATA_CHECK_CODE);
                return true;
            }
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
                manager.onSpeekerInstallNeeded();
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
            //using API 8 version, use setOnUtteranceProgressListener on API 15
            int rc = tts.setOnUtteranceCompletedListener(this);
            Log.e("setOnUtteranceCompletedListener", "setOnUtteranceCompletedListener="+rc);
            tts.setOnUtteranceCompletedListener(this);
        }
        else if (status == TextToSpeech.ERROR)
        {
            success=false;
            shutdown();
        }
        manager.onSpeekerInitied(success);
    }

    public boolean isSpeaking()
    {
        return tts.isSpeaking();
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
    public Locale[] getAvailableLocales()
    {
        return getAvailableLocales(Locale.getAvailableLocales());
    }

    public Locale[] getAvailableLocales(Locale[] locales)
    {
        locales = locales.clone();

        HashSet<String> iso3 = new HashSet<String>();

        RepeatedMap<String,Locale> var = new RepeatedMap<String,Locale>();
        RepeatedMap<String,Locale> cou = new RepeatedMap<String,Locale>();
        RepeatedMap<String,Locale> lan = new RepeatedMap<String,Locale>();

        for(int i=0;i<locales.length;i++)
        {
            Locale loc = locales[i];
            String iso=loc.getISO3Language();

            int success = isLanguageAvailable(loc);

            if(success==LANG_COUNTRY_VAR_AVAILABLE && loc.getVariant().length()>0)
            {
                var.put(iso, loc);
                iso3.add(iso);
            }
            else if(success==LANG_COUNTRY_AVAILABLE && loc.getCountry().length()>0 && loc.getVariant().length()==0)
            {
                cou.put(iso, loc);
                iso3.add(iso);
            }
            else if(success==LANG_AVAILABLE && loc.getCountry().length()==0 && loc.getVariant().length()==0)
            {
                lan.put(iso, loc);
                iso3.add(iso);
            }
            else
            {
                locales[i] = null;
            }
        }

        ArrayList<Locale> available = new ArrayList<Locale>(locales.length);
        for(String iso : iso3)
        {
            Locale[] vv = lan.get(new Locale[0], iso);
            if(vv!=null && vv.length>1)
            {
                available.addAll(Arrays.asList(vv));
                continue;
            }
            Locale[] cc = cou.get(new Locale[0], iso);
            if(cc!=null && cc.length>1)
            {
                available.addAll(Arrays.asList(cc));
                continue;
            }
            Locale[] ll = lan.get(new Locale[0], iso);
            if(ll!=null && ll.length>1)
            {
                available.addAll(Arrays.asList(ll));
                continue;
            }

            if(ll!=null && ll.length==1)
            {
                available.addAll(Arrays.asList(ll));
                continue;
            }
            if(cc!=null && cc.length>1)
            {
                available.addAll(Arrays.asList(cc));
                continue;
            }
            if(vv!=null && vv.length>1)
            {
                available.addAll(Arrays.asList(vv));
                continue;
            }
        }

        return available.toArray(new Locale[0]);
    }

    public boolean isInstallNeeded()
    {
        return InstallNeeded;
    }

    public String silence(long millis, boolean flush, boolean wait)
    {
        return speak(SILENCE+millis,flush,wait);
    }

    public void stop()
    {
        synchronized (lock)
        {
            status = Status.STOPPED;
            if(tts!=null) tts.stop();
            lock.notifyAll();
        }
    }

    public void pause()
    {
        synchronized (lock)
        {
            status = Status.PAUSED;
            if(tts!=null) tts.stop();
        }
    }
    public void resume()
    {
        synchronized (lock)
        {
            status = Status.IDLE;
            lock.notifyAll();
        }
    }

    public void shutdown()
    {
        synchronized (lock)
        {
            if(tts!=null)
            {
                tts.shutdown();
                tts=null;
            }
        }
    }

    private String utteranceLock=null;
    private AtomicInteger count = new AtomicInteger(0);

    public String speak(String text, boolean flush, boolean wait)
    {
        status = Status.PLAYING;
        return speak(text, flush, wait, Integer.toString(count.incrementAndGet()));
    }
    String speak(final String text, final boolean flush, final boolean wait, final String utterance)
    {
        synchronized (lock)
        {
            if(tts==null)
            {
                return null;
            }
            //build map to pass utterance id
            HashMap<String, String> params = new HashMap<String, String>(1);
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utterance);

            //save utterance for completed event
            utteranceLock = utterance;
            int ret;
            if(text.startsWith(SILENCE))
            {
                long millis = Long.parseLong(text.substring(SILENCE.length()));
                ret = tts.playSilence(millis, flush?TextToSpeech.QUEUE_FLUSH:TextToSpeech.QUEUE_ADD, params);
            }
            else
            {
                ret = tts.speak(text, flush?TextToSpeech.QUEUE_FLUSH:TextToSpeech.QUEUE_ADD, params);
            }
            if(Log.isLoggable(SpeechPlayer.class.getSimpleName(), Log.DEBUG))
            {
                Log.d(SpeechPlayer.class.getSimpleName(),"speak: "+text);
            }
            for(OnSpeakingListener item:listener)
            {
                item.onSpeaking();
            }
            if(ret!=TextToSpeech.SUCCESS || status==Status.PAUSED || status==Status.STOPPED)
            {
                return null;
            }
            try
            {
                if(wait)
                {
                    Log.d("utterance","utterance="+utterance+" lock.wait()");
                    lock.wait();
                }
            }
            catch(InterruptedException ex)
            {
                Logger.getLogger(SpeechSpeaker.class.getName()).log(Level.SEVERE,"Exception waitting utterance {0}",utterance);
            }
            return utterance;
        }
    }

    public void onUtteranceCompleted(String utterance)
    {
        synchronized (lock)
        {
            if(utterance.equals(this.utteranceLock))
            {
                Log.d("utterance","utterance="+utterance+" lock.notifyAll()");
                lock.notifyAll();
            }
            else
            {
                Logger.getLogger(SpeechSpeaker.class.getName()).log(Level.SEVERE,"onUtteranceCompleted waiting for utterance "+this.utteranceLock+" found "+utterance);
            }
            manager.onUtteranceCompleted(utterance);
        }
    }

    private final Object playLock = new Object();
    public String[] speak(final String[] text, final boolean flush, boolean wait)
    {
        status = Status.PLAYING;
        // create utterances id for text
        final String utterances[] = new String[text.length];
        for(int i=0;i<text.length;i++)
        {
            utterances[i]= Integer.toString(count.incrementAndGet());
        }

        Runnable task = new Runnable()
        {
            public void run()
            {
                synchronized(playLock)
                {
                    synchronized(lock)
                    {
                        for(int i = 0; i < text.length && status!=Status.STOPPED && tts!=null; i++)
                        {
                            do
                            {
                                switch (status)
                                {
                                    case STOPPED:
                                        break;
                                    case PAUSED:
                                        try
                                        {
                                            lock.wait();
                                        }
                                        catch(InterruptedException e)
                                        {
                                            Log.e(SpeechSpeaker.class.getName(), "Exception in PAUSED status");
                                        }
                                    case IDLE:
                                        status=Status.PLAYING;
                                    case PLAYING:
                                        speak(text[i], i==0?flush:false, true, utterances[i] );
                                        break;
                                    default:
                                        Log.w(SpeechSpeaker.class.getName(),"Unknown status "+status);
                                        status=Status.IDLE;
                                        break;
                                }
                            }
                            while( status==Status.PAUSED);
                        }
                        status = Status.IDLE;
                    }
                }
            }

        };

        if(wait)
        {
            task.run();
        }
        else
        {
            new Thread(task).start();
        }

        return utterances;
    }
    public void registerOnSpeakingListener(OnSpeakingListener listener)
    {
        this.listener.add(listener);
    }
    public void unregisterOnSpeakingListener(OnSpeakingListener listener)
    {
        this.listener.remove(listener);
    }

    private volatile AudioManager audioManager = null;
    private AudioManager getAudioManager()
    {
        if(audioManager==null)
        {
            audioManager=(AudioManager)activity.getSystemService(Context.AUDIO_SERVICE);
        }
        return audioManager;
    }
    public int getVolume()
    {
        AudioManager am = getAudioManager();
        return am.getStreamVolume(am.STREAM_MUSIC);
    }
    public int getMaxVolume()
    {
        AudioManager am = getAudioManager();
        return am.getStreamMaxVolume(am.STREAM_MUSIC);
    }

    public void setVolume(int volume)
    {
        setVolume(volume, 0);
    }
    public void setVolume(int volume, int flags)
    {
        AudioManager am = getAudioManager();
        am.setStreamVolume(am.STREAM_MUSIC, volume, flags);
        Log.d(tag, "setVolume(" + volume + ")");
    }
    public void adjustVolume(int direction)
    {
        adjustVolume(direction, 0);
    }
    public void adjustVolume(int direction, int flags)
    {
        AudioManager am = getAudioManager();
        am.adjustStreamVolume(am.STREAM_MUSIC, direction, flags);
        Log.d(tag, "adjustVolume(" + direction + ")");
    }
    public int getVolumePercentage()
    {
        int value = getVolume();
        int max = getMaxVolume();
        return Math.round(value*100f/max);
    }
    public void setVolumePercentage(int volume)
    {
        setVolumePercentage(volume, 0);
    }
    public void setVolumePercentage(int volume, int flags)
    {
        int max = getMaxVolume();
        int value = Math.round(volume*max/100f);
        setVolume(value, flags);

        if(Log.isLoggable(tag, Log.DEBUG))
        {
            Log.d(tag,"setVolumePercentage("+volume+"%) ["+value+"/"+max+")");
        }
    }

    public boolean areDefaultsEnforced()
    {
        return tts.areDefaultsEnforced();
    }

    public String getDefaultEngine()
    {
        return tts.getDefaultEngine();
    }

    public List<TextToSpeech.EngineInfo> getEngines()
    {
        return tts.getEngines();
    }
}
