/*
 * RiskManagerActivity.java
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
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.softenido.droiddesk.admob.AdMob;
import com.softenido.formulas.TradingFormulas;

import java.util.Properties;

public class TradeRiskActivity extends Activity
{
    static final String BUYING_POWER = "traderisk1.buyingpower";
    static final String TRADE_RISK = "traderisk1.traderisk1";
    static final String TRADE_COMMISION = "traderisk1.tradecommision";
    static final String SHARE_SLIPPAGE = "traderisk1.shareslippage";
    static final String TRADE_RATIO2 = "traderisk1.traderatio2";

    private AdMob admob=null;

    EditText buyingPower;
    EditText tradeRisk;
    EditText tradeCommision;
    EditText shareSlippage;
    EditText tradeTarget;
    EditText stopLimitStop;
    EditText stopLimitLimit;
    EditText stopLoss;
    EditText tradeRatio1;
    EditText tradeRatio2;
    EditText tradeSize;

    Color bg = null;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traderisk);

        admob = AdMob.addBanner(this,R.id.traderiskmanagerLayout);

        buyingPower = (EditText) findViewById(R.id.buying_power);
        tradeRisk = (EditText) findViewById(R.id.trade_risk);
        tradeCommision = (EditText) findViewById(R.id.trade_commision);
        shareSlippage = (EditText) findViewById(R.id.share_slippage);
        tradeTarget = (EditText) findViewById(R.id.trade_target);
        stopLimitStop = (EditText) findViewById(R.id.stop_limit_stop);
        stopLimitLimit = (EditText) findViewById(R.id.stop_limit_limit);
        stopLoss = (EditText) findViewById(R.id.stop_loss);
        tradeRatio1 = (EditText) findViewById(R.id.trade_ratio1);
        tradeRatio2 = (EditText) findViewById(R.id.trade_ratio2);
        tradeSize = (EditText) findViewById(R.id.trade_size);

        Button tradeReset = (Button) findViewById(R.id.bReset);
        Button tradeUpdate = (Button) findViewById(R.id.bUpdate);


        TextView.OnEditorActionListener actionListener = new TextView.OnEditorActionListener()
        {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent)
            {
                paint();
                if(keyEvent.getKeyCode()==KeyEvent.KEYCODE_ENTER)
                {
                    View view = textView.focusSearch(View.FOCUS_DOWN);
                    if(view!=null && view.getClass()!=Button.class)
                        view.requestFocus();
                }
                return true;
            }
        };
        TextView.OnFocusChangeListener focusListener = new TextView.OnFocusChangeListener()
        {
            public void onFocusChange(View view, boolean b)
            {
                paint();
            }
        };

        buyingPower.setOnEditorActionListener(actionListener);
        tradeRisk.setOnEditorActionListener(actionListener);
        tradeCommision.setOnEditorActionListener(actionListener);
        shareSlippage.setOnEditorActionListener(actionListener);
        tradeTarget.setOnEditorActionListener(actionListener);
        stopLimitStop.setOnEditorActionListener(actionListener);
        stopLoss.setOnEditorActionListener(actionListener);
        tradeRatio2.setOnEditorActionListener(actionListener);

        buyingPower.setOnFocusChangeListener(focusListener);
        tradeRisk.setOnFocusChangeListener(focusListener);
        tradeCommision.setOnFocusChangeListener(focusListener);
        shareSlippage.setOnFocusChangeListener(focusListener);
        tradeTarget.setOnFocusChangeListener(focusListener);
        stopLimitStop.setOnFocusChangeListener(focusListener);
        stopLoss.setOnFocusChangeListener(focusListener);
        tradeRatio2.setOnFocusChangeListener(focusListener);

        tradeReset.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View view)
            {
                tradeTarget.setText("");
                stopLimitStop.setText("");
                stopLoss.setText("");
                tradeTarget.requestFocus();
                paint();
            }
        });
        tradeUpdate.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View view)
            {
                paint();
            }
        });

        Properties config = TraderToolBoxApp.getConfiguration();

        buyingPower.setText(config.getProperty(BUYING_POWER,"100000"));
        tradeRisk.setText(config.getProperty(TRADE_RISK, "2000"));
        tradeCommision.setText(config.getProperty(TRADE_COMMISION, "10"));
        shareSlippage.setText(config.getProperty(SHARE_SLIPPAGE, "0.01"));
        tradeRatio2.setText(config.getProperty(TRADE_RATIO2, "2"));
    }

    @Override
    protected void onPause()
    {
        Properties config = TraderToolBoxApp.getConfiguration();
        config.setProperty(BUYING_POWER, buyingPower.getText().toString());
        config.setProperty(TRADE_RISK, tradeRisk.getText().toString());
        config.setProperty(TRADE_COMMISION, tradeCommision.getText().toString());
        config.setProperty(SHARE_SLIPPAGE, shareSlippage.getText().toString());
        config.setProperty(TRADE_RATIO2, tradeRatio2.getText().toString());
        TraderToolBoxApp.saveConfiguration(this);
        super.onPause();
    }

    double buyingPowerValue = 0;
    double tradeRiskValue = 0;
    double tradeCommisionValue = 0;
    double shareSlippageValue = 0;
    double tradeTargetValue = 0;
    double stopLimitStopValue = 0;
    double stopLimitLimitValue = 0;
    double stopLossValue = 0;
    double tradeRatio1Value = 0;
    double tradeRatio2Value = 0;
    int tradeSizeValue = 0;

   static double getValue(TextView tv)
   {
       try
       {
            return Double.parseDouble(tv.getText().toString());
       }
       catch (NumberFormatException ex)
       {
            return 0;
       }
   }

   public void paint()
   {
       buyingPowerValue = getValue(buyingPower);
       tradeRiskValue = getValue(tradeRisk);
       tradeCommisionValue = getValue(tradeCommision);
       shareSlippageValue = getValue(shareSlippage);
       tradeTargetValue = getValue(tradeTarget);
       stopLimitStopValue = getValue(stopLimitStop);
       stopLossValue = getValue(stopLoss);
       tradeRatio2Value = getValue(tradeRatio2);

       stopLimitLimitValue = TradingFormulas.tradeLimit(tradeTargetValue,stopLossValue,tradeRatio2Value);
       tradeRatio1Value    = TradingFormulas.tradeRisk(tradeTargetValue,stopLimitStopValue,stopLossValue);
       tradeSizeValue      = TradingFormulas.tradeSize(buyingPowerValue, tradeRiskValue, stopLimitLimitValue, stopLossValue, tradeCommisionValue, shareSlippageValue);

       stopLimitLimit.setText(String.format("%.2f",stopLimitLimitValue));
       tradeRatio1.setText(String.format("%.2f",tradeRatio1Value));

       tradeSize.setText(String.format("%d", tradeSizeValue));
   }
}