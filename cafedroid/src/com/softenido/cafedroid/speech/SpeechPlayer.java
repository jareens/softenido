/*
 * SpeechPlayer.java
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

package com.softenido.cafedroid.speech;

import android.media.AudioManager;
import android.os.PowerManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import com.softenido.cafecore.gauge.GaugeProgress;
import com.softenido.cafecore.gauge.GaugeView;
import com.softenido.cafecore.gauge.ProxyGaugeProgress;
import com.softenido.cafecore.math.FastMath;
import com.softenido.cafecore.statistics.classifier.LanguageClassifier;
import com.softenido.cafecore.statistics.classifier.Score;
import com.softenido.cafecore.text.Phrases;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 3/09/12
 * Time: 15:43
 * To change this template use File | Settings | File Templates.
 */
public class SpeechPlayer implements SpeechSpeaker.OnSpeakingListener
{
    public static final int FLAG_ALLOW_RINGER_MODES = AudioManager.FLAG_ALLOW_RINGER_MODES;
    public static final int FLAG_PLAY_SOUND = AudioManager.FLAG_PLAY_SOUND;
    public static final int FLAG_REMOVE_SOUND_AND_VIBRATE = AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE;
    public static final int FLAG_SHOW_UI = AudioManager.FLAG_SHOW_UI;
    public static final int FLAG_VIBRATE = AudioManager.FLAG_VIBRATE;

    public static final int ADJUST_LOWER = AudioManager.ADJUST_LOWER;
    public static final int ADJUST_RAISE = AudioManager.ADJUST_RAISE;
    public static final int ADJUST_SAME = AudioManager.ADJUST_SAME;

    private final Object lock = new Object();
    private volatile boolean classified = false;

    private static enum Status
    {
        INIT, STOP, PLAY, PAUSE, PREV_ROW, PREV_COL, NEXT_ROW, NEXT_COL, EXIT;
    }
    private volatile Status status = Status.INIT;

    private final SpeechSpeaker speaker;
    private final LanguageClassifier classifier;
    private final String[] paragraphs;
    private final String[][] phrases;
    private final String[][] langs;
    private final boolean [][] empty;
    private final int[][] progress;
    private final Locale locale;
    private volatile int progressMax;

    private volatile int row=0;
    private volatile int col=0;
    private volatile Locale loc = null;
    private volatile String utterance =null;

    private volatile boolean lowerCase = false;
    private volatile boolean detection = true;
    private volatile String ignoreRegex = null;
    private volatile String alternative = null;
    private volatile ProxyGaugeProgress gauge = new ProxyGaugeProgress();
    private volatile PowerManager.WakeLock wakeLock = null;
    private volatile boolean ignoreFirst=false;
    private volatile int minPhraseSize = 16;

    public static interface OnStatusChangedListener
    {
        void onStatusPlay();
        void onStatusPlaying(int row, int col, Locale loc, String utterance);
        void onStatusStop();
        void onStatusPause();
    }
    private final List<OnStatusChangedListener> listener = Collections.synchronizedList(new ArrayList<OnStatusChangedListener>());

    public SpeechPlayer(SpeechSpeaker speaker, LanguageClassifier classifier, List<String> paragraphs, Locale locale)
    {
        this(speaker, classifier, paragraphs.toArray(new String[0]), locale);
    }
    public SpeechPlayer(SpeechSpeaker speaker, LanguageClassifier classifier, String[] paragraphs, Locale locale)
    {
        this.speaker = speaker;
        this.classifier = classifier;

        this.paragraphs = paragraphs.clone();
        this.phrases = new String[paragraphs.length][];
        this.langs = new String[paragraphs.length][];
        this.empty = new boolean[paragraphs.length][];
        this.progress = new int[paragraphs.length][];
        int count = 0;
        for(String p : paragraphs)
        {
            count += p.length();
        }
        this.progressMax =  count;
        this.locale = locale;
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }

    HashMap<String,Locale> localeMap = new HashMap<String,Locale>();
    public void setLocale(String language, String locale)
    {
        Log.d("SpeechPlayer", "setLocale("+language+", "+locale);
        String[] tokens = locale.split("_");
        String lan = (tokens.length>0)?tokens[0]:"";
        String cou = (tokens.length>1)?tokens[1]:"";
        String var = (tokens.length>2)?tokens[2]:"";
        localeMap.put(language,new Locale(lan,cou,var));
    }

    private Locale getLocaleFor(String lang)
    {
        Locale ret;

        if(detection)
        {
            Locale loc = (lang.length()>0)?localeMap.get(lang):locale;
            if(loc==null)
            {
                loc = locale;
            }

            switch(speaker.isLanguageAvailable(loc))
            {
                case SpeechSpeaker.LANG_AVAILABLE:
                case SpeechSpeaker.LANG_COUNTRY_AVAILABLE:
                case SpeechSpeaker.LANG_COUNTRY_VAR_AVAILABLE:
                    ret = loc;
                    break;
                default:
                    ret = locale;
                    break;
            }
        }
        else
        {
            ret = locale;
        }
        Log.d("SpeechPlayer", "getLocaleFor("+lang+")="+ret+")");
        return ret;
    }

    private void waitFor(String[][] array, int x) throws InterruptedException
    {
        synchronized(lock)
        {
            while(array[x]==null)
            {
                lock.wait(54321);
            }
        }
    }
    private void waitFor(String[][] array, int x, int y) throws InterruptedException
    {
        synchronized(lock)
        {
            while(array[x]==null || array[x][y]==null)
            {
                lock.wait(54321);
            }
        }
    }

    private final AtomicBoolean firstSplit = new AtomicBoolean();
    private void split()
    {
        if(!firstSplit.compareAndSet(false, true))
            return;
        Log.d(SpeechPlayer.class.getSimpleName(),"SpeechPlayer.split try");
        try
        {
            int counter=0;
            for(int i=0;i< phrases.length;i++)
            {
                if(phrases[i]!=null)
                    continue;
                phrases[i] = Phrases.split(paragraphs[i], this.minPhraseSize);
                langs[i] = new String[phrases[i].length];
                empty[i] = new boolean[phrases[i].length];
                progress[i] = new int[phrases[i].length];
                for(int j=0;j<progress[i].length;j++)
                {
                    progress[i][j] = counter;
                    //counter += phrases[i][j].length()+10;
                    counter += phrases[i][j].length() + FastMath.log2(phrases[i][j].length()) + 1;
                }
                this.progressMax = Math.max(this.progressMax,counter);
                synchronized(lock)
                {
                    lock.notifyAll();
                }
            }
        }
        finally
        {
            firstSplit.set(false);
            Log.d(SpeechPlayer.class.getSimpleName(),"SpeechPlayer.split finally");
        }
    }

    private final AtomicBoolean firstClassify = new AtomicBoolean();
    private void classify()
    {
        if(!firstClassify.compareAndSet(false, true))
            return;
        try
        {
            classifier.firstPass();
            for(int i=0;i<langs.length && status!=Status.EXIT;i++)
            {
                // wait for split task
                waitFor(langs, i);
                for(int j=0;j<langs[i].length && status!=Status.EXIT;j++)
                {
                    if(langs[i][j]!=null)
                        continue;
                    Score score = classifier.classify(phrases[i][j]);
                    synchronized(lock)
                    {
                        langs[i][j] = score!=null?score.getName():"";
                        lock.notifyAll();
                    }
                }
            }
            if(classifier.secondPass())
            {
                for(int i=0;i<langs.length && status!=Status.EXIT;i++)
                {
                    for(int j=0;j<langs[i].length && status!=Status.EXIT;j++)
                    {
                        Score score = classifier.classify(phrases[i][j]);
                        langs[i][j] = score!=null?score.getName():"";
                    }
                }
            }
        }
        catch (InterruptedException ex)
        {
            Log.e(SpeechPlayer.class.getSimpleName(),"error in SpeechPlayer.classify",ex);
        }
        finally
        {
            firstClassify.set(false);
            Log.d(SpeechPlayer.class.getSimpleName(),"SpeechPlayer.classify finally");
        }
    }

    private final AtomicBoolean firstSpeak = new AtomicBoolean();
    private void speak()
    {
        if(!firstSpeak.compareAndSet(false, true))
            return;

        PowerManager.WakeLock cpuLock = this.wakeLock;
        if(cpuLock!=null) this.wakeLock.acquire();
        this.speaker.registerOnSpeakingListener(this);
        try
        {
            this.start();

            this.speaker.waitForStarted(60000);
            Log.d("SpeechPlayer", "speaker.getAvailableLocales()="+Arrays.toString(this.speaker.getAvailableLocales()));
            Log.d("SpeechPlayer", "speaker.areDefaultsEnforced()="+speaker.areDefaultsEnforced());
            Log.d("SpeechPlayer", "speaker.getDefaultEngine()="+speaker.getDefaultEngine());

            boolean first = true;
            for(;row<phrases.length && status==Status.PLAY;)
            {
                // wait for split task
                waitFor(phrases, row);
                for(;col<phrases[row].length && status==Status.PLAY;)
                {
                    if(ignoreFirst && row==0)
                        break;
                    // wait for classify task
                    if(detection)
                        waitFor(langs, row, col);
                    else
                        waitFor(langs, row);

                    if(status!=Status.PLAY)
                        break;

                    if(first)
                    {
                        first=false;
                        onStatusPlay();
                    }

                    loc = getLocaleFor(langs[row][col]);

                    int ret = speaker.setLanguage(loc);
                    Log.d("SpeechPlayer", "speaker.setLanguage("+loc+")="+ret);

                    if(status!=Status.PLAY)
                        break;

                    utterance = lowerCase ? phrases[row][col].toLowerCase(loc) : phrases[row][col];
                    synchronized(lock)
                    {
                        if(ignoreRegex!=null)
                        {
                            utterance = utterance.replaceAll(ignoreRegex,alternative).trim();
                        }
                    }
                    empty[row][col] = (utterance.length()==0);

                    Log.d("SpeechPlayer", "speaker.speak("+utterance+", false, true)");

                    speaker.speak(utterance, false, true);
                    if(status!=Status.PLAY)
                        break;

                    col++;
                }

                synchronized (lock)
                {
                    switch(status)
                    {
                        case PLAY:
                            seek(false,false);
                            break;
                        case NEXT_ROW:
                            seek(false,false);
                            status=Status.PLAY;
                            break;
                        case NEXT_COL:
                            seek(true,false);
                            status=Status.PLAY;
                            break;
                        case PREV_ROW:
                            seek(false,true);
                            status=Status.PLAY;
                            break;
                        case PREV_COL:
                            seek(true,true);
                            status=Status.PLAY;
                            break;
                        case STOP:
                        case PAUSE:
                            break;
                    }
                }
            }
            if(status==Status.PAUSE)
            {
                this.onStatusPause();
            }
            else if(status!=Status.INIT)
            {
                status=Status.STOP;
                this.onStatusStop();
                row=0;
                col=0;
            }
        }
        catch (InterruptedException ex)
        {
            Log.e(SpeechPlayer.class.getSimpleName(),"error in SpeechPlayer.speak",ex);
        }
        finally
        {
            firstSpeak.set(false);
            this.speaker.unregisterOnSpeakingListener(this);
            if(cpuLock!=null) cpuLock.release();
            Log.d(SpeechPlayer.class.getSimpleName(),"SpeechPlayer.speak finally");
        }
    }

    private void seek(boolean phrase, boolean back)
    {
        if(!phrase && !back)//play or nextRow
        {
            row++;
            col=0;
        }
        else if(phrase && !back) //nextCol
        {
            col++;
        }
        else if(!phrase && back) //prevRow
        {
            row=Math.max(0,row-1);
            col=0;
            if(empty[row][col] && (row>0 || col>0))
            {
                for(int i=1;i<phrases[row].length;i++)
                    if(empty[row][i])
                        return;
                seek(phrase,back);
            }
        }
        else if (phrase && back) //prevCol
        {
            if(col>0)
            {
                col--;
            }
            else if(row>0)
            {
                row--;
                col=phrases[row].length-1;
            }
            if(empty[row][col] && (row>0 || col>0))
            {
                seek(phrase, back);
            }
        }
    }


    public boolean play()
    {
        synchronized(lock)
        {
            status = Status.PLAY;
            boolean ret = speaker.start();
            new Thread(new Runnable()
            {
                public void run()
                {
                    speak();
                }
            },"SpeechPlayer.speak").start();
            return ret;
        }
    }
    public void stop()
    {
        synchronized(lock)
        {
            status=Status.STOP;
            speaker.stop();
        }
    }
    public void pause()
    {
        synchronized(lock)
        {
            status=Status.PAUSE;
            speaker.pause();
        }
    }
    public void prev(boolean phrase)
    {
        synchronized(lock)
        {
            status=phrase?Status.PREV_COL:Status.PREV_ROW;
            speaker.pause();
        }
    }
    public void next(boolean phrase)
    {
        synchronized(lock)
        {
            status=phrase?Status.NEXT_COL:Status.NEXT_ROW;
            speaker.pause();
        }
    }

    public boolean isLowerCase()
    {
        return lowerCase;
    }

    public void setLowerCase(boolean lowerCase)
    {
        this.lowerCase = lowerCase;
    }

    public boolean isDetection()
    {
        return detection;
    }

    public void setDetection(boolean detection)
    {
        synchronized (lock)
        {
            this.detection = detection;
            lock.notifyAll();
        }
    }

    public void setIgnorable(String ignoreRegex,String alternative)
    {
        synchronized (lock)
        {
            this.ignoreRegex= ignoreRegex;
            this.alternative = alternative;
        }
    }

    public int getMinPhraseSize()
    {
        return minPhraseSize;
    }

    public void setMinPhraseSize(int minPhraseSize)
    {
        this.minPhraseSize = minPhraseSize;
    }

    // cuando va a empezar a hablar
    private void onStatusPlay()
    {
        gauge.start(this.progressMax);
        for(OnStatusChangedListener item:listener)
        {
            item.onStatusPlay();
        }
    }
    // mientras habla, cada frase (utterance), se llama desde el SpeechSpeaker
    public void onSpeaking()
    {
        gauge.setMax(this.progressMax);
        gauge.setVal(progress[row][col]);
        for(OnStatusChangedListener item:listener)
        {
            item.onStatusPlaying(row, col, loc, utterance);
        }
    }
    // cuando entra en pausa
    private void onStatusPause()
    {
        for(OnStatusChangedListener item:listener)
        {
            item.onStatusPause();
        }
    }
    // cuando se para
    private void onStatusStop()
    {
        gauge.close();
        for(OnStatusChangedListener item:listener)
        {
            item.onStatusStop();
        }
    }

    public void registerOnStatusChangedListener(OnStatusChangedListener listener)
    {
        this.listener.add(listener);
    }
    public void unregisterOnStatusChangedListener(OnStatusChangedListener listener)
    {
        this.listener.remove(listener);
    }

    public void setView(GaugeView gp)
    {
        this.gauge.setView(gp);
    }

    public void setWakeLock(PowerManager.WakeLock wakeLock)
    {
        this.wakeLock = wakeLock;
    }

    public void setIgnoreFirst(boolean ignoreFirst)
    {
        this.ignoreFirst = ignoreFirst;
    }

    public void start()
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                split();
            }
        },"SpeechPlayer.split").start();
        new Thread(new Runnable()
        {
            public void run()
            {
                classify();
            }
        },"SpeechPlayer.classify").start();
    }

    public int getVolume()
    {
        return speaker.getVolume();
    }

    public int getMaxVolume()
    {
        return speaker.getMaxVolume();
    }

    public void setVolume(int volume)
    {
        speaker.setVolume(volume);
    }

    public void setVolume(int volume, int flags)
    {
        speaker.setVolume(volume, flags);
    }

    public void adjustVolume(int direction)
    {
        speaker.adjustVolume(direction);
    }
    public void adjustVolume(int direction, int flags)
    {
        speaker.adjustVolume(direction, flags);
    }

    public void setVolumePercentage(int volume)
    {
        speaker.setVolumePercentage(volume);
    }
    public void setVolumePercentage(int volume, int flags)
    {
        speaker.setVolumePercentage(volume, flags);
    }

    public int getVolumePercentage()
    {
        return speaker.getVolumePercentage();
    }

    public void setGaugeView(GaugeView view)
    {
        this.gauge.setView(view);
    }
    public GaugeProgress getGaugeProgress()
    {
        return this.gauge;
    }
}