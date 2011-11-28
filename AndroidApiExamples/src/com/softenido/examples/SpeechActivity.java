/*
 * TextToSpeechActivity.java
 *
 * Copyright (c) 2011  Francisco Gómez Carrasco
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

package com.softenido.examples;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import com.softenido.droidcore.util.BatteryChanged;
import com.softenido.droiddesk.admob.AdMob;
import com.softenido.droiddesk.speech.SpeechBuilder;
import com.softenido.droiddesk.speech.SpeechHearer;
import com.softenido.droiddesk.speech.SpeechSpeaker;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class SpeechActivity extends Activity

{
    static final int SAY_RESULT    = 1;
    static final int LISTEN_RESULT = 2;

    private AdMob admob=null;
    private BatteryChanged bc=null;
    private SpeechBuilder speech = null;
    private AtomicBoolean cancel = new AtomicBoolean();
    private Handler handler = null;
    private ServerSocket server=null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speech_tools);
        admob = AdMob.addBanner(this, R.id.mainLayout,true);

        final EditText edit = (EditText) findViewById(R.id.text_to_speech_edit);
        final Button say = (Button) findViewById(R.id.text_to_speech_say);
        final Button install = (Button) findViewById(R.id.text_to_speech_install);
        final Button listen = (Button) findViewById(R.id.text_to_speech_listen);
        final ToggleButton port = (ToggleButton) findViewById(R.id.text_to_speech_port);
        handler = new Handler();

        say.setEnabled(false);
        install.setEnabled(false);

        speech = new SpeechBuilder(this,Locale.getDefault())
        {
            @Override
            protected void onSpeekerInstallNeeded()
            {
                install.setEnabled(true);
                say.setEnabled(false);
            }

            @Override
            public void onSpeekerInitied(boolean status)
            {
                install.setEnabled(false);
                say.setEnabled(status);
            }

            @Override
            public void onHearerRecognized(ArrayList<String> text)
            {
                if(text!=null)
                {
                    StringBuilder sb = new StringBuilder();
                    for(String item : text)
                    {
                        sb.append(item).append(" ");
                    }
                    edit.setText(sb.toString());
                }
            }
        };
        final SpeechSpeaker speaker = speech.getSpeaker();
        final SpeechHearer hearer = speech.getHearer();

        speaker.start();

        say.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                speaker.speak(edit.getText().toString(), true);
            }
        });
        install.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                speaker.install();
            }
        });

        listen.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                hearer.hear("Say something");
            }
        });
        port.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                try
                {
                    if(port.isChecked())
                    {
                        portServer();
                    }
                    else if(server != null)
                    {
                        cancel.set(true);
                        server.close();
                    }
                }
                catch (IOException ex)
                {
                    Logger.getLogger(SpeechActivity.class.getName()).log(Level.WARNING,"exception closing server");
                    Toast.makeText(getApplicationContext(),ex.toString(),Toast.LENGTH_LONG);
                }
            }
        });
    }

    private List<String> phrases = new ArrayList<String>();

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        speech.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop()
    {
        speech.shutdown();
        if(server!=null)
        {
            cancel.set(true);
        }
        super.onStop();
    }

    private boolean portServer() throws IOException
    {
        cancel = new AtomicBoolean();
        server = new ServerSocket(9999);

        toastTelnetHelp(server);

        new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    loopSocket(server, cancel);
                }
                catch (IOException ex)
                {
                    Logger.getLogger(SpeechActivity.class.getName()).log(Level.SEVERE,"getting Socket",ex);
                }
            }
        }).start();
        return true;
    }

    private void toastTelnetHelp(ServerSocket server) throws UnknownHostException
    {
        String text = "telnet "+server.getInetAddress().getCanonicalHostName()+" 9999";
        Toast.makeText(this.getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    private void loopSocket(ServerSocket server, AtomicBoolean cancel) throws IOException
    {
        while(!cancel.get())
        {
            byte[] buf = new byte[64000];
            Socket sock = server.accept();

            InputStream in=  sock.getInputStream();
            int r=0;
            while(!cancel.get() && (r = in.read(buf))>0)
            {
                String text = new String(buf,0, r);
                Scanner sc = new Scanner(text);
                while(!cancel.get() && sc.hasNextLine())
                {
                    String line = sc.nextLine();
                    speech.getSpeaker().speak(line,false);
                }
            }
            sock.close();
        }
        server.close();
    }

}
