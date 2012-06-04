/*
 * EquityRiskActivity.java
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

package com.softenido.tradertoolbox;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import com.softenido.droiddesk.admob.AdMob;

public class EquityRiskActivity extends Activity
{
    private AdMob admob=null;
    
    double equityValue = 0;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equityrisk);
        admob = AdMob.addBanner(this,R.id.equityriskmanagerLayout);

        final EditText equity = (EditText) findViewById(R.id.equity);
        final EditText low = (EditText) findViewById(R.id.low_trade_risk);
        final EditText mid = (EditText) findViewById(R.id.mid_trade_risk);
        final EditText max = (EditText) findViewById(R.id.max_trade_risk);
        final EditText month = (EditText) findViewById(R.id.month_trade_risk);

        equity.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent)
            {
                try
                {
                    double value = Double.parseDouble(textView.getText().toString());
                    if(value!=equityValue)
                    {
                        low.setText(String.format("%.2f",value*0.005));
                        mid.setText(String.format("%.2f",value*0.01));
                        max.setText(String.format("%.2f",value*0.02));
                        month.setText(String.format("%.2f",value*0.06));
                    }
                }
                catch (NumberFormatException ex)
                {
                    low.setText("");
                    mid.setText("");
                    max.setText("");
                    month.setText("");
                }
                return true;
            }
        });



   }

}