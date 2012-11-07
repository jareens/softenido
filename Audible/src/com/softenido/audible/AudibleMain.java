/*
 * AudibleMain.java
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

package com.softenido.audible;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.softenido.cafecore.gauge.DebugGauge;
import com.softenido.cafecore.io.Files;
import com.softenido.cafecore.profile.Profiler;
import com.softenido.cafecore.text.Paragraphs;
import com.softenido.cafecore.util.Locales;
import com.softenido.cafecore.util.Sorts;
import com.softenido.cafedroid.admob.AdMob;
import com.softenido.cafedroid.gauge.DroidGaugeBuilder;
import com.softenido.cafedroid.gauge.DroidGaugeView;
import com.softenido.cafedroid.logging.ToastStatusNotifier;
import com.softenido.cafedroid.os.Debug;
import com.softenido.cafedroid.os.PolicyAdmin;
import com.softenido.cafedroid.speech.SpeechManager;
import com.softenido.cafedroid.speech.SpeechPlayer;
import com.softenido.cafedroid.speech.SpeechSpeaker;
import com.softenido.cafedroid.util.InvokeIntents;
import com.softenido.cafedroid.util.logging.LogCatHandler;
import com.softenido.cafedroid.util.ui.Notifier;
import com.softenido.gutenberg.GutenbergLanguageClassifier;
import org.apache.http.protocol.HTTP;

import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.logging.Level;

public class AudibleMain extends ListActivity implements SpeechPlayer.OnStatusChangedListener
{
    private String head;
    private String body;
    private List<String> bodyParts;
    private List<String> paragraphs;

    public static final int CODE_PREFERENCES = 9876;
    public static final int CODE_PICK_TEXT_FILE = 9875;

    static enum Status
    {
        INSTALL(0), READY(1), PLAYING(2), PAUSED(3), EDIT(4);
        final int value;
        Status(int value)
        {
            this.value = value;
        }
    }

    @SuppressWarnings("FieldCanBeLocal")
    private AdMob admob=null;

    TextView title=null;
    ListView textList=null;
    EditText textEdit=null;

    DroidGaugeView gaugePlayer = null;
    DroidGaugeView gaugeClassifier=null;

    LinearLayout volumePannel = null;
    SeekBar volumeBar = null;
    TextView volumeText = null;
    Button volumeAdd = null;
    Button volumeSub = null;

    View layoutHomeExtra =null;
    ImageButton extraCopy = null;
    ImageButton extraPaste = null;
    ImageButton extraErase = null;
    ImageButton extraKeyboard = null;
    ImageButton extraFile = null;
    ImageButton extraSendto = null;

    ToggleButton toggleExtra =null;
    ToggleButton toggleVolume =null;
    ToggleButton toggleLang =null;
    ToggleButton togglePhrase =null;
    ToggleButton toggleAuto =null;

    Button setup=null;
    ImageButton prev = null;
    ImageButton next = null;
    ImageButton stop = null;
    ImageButton play = null;
    ImageButton pause = null;
    ImageButton resume = null;
    ImageButton install = null;

    Handler handler;

    private volatile Status status = Status.READY;
    private volatile SpeechManager manager = null;
    private volatile SpeechSpeaker speaker = null;
    private volatile SpeechPlayer player=null;

    volatile Notifier notifier = null;

    // settings
    volatile boolean readingLowercase =false;
    volatile boolean readingIgnoreTitle =false;
    volatile boolean readingIgnoreTitleRepeated = true;
    volatile String readingIgnore=null;
    volatile String readingAlternative=null;

    volatile boolean autoPlayExit=false;

    volatile boolean autoPlay=true;
    volatile boolean autoExit=true;
    volatile boolean autoScreenLock=false;

    volatile int langUnit=2;

    volatile Theme theme = Theme.Gray;
    volatile FontConfig font = FontConfig.NORMAL;
    volatile boolean showToasts = true;
    volatile boolean showProgress = true;
    volatile boolean earlySave = false;
    volatile boolean earlyDetect = false;
    private volatile boolean needInstallEngine=false;

    AudiblePreferences settings=null;
    GutenbergLanguageClassifier classifier = null;
    static volatile PolicyAdmin policyAdmin = null;//static because it's referenced in preferences activity

    volatile String volumePrefix = "";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        admob = AdMob.addBanner(this, R.id.mainLayout, true);

        handler = new Handler();
        this.settings = KeepAudibleLoadService.getPreferences(this.getApplicationContext());
        this.classifier= KeepAudibleLoadService.getClassifier();
        this.policyAdmin = new PolicyAdmin(this);

        boolean debuggable = Debug.isDebuggable(this);
        notifier = buildNotifier(debuggable, false);
        this.strictMode(debuggable);

        //get views
        title = (TextView) findViewById(R.id.title);
        textList = (ListView) findViewById(android.R.id.list);
        textEdit = (EditText) findViewById(R.id.body_edit);
        //extre views
        layoutHomeExtra = findViewById(R.id.layout_home_extra);
        extraCopy = (ImageButton) findViewById(R.id.extra_copy);
        extraPaste = (ImageButton) findViewById(R.id.extra_paste);
        extraErase = (ImageButton) findViewById(R.id.extra_erase);
        extraKeyboard = (ImageButton) findViewById(R.id.extra_keyboard);
        extraFile = (ImageButton) findViewById(R.id.extra_file);
        extraSendto = (ImageButton) findViewById(R.id.extra_sendto);

        //gauges
        gaugePlayer = DroidGaugeBuilder.createViewBefore(handler, this, R.id.mainLayout, R.id.layout_home_extra, DroidGaugeBuilder.Mode.BAR_TEXT, DroidGaugeBuilder.Thin.MED);
        classifier.setStatusNotifier(new ToastStatusNotifier(AudibleMain.this, handler, Level.INFO));

        //volume views
        volumePannel = (LinearLayout) findViewById(R.id.seekbar_view);
        volumeBar = (SeekBar)findViewById(R.id.seekbar_view_bar);
        volumeBar.setOnSeekBarChangeListener(volumeBarListener);
        volumeText= (TextView)findViewById(R.id.seekbar_view_text);
        volumeAdd = (Button)findViewById(R.id.seekbar_view_add);
        volumeSub = (Button)findViewById(R.id.seekbar_view_sub);
        //get components for toggle pannel
        toggleExtra = (ToggleButton)findViewById(R.id.toggle_extra);
        toggleVolume = (ToggleButton)findViewById(R.id.toggle_volume);
        toggleLang = (ToggleButton)findViewById(R.id.toggle_detect_lang);
        togglePhrase = (ToggleButton)findViewById(R.id.toggle_phrase);
        toggleAuto = (ToggleButton)findViewById(R.id.toggle_auto);
        //get components for buttons pannel
        setup= (Button) findViewById(R.id.setup);
        prev = (ImageButton) findViewById(R.id.player_prev);
        next = (ImageButton) findViewById(R.id.player_next);
        stop = (ImageButton) findViewById(R.id.player_stop);
        play = (ImageButton) findViewById(R.id.player_play);
        pause= (ImageButton) findViewById(R.id.player_pause);
        resume= (ImageButton) findViewById(R.id.player_resume);
        install= (ImageButton) findViewById(R.id.engine_install);

        //set listeners
        volumeAdd.setOnClickListener(volumeAddListener);
        volumeSub.setOnClickListener(volumeSubListener);
        extraCopy.setOnClickListener(extraCopyListener);
        extraPaste.setOnClickListener(extraPasteListener);
        extraErase.setOnClickListener(extraEraseListener);
        extraKeyboard.setOnClickListener(extraKeyboardListener);
        extraFile.setOnClickListener(extraFileListener);
        extraSendto.setOnClickListener(extraSendtoListener);

        toggleExtra.setOnClickListener(toggleExtraListener);
        toggleVolume.setOnClickListener(toggleVolumeListener);
        toggleLang.setOnClickListener(toggleLangListener);

        this.loadPreferences();
        KeepAudibleLoadService.connect(this);
        //remember toggles

        this.loadHeadAndBody();
        this.setTitleAndText(head, body);

        manager = getManager();
        speaker = manager.getSpeaker();
        speaker.start();

        this.setButtonsListeners();
        player = buildSpeechPlayer();

        volumePrefix = toggleVolume.getTextOn().toString();
        volumeBar.setMax(player.getMaxVolume());
        volumePannel.setVisibility(toggleVolume.isChecked() ? View.VISIBLE : View.GONE);
        this.paintVolume();
        layoutHomeExtra.setVisibility(View.GONE);
    }

    private SpeechPlayer buildSpeechPlayer()
    {
        SpeechPlayer player = new SpeechPlayer(speaker, classifier, paragraphs, Locale.getDefault());
        player.registerOnStatusChangedListener(this);
        PowerManager pm = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
        player.setWakeLock(pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SpeechPlayer.WakeLock"));
        String[] languages = settings.getLanguages();
        String[] locales = settings.getLocales();
        for(int i=0;i<languages.length;i++)
        {
            player.setLocale(languages[i],locales[i]);
        }
        player.setDetection(settings.getLangDetect());
        player.start();
        return player;
    }

    private void loadPreferences()
    {
        DroidGaugeBuilder.Mode mode = settings.isProgress()? DroidGaugeBuilder.Mode.BAR_TEXT: DroidGaugeBuilder.Mode.NONE;
        gaugePlayer.setMode(mode);

        this.readingLowercase = settings.getReadingLowercase();
        this.readingIgnoreTitle = settings.getReadingIgnoreTitle();
        this.readingIgnoreTitleRepeated = settings.getReadingIgnoreTitleRepeated();

        boolean parentheses = settings.getReadingIgnoreParentheses();
        boolean square = settings.getReadingIgnoreSquarebrackets();
        boolean curly = settings.getReadingIgnoreCurlybrackets();
        boolean pipe = settings.getReadingIgnorePipe();
        boolean under = settings.getReadingIgnoreUnderscore();
        boolean hyphen = settings.getReadingIgnoreHyphens();
        boolean asterisk = settings.getReadingIgnoreAsterisk();


        if(parentheses||square||curly||pipe||hyphen)
        {
            StringBuilder sb = new StringBuilder();

            if(hyphen) sb.append("(--+)");

            if(parentheses||square||curly||pipe)
            {
                if(hyphen) sb.append("|");
                sb.append("([");
                if(parentheses) sb.append("\\(\\)");
                if(square) sb.append("\\[\\]");
                if(curly) sb.append("\\{\\}");
                if(curly) sb.append("\\|");
                if(under) sb.append("_");
                if(asterisk) sb.append("*");
                sb.append("]+)");
            }

            this.readingIgnore = sb.toString();
            this.readingAlternative = " ";
        }
        else
        {
            this.readingIgnore = null;
            this.readingAlternative = null;
        }

        toggleAuto.setChecked(settings.isAuto());
        togglePhrase.setChecked(settings.isPhrase());

        toggleLang.setChecked(settings.getLangDetect());
        toggleVolume.setChecked(settings.isVolume());
        this.autoPlay = settings.isAutoPlay();
        this.autoExit = settings.isAutoExit();
        this.autoScreenLock = settings.isAutoScreenLock();
        this.langUnit      = settings.getLangUnit();
        this.theme = Theme.valueOf(settings.getTheme());
        this.font  = new FontConfig(settings.getFontTypeFace(), settings.getFontBold(), settings.getFontSize());
        this.showToasts = settings.isToasts();
        this.showProgress = settings.isProgress();
        this.earlySave = settings.isEarlySave();
        this.earlyDetect= settings.isEarlyDetect();

        this.classifier.setUnmatched(settings.getLangDefault());
        if(toggleLang.isChecked() && toggleAuto.isChecked())
        {
            classifier.setTwoPasses(earlyDetect);
            classifier.add(settings.getLanguages());

            //classifier.setLanguages(settings.getLanguages());
            if(player!=null)
            {
                player.start();
            }
        }
    }

    private void setTitleAndText(String head, String body)
    {
        this.head = head;
        this.body = body;
        if(earlySave)
        {
            settings.setTitle(head);
            settings.setBody(body);
            settings.commit();
        }

        title.setText(head);
        this.bodyParts = new ArrayList<String>();
        this.paragraphs = new ArrayList<String>();
        this.paragraphs.add(head);

        for(String item : Paragraphs.split(body,true))
        {
            bodyParts.add(item);
            paragraphs.add(item);
        }
        setListAdapter(new ArrayAdapter(this, R.layout.paragraph_view, R.id.paragraph_id, bodyParts)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextColor(theme.textColor);
                view.setBackgroundColor(theme.backGroundColor);
                view.setTypeface(font.typeface);
                view.setTextSize(font.dp, font.size);
                return view;
            }
        });
    }
    private Notifier buildNotifier(boolean debuggable, boolean longLength)
    {
        Notifier toast = settings.isToasts()?Notifier.build(this, handler, longLength): Notifier.build();
        Notifier log   = Notifier.build("Audible", Log.INFO);
        Notifier pair = Notifier.build(toast,log);
        toast.setLevel(debuggable ? Log.DEBUG : Log.INFO);
        return pair;
    }

    private SpeechManager getManager()
    {
        return new SpeechManager(this, Locale.getDefault())
        {
            @Override
            protected void onSpeekerInstallNeeded()
            {
                needInstallEngine=true;
                setStatus(Status.INSTALL);
            }
            @Override
            public void onSpeekerInitied(boolean success)
            {
                if(success)
                {
                    Locale[] locales = getSpeaker().getAvailableLocales(Locale.getAvailableLocales());
                    Arrays.sort(locales, Sorts.asStringComparatorIgnoreCase());

                    String[] langs = Locales.getDisplayLanguageCountry(locales,Locale.getDefault());

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AudibleMain.this,android.R.layout.simple_spinner_item,langs);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//                    extraLangDoc.setAdapter(adapter);
//                    extraLangPar.setAdapter(adapter);

                    setStatus(Status.READY);
                    if(autoPlayExit && toggleAuto.isChecked() && autoPlay)
                    {
                        notifier.i("auto-play");
                        speakerPlay();
                    }
                }
            }
        };
    }

    private boolean strictMode(boolean value)
    {
        // when a debugger is connected, enforce the Strict DroidGaugeMode
        //if(Debug.isDebuggerConnected())
        if(value)
        {
            boolean tp = Debug.setStrictMode(new Debug.ThreadMode(true).setDetectAll(false));
            boolean vp = Debug.setVmPolicy(new Debug.VmMode(true));
            if(tp || vp)
            {
                String msg = (tp?"StrictMode.setVmPolicy":"")+
                        (tp&&vp?"\n":"")+
                        (vp?"StrictMode.setThreadPolicy":"");
                notifier.i(msg);
            }
            Profiler.setActive(true);
            Profiler.setLineFeed(true);
            LogCatHandler.setHandler();
            return true;
        }
        return false;
    }

    private void setStatus(Status value)
    {
        this.status = value;
        switch (value)
        {
            case INSTALL:
                textList.setVisibility(View.VISIBLE);
                textEdit.setVisibility(View.GONE);
                install.setVisibility(View.VISIBLE);
                play.setVisibility(View.GONE);
                pause.setVisibility(View.GONE);
                resume.setVisibility(View.GONE);
                break;
            case READY:
                textList.setVisibility(View.VISIBLE);
                textEdit.setVisibility(View.GONE);
                install.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
                resume.setVisibility(View.GONE);
                break;
            case PLAYING:
                textList.setVisibility(View.VISIBLE);
                textEdit.setVisibility(View.GONE);
                install.setVisibility(View.GONE);
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                resume.setVisibility(View.GONE);
                break;
            case PAUSED:
                textList.setVisibility(View.VISIBLE);
                textEdit.setVisibility(View.GONE);
                install.setVisibility(View.GONE);
                play.setVisibility(View.GONE);
                pause.setVisibility(View.GONE);
                resume.setVisibility(View.VISIBLE);
                break;
            case EDIT:
                textList.setVisibility(View.GONE);
                textEdit.setVisibility(View.VISIBLE);
                install.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
                resume.setVisibility(View.GONE);
                break;
            default:
                Log.w(AudibleMain.class.getName(),"unknown status "+status);
        }
        extraKeyboard.setEnabled(value==Status.READY||value==Status.EDIT);
        setup.setEnabled(value==Status.INSTALL||value==Status.READY);
        prev.setEnabled(value==Status.PAUSED||value==Status.PLAYING);
        next.setEnabled(value==Status.PAUSED||value==Status.PLAYING);
        pause.setEnabled(value==Status.PLAYING);
        play.setEnabled(value==Status.READY);
    }
    private void speakerInstall()
    {
        speaker.install();
    }
    private void speakerPlay()
    {
        setStatus(Status.PLAYING);
        player.setLowerCase(readingLowercase);
        player.setDetection(this.toggleLang.isChecked());
        player.setIgnorable(this.readingIgnore, this.readingAlternative);
        player.setView(DebugGauge.wrap(gaugePlayer));
        player.getGaugeProgress().setShow(true, true, true);
        boolean ignoreTitle = this.readingIgnoreTitle || (this.readingIgnoreTitleRepeated && isTitleRepeated(this.head, this.body));
        player.setIgnoreFirst(ignoreTitle);
        player.play();
    }
    private void speakerPause()
    {
        setStatus(Status.PAUSED);
        player.pause();
    }
    private void speakerStop()
    {
        setStatus(Status.READY);
        player.stop();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        manager.onActivityResult(requestCode, resultCode, data);
        if(resultCode==CODE_PREFERENCES)
        {
            loadPreferences();
        }
        else if (requestCode==CODE_PICK_TEXT_FILE)
        {
            if(data!=null)
            {
                loadTextFile(data.getData());
            }
        }
    }

    @Override
    protected void onStop()
    {
        classifier.setStatusNotifier(null);
        KeepAudibleLoadService.setActive(false, settings.getQuickStart());
        player.stop();
        settings.setTitle(head);
        settings.setBody(body);
        settings.setAuto(toggleAuto.isChecked());
        new Thread(new Runnable()
        {
            public void run()
            {
                settings.commit();
            }
        }).start();
        Log.v("-audible-head",head);
        Log.v("-audible-body",body);
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        manager.shutdown();
        super.onDestroy();
    }

    private static String toString(Object obj)
    {
        if(obj==null)
        {
            return null;
        }
        return obj.toString();
    }
    static final boolean parallel=true;
    private void loadHeadAndBody()
    {
        //set text, from intent or saved
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        Bundle bundle;
        if(action!=null && intent!=null && (bundle=intent.getExtras())!=null)
        {
            Log.d("-audible-","action:"+action);
            Log.d("-audible-","intent:"+intent);
            head = bundle.get(Intent.EXTRA_SUBJECT).toString();
            body = toString(bundle.get(Intent.EXTRA_TEXT));
            autoPlayExit = true;
        }
        else
        {
            head = settings.getTitle();
            body= settings.getBody();
            autoPlayExit = false;
        }
    }
    private void setButtonsListeners()
    {
        //get components for buttons pannel
        final Intent about = new Intent(this,AudiblePreferenceActivity.class);
        setup.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                settings.setAuto(toggleAuto.isChecked());
                startActivityForResult(about, 0);
            }
        });
        prev.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                player.prev(togglePhrase.isChecked());
            }
        });
        next.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                player.next(togglePhrase.isChecked());
            }

        });
        stop.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                speakerStop();
            }
        });
        play.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                speakerPlay();
            }

        });
        pause.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                speakerPause();
            }

        });
        resume.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                speakerPlay();
            }

        });
        install.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                speakerInstall();
            }
        });
    }

    public void onStatusPlay()
    {
        if(toggleAuto.isChecked() && autoScreenLock)
        {
            if(policyAdmin.isAdminActive())
            {
                notifier.i("Auto Lock Screen");
                policyAdmin.lockNow();
            }
            else
            {
                notifier.i("Can't Lock Screen. Set Admin permit using 'Lock Screen' configuration option.");
            }
        }
    }

    private volatile int lastSmoothScroll = 0;
    public void onStatusPlaying(int row, int col, final Locale locale, String utterance)
    {
        final int index = Math.max(row-1,0);
        handler.post(new Runnable()
        {
            public void run()
            {
                if(index != lastSmoothScroll)
                {
                    textList.smoothScrollToPosition(index);
                    lastSmoothScroll = index;
                }
            }
        });
    }

    public void onStatusStop()
    {
        handler.post(new Runnable()
        {
            public void run()
            {
                setStatus(Status.READY);
                if(autoPlayExit && toggleAuto.isChecked() && autoExit)
                {
                    notifier.i("auto-exit");
                    AudibleMain.this.finish();
                }
            }
        });
    }

    public void onStatusPause()
    {
        handler.post(new Runnable()
        {
            public void run()
            {
                setStatus(Status.PAUSED);
            }
        });
    }

    final View.OnClickListener toggleVolumeListener = new View.OnClickListener()
    {
        public void onClick(View arg0)
        {
            volumePannel.setVisibility(toggleVolume.isChecked()?View.VISIBLE:View.GONE);
        }
    };
    final SeekBar.OnSeekBarChangeListener volumeBarListener = new SeekBar.OnSeekBarChangeListener()
    {
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        {
            if(fromUser)
            {
                setVolume(progress);
            }
        }
        public void onStartTrackingTouch(SeekBar seekBar)
        {
        }
        public void onStopTrackingTouch(SeekBar seekBar)
        {
        }
    };
    final View.OnClickListener volumeAddListener = new View.OnClickListener()
    {
        public void onClick(View arg0)
        {
            adjustVolume(+1);
        }
    };
    final View.OnClickListener volumeSubListener = new View.OnClickListener()
    {
        public void onClick(View arg0)
        {
            adjustVolume(-1);
        }
    };

    final View.OnClickListener toggleLangListener = new View.OnClickListener()
    {
        public void onClick(View arg0)
        {
            player.setDetection(toggleLang.isChecked());
            if(toggleLang.isChecked())
            {
                player.start();
            }
        }
    };

    //get components for toggle pannel
    final View.OnClickListener toggleExtraListener = new View.OnClickListener()
    {
        public void onClick(View arg0)
        {
            layoutHomeExtra.setVisibility(toggleExtra.isChecked() ? View.VISIBLE : View.GONE);
        }
    };
    final View.OnClickListener extraCopyListener = new View.OnClickListener()
    {
        public void onClick(View arg0)
        {
            ClipboardManager cb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            cb.setText(body);
        }
    };
    //get components for extra pannel

    final View.OnClickListener extraPasteListener = new View.OnClickListener()
    {
        public void onClick(View arg0)
        {
            ClipboardManager cb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            if (cb.hasText()) {
                setTitleAndText("ClipBoard", cb.getText().toString());
            }
        }
    };

    final View.OnClickListener extraEraseListener = new View.OnClickListener()
    {
        public void onClick(View arg0)
        {
            ClipboardManager cb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            cb.setText("");
        }
    };

    final View.OnClickListener extraKeyboardListener = new View.OnClickListener()
    {
        public void onClick(View arg0)
        {
            toggleEdit();
        }
    };

    final View.OnClickListener extraFileListener = new View.OnClickListener()
    {
        public void onClick(View arg0)
        {
            InvokeIntents.getContent(AudibleMain.this, HTTP.PLAIN_TEXT_TYPE, R.string.choose_text_file_with, CODE_PICK_TEXT_FILE, true);
        }
    };

    final View.OnClickListener extraSendtoListener = new View.OnClickListener()
    {
        public void onClick(View arg0)
        {
            InvokeIntents.send(AudibleMain.this, HTTP.PLAIN_TEXT_TYPE, R.string.choose_send_with, head, body);
        }
    };

    static boolean isTitleRepeated(String title, String body)
    {
        return body.trim().toLowerCase().startsWith(title.trim().toLowerCase());
    }
    void setVolume(int volume)
    {
        player.setVolume(volume, toggleVolume.isChecked()?0:SpeechPlayer.FLAG_SHOW_UI);
        paintVolume();
    }
    void adjustVolume(int direction)
    {
        player.adjustVolume(direction, toggleVolume.isChecked()?0:SpeechPlayer.FLAG_SHOW_UI);
        paintVolume();
    }
    volatile boolean stoppedByVolume = false;

    void paintVolume()
    {
        int volume = player.getVolume();
        int percentage = player.getVolumePercentage();
        volumeBar.setProgress(volume);
        volumeText.setText(volumePrefix+" "+percentage+"%");
        if(volume==0 && !stoppedByVolume)
        {
            stoppedByVolume = true;
            speakerPause();
        }
        else if(volume>0 && stoppedByVolume)
        {
            stoppedByVolume = false;
            speakerPlay();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        switch(keyCode)
        {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                adjustVolume(-1);
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                adjustVolume(+1);
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    private void toggleEdit()
    {
        if(textEdit.getVisibility()==View.VISIBLE)
        {
            this.setTitleAndText(head, textEdit.getText().toString());
            player.unregisterOnStatusChangedListener(this);
            player = buildSpeechPlayer();
            setStatus(needInstallEngine?Status.INSTALL:Status.READY);
        }
        else
        {
            textEdit.setText(body);
            setStatus(Status.EDIT);
        }
    }
    private void loadTextFile(Uri uri)
    {
        try
        {
            if(uri!=null)
            {
                InputStream in = getContentResolver().openInputStream(uri);
                String text = new String(Files.bytesFromFile(in));
                this.setTitleAndText(uri.getLastPathSegment(), text);
                player.unregisterOnStatusChangedListener(this);
                player = buildSpeechPlayer();
                setStatus(needInstallEngine?Status.INSTALL:Status.READY);
            }
        }
        catch (IOException ex)
        {
            Log.e(AudibleMain.class.getSimpleName(),"loadTextFile",ex);
        }
    }

}
