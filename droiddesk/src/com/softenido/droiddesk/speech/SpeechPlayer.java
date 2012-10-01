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

package com.softenido.droiddesk.speech;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import com.softenido.cafecore.statistics.classifier.Score;
import com.softenido.cafecore.statistics.classifier.TextClassifier;
import com.softenido.cafecore.text.Phrases;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 3/09/12
 * Time: 15:43
 * To change this template use File | Settings | File Templates.
 */
public class SpeechPlayer implements SpeechSpeaker.OnSpeakingListener
{
    private final Object lock = new Object();
    private volatile boolean classified = false;

    private static enum Status
    {
        STOP, PLAY, PAUSE, PREV_ROW, PREV_COL, NEXT_ROW, NEXT_COL;
    }
    private volatile Status status = Status.STOP;

    final SpeechSpeaker speaker;
    final TextClassifier classifier;
    final String[] paragraphs;
    final String[][] phrases;
    final String[][] langs;
    final boolean [][] empty;
    final int[][] progress;
    final int progressMax;
    final Locale locale;

    private volatile int row=0;
    private volatile int col=0;
    private volatile Locale loc = null;
    private volatile String utterance =null;

    volatile boolean lowerCase = false;
    volatile boolean detection = true;
    volatile String ignoreRegex = null;
    volatile String alternative = null;
    volatile ProgressBar progressBar = null;
    volatile Handler handler= null;

    private volatile int minPhraseSize = 64;

    public static interface OnStatusChangedListener
    {
        void onStatusPlay();
        void onStatusPlaying(int row, int col, Locale loc, String utterance);
        void onStatusStop();
        void onStatusPause();
    }
    private final List<OnStatusChangedListener> listener = Collections.synchronizedList(new ArrayList<OnStatusChangedListener>());

    public SpeechPlayer(SpeechSpeaker speaker, TextClassifier classifier, List<String> paragraphs, Locale locale)
    {
        this(speaker, classifier, paragraphs.toArray(new String[0]), locale);
    }
    public SpeechPlayer(SpeechSpeaker speaker, TextClassifier classifier, String[] paragraphs, Locale locale)
    {
        this.speaker = speaker;
        this.classifier = classifier;

        this.paragraphs = paragraphs.clone();
        this.phrases = new String[paragraphs.length][];
        this.langs= new String[paragraphs.length][];
        this.empty = new boolean[paragraphs.length][];
        this.progress = new int[paragraphs.length][];
        int counter=0;
        for(int i=0;i< phrases.length;i++)
        {
            phrases[i] = Phrases.split(paragraphs[i], this.minPhraseSize);
            langs[i] = new String[phrases[i].length];
            empty[i] = new boolean[phrases[i].length];
            progress[i] = new int[phrases[i].length];
            for(int j=0;j<progress[i].length;j++)
            {
                progress[i][j] = counter;
                counter += phrases[i][j].length()+1;
            }
        }
        progressMax = counter;
        this.locale = locale;
        this.speaker.registerOnSpeakingListener(this);
    }

    @Override
    protected void finalize() throws Throwable
    {
        this.speaker.unregisterOnSpeakingListener(this);
        super.finalize();
    }

    HashMap<String,Locale> localeMap = new HashMap<String,Locale>();
    public void setLocale(String language, String locale)
    {
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
        return ret;
    }

    private final AtomicBoolean firstClassify = new AtomicBoolean();
    private void classify()
    {
        if(!firstClassify.compareAndSet(false, true))
            return;
        try
        {
            for(int i=0;i<langs.length && status==Status.PLAY;i++)
            {
                for(int j=0;j<langs[i].length && status==Status.PLAY;j++)
                {
                    if(langs[i][j]!=null)
                    {
                        continue;
                    }
                    Score score = classifier.classify(phrases[i][j]);
                    synchronized(lock)
                    {
                        langs[i][j] = score!=null?score.getName():"";
                        lock.notifyAll();
                    }
                }
            }
        }
        finally
        {
            firstClassify.set(false);
        }
    }
    private final AtomicBoolean firstSpeak = new AtomicBoolean();
    private void speak()
    {
        if(!firstSpeak.compareAndSet(false, true))
            return;
        try
        {
            new Thread(new Runnable()
            {
                public void run()
                {
                    classify();
                }
            },"classify").start();

            boolean first = true;
            for(;row<phrases.length && status==Status.PLAY;)
            {
                for(;col<phrases[row].length && status==Status.PLAY;)
                {
                    synchronized (lock)
                    {
                        while(langs[row][col]==null && status==Status.PLAY && detection)
                        {
                            lock.wait(54321);
                        }
                    }

                    if(status!=Status.PLAY)
                        break;

                    loc = getLocaleFor(langs[row][col]);

                    speaker.setLanguage(loc);

                    if(status!=Status.PLAY)
                        break;

                    utterance = phrases[row][col];

                    if(first)
                    {
                        first=false;
                        onStatusPlay();
                    }
                    if(lowerCase)
                    {
                        utterance = utterance.toLowerCase(loc);
                    }

                    synchronized(lock)
                    {
                        if(ignoreRegex!=null)
                        {
                            utterance = utterance.replaceAll(ignoreRegex,alternative).trim();
                        }
                    }
                    empty[row][col] = (utterance.length()==0);
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
            else
            {
                status=Status.STOP;
                this.onStatusStop();
                row=0;
                col=0;
            }
        }
        catch (InterruptedException ex)
        {
            Log.e(SpeechPlayer.class.getName(),"error in SpeechPlayer.loop",ex);
        }
        finally
        {
            firstSpeak.set(false);
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
            },"speak").start();
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
        if(progressBar !=null)
        {
            handler.post(new Runnable()
            {
                public void run()
                {
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
        }
        for(OnStatusChangedListener item:listener)
        {
            item.onStatusPlay();
        }
    }
    // mientras habla, cada frase (utterance), se llama desde el SpeechSpeaker
    public void onSpeaking()
    {
        if(progressBar !=null)
        {
            handler.post(new Runnable()
            {
                public void run()
                {
                    progressBar.setProgress(progress[row][col]*100/progressMax);
                }
            });
        }
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
        if(progressBar !=null)
        {
            handler.post(new Runnable()
            {
                public void run()
                {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
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

    public void setProgressBar(ProgressBar pb, Handler handler)
    {
        synchronized (lock)
        {
            this.progressBar = pb;
            this.handler = handler;
        }
    }
}


