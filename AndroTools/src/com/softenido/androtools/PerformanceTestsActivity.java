/*
 * PerformanceTestsActivity.java
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
package com.softenido.androtools;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import com.softenido.cafecore.benchmark.Benchmark;
import com.softenido.cafecore.benchmark.BenchmarkManager;
import com.softenido.cafedroid.admob.AdMob;
import com.softenido.cafedroid.gauge.DroidGaugeBuilder;
import com.softenido.cafedroid.gauge.DroidGaugeProgress;

/**
 * Created with IntelliJ IDEA.
 * User: franci
 * Date: 1/11/12
 * Time: 17:30
 * To change this template use File | Settings | File Templates.
 */
public class PerformanceTestsActivity extends Activity
{
    private AdMob admob=null;
    volatile Handler handler=null;

    volatile DroidGaugeProgress gauge;

    TextView newAllocation;
    TextView stringOperations;
    TextView intOperations;
    TextView longOperations;
    TextView floatOperations;
    TextView doubleOperations;
    TextView bigInteger;
    TextView sqrt;
    TextView md5;
    TextView sha1;
    TextView average;
    CheckBox exaustive;
    Button play;
    Button stop;

    volatile BenchmarkManager manager = BenchmarkManager.buildDefault(MILLIS);

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.performance);
        admob = AdMob.addBanner(this, R.id.performance_main, true);

        handler = new Handler();
        gauge = DroidGaugeBuilder.createProgressBefore(handler, this, R.id.performance_main, R.id.firstLine, DroidGaugeBuilder.Mode.BAR_TEXT, DroidGaugeBuilder.Thin.MED);
        gauge.setShow(true, true, true);

        newAllocation = (TextView)findViewById(R.id.memory_allocation);
        stringOperations = (TextView)findViewById(R.id.string_operations);
        intOperations = (TextView)findViewById(R.id.int_operations);
        longOperations = (TextView)findViewById(R.id.long_operations);
        floatOperations = (TextView)findViewById(R.id.float_operations);
        doubleOperations = (TextView)findViewById(R.id.double_operations);
        bigInteger = (TextView)findViewById(R.id.big_integer);
        sqrt = (TextView)findViewById(R.id.big_decimal);
        md5 = (TextView)findViewById(R.id.md5);
        sha1 = (TextView)findViewById(R.id.sha1);
        average = (TextView)findViewById(R.id.average);
        play = (Button)findViewById(R.id.play_button);
        stop = (Button)findViewById(R.id.stop_button);
        exaustive = (CheckBox)findViewById(R.id.exaustive);

        play.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                play.setEnabled(false);
                stop.setEnabled(true);
                new Thread(new Runnable()
                {
                    public void run()
                    {
                        doTest();
                    }
                }).start();
            }
        });
        stop.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                manager.cancel();
            }
        });

        stop.setEnabled(false);

    }

    static int NPM = 1000000;
    static int MILLIS = 10000;

    void doTest()
    {
        boolean exaustive = this.exaustive.isChecked();
        final TextView[] views = {newAllocation, stringOperations, intOperations, longOperations, floatOperations, doubleOperations, bigInteger, sqrt, md5, sha1};
        final String[] names   = {"new",         "String",         "int",         "long",         "float",         "double",         "BigInteger",   "sqrt", "md5", "sha1"};

        manager = BenchmarkManager.buildDefault(exaustive?MILLIS*6:MILLIS);

        handler.post(new Runnable()
        {
            public void run()
            {
                for(int i=0;i<views.length;i++)
                {
                    views[i].setText(names[i]+"=");
                }
            }
        });

        manager.setView(gauge);
        manager.run();
        if(!manager.isCanceled())
        {
            manager.print(System.out);
            for(int i=0;i<views.length;i++)
            {
                Benchmark bm =  manager.getBenchmark(names[i]);
                if(bm==null)
                {
                    continue;
                }
                final String text = String.format("%s=%.2f", names[i],bm.getRate());
                final TextView tv = views[i];
                handler.post(new Runnable()
                {
                    public void run()
                    {
                        tv.setText(text);
                    }
                });
            }
        }
        handler.post(new Runnable()
        {
            public void run()
            {
                PerformanceTestsActivity.this.average.setText("average="+manager.getAverage());
                play.setEnabled(true);
                stop.setEnabled(false);
            }
        });
    }
}
