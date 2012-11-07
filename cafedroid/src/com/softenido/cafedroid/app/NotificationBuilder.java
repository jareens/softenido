/*
 * NotificationBuilder.java
 *
 * Copyright (c) 2011-2012 Francisco Gómez Carrasco
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
 *
 * most part of this file has been copied from android source code
 */

package com.softenido.cafedroid.app;

import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.widget.ProgressBar;
import android.widget.RemoteViews;


/**
 * Created by IntelliJ IDEA.
 * User: franci
 * Date: 29/11/11
 * Time: 22:15
 * To change this template use File | Settings | File Templates.
 */
public class NotificationBuilder
{
    private Context mContext;

    private long mWhen;
    private int mSmallIcon;
    private int mSmallIconLevel;
    private int mNumber;
    private CharSequence mContentTitle;
    private CharSequence mContentText;
    private CharSequence mContentInfo;
    private PendingIntent mContentIntent;
    private RemoteViews mContentView;
    private PendingIntent mDeleteIntent;
    // api-9 private PendingIntent mFullScreenIntent;
    private CharSequence mTickerText;
    // api-11 private RemoteViews mTickerView;
    // api-11 private Bitmap mLargeIcon;
    private Uri mSound;
    private int mAudioStreamType;
    private long[] mVibrate;
    private int mLedArgb;
    private int mLedOnMs;
    private int mLedOffMs;
    private int mDefaults;
    private int mFlags;
    private int mProgressMax;
    private int mProgress;
    private boolean mProgressIndeterminate;

    /**
     * Constructor.
     *
     * Automatically sets the when field to {@link System#currentTimeMillis()
     * System.currentTimeMllis()} and the audio stream to the {@link android.app.Notification#STREAM_DEFAULT}.
     *
     * @param context A {@link Context} that will be used to construct the
     *      RemoteViews. The Context will not be held past the lifetime of this
     *      Builder object.
     */
    public NotificationBuilder(Context context)
    {
        mContext = context;

        // Set defaults to match the defaults of a Notification
        mWhen = System.currentTimeMillis();
        mAudioStreamType = android.app.Notification.STREAM_DEFAULT;
    }

    /**
     * Set the time that the event occurred.  Notifications in the panel are
     * sorted by this time.
     */
    public NotificationBuilder setWhen(long when)
    {
        mWhen = when;
        return this;
    }

    /**
     * Set the small icon to use in the notification layouts.  Different classes of devices
     * may return different sizes.  See the UX guidelines for more information on how to
     * design these icons.
     *
     * @param icon A resource ID in the application's package of the drawble to use.
     */
    public NotificationBuilder setSmallIcon(int icon)
    {
        mSmallIcon = icon;
        return this;
    }

    /**
     * A variant of {@link #setSmallIcon(int) setSmallIcon(int)} that takes an additional
     * level parameter for when the icon is a {@link android.graphics.drawable.LevelListDrawable
     * LevelListDrawable}.
     *
     * @param icon A resource ID in the application's package of the drawble to use.
     * @param level The level to use for the icon.
     *
     * @see android.graphics.drawable.LevelListDrawable
     */
    public NotificationBuilder setSmallIcon(int icon, int level) {
        mSmallIcon = icon;
        mSmallIconLevel = level;
        return this;
    }

    /**
     * Set the title (first row) of the notification, in a standard notification.
     */
    public NotificationBuilder setContentTitle(CharSequence title) {
        mContentTitle = title;
        return this;
    }

    /**
     * Set the text (second row) of the notification, in a standard notification.
     */
    public NotificationBuilder setContentText(CharSequence text) {
        mContentText = text;
        return this;
    }

    /**
     * Set the large number at the right-hand side of the notification.  This is
     * equivalent to setContentInfo, although it might toast the number in a different
     * font size for readability.
     */
    public NotificationBuilder setNumber(int number) {
        mNumber = number;
        return this;
    }

    /**
     * Set the large text at the right-hand side of the notification.
     */
    public NotificationBuilder setContentInfo(CharSequence info) {
        mContentInfo = info;
        return this;
    }

    /**
     * Set the progress this notification represents, which may be
     * represented as a {@link ProgressBar}.
     */
    public NotificationBuilder setProgress(int max, int progress, boolean indeterminate) {
        mProgressMax = max;
        mProgress = progress;
        mProgressIndeterminate = indeterminate;
        return this;
    }

    /**
     * Supply a custom RemoteViews to use instead of the standard one.
     */
    public NotificationBuilder setContent(RemoteViews views)
    {
        mContentView = views;
        return this;
    }

    /**
     * Supply a {@link PendingIntent} to send when the notification is clicked.
     * If you do not supply an intent, you can now add PendingIntents to individual
     * views to be launched when clicked by calling {@link RemoteViews#setOnClickPendingIntent
     * RemoteViews.setOnClickPendingIntent(int,PendingIntent)}.
     */
    public NotificationBuilder setContentIntent(PendingIntent intent) {
        mContentIntent = intent;
        return this;
    }

    /**
     * Supply a {@link PendingIntent} to send when the notification is cleared by the user
     * directly from the notification panel.  For example, this intent is sent when the user
     * clicks the "Clear all" button, or the individual "X" buttons on notifications.  This
     * intent is not sent when the application calls {@link android.app.NotificationManager#cancel
     * NotificationManager.cancel(int)}.
     */
    public NotificationBuilder setDeleteIntent(PendingIntent intent) {
        mDeleteIntent = intent;
        return this;
    }

//    /**
//     * An intent to launch instead of posting the notification to the status bar.
//     * Only for use with extremely high-priority notifications demanding the user's
//     * <strong>immediate</strong> attention, such as an incoming phone call or
//     * alarm clock that the user has explicitly set to a particular time.
//     * If this facility is used for something else, please give the user an option
//     * to turn it off and use a normal notification, as this can be extremely
//     * disruptive.
//     *
//     * @param intent The pending intent to launch.
//     * @param highPriority Passing true will cause this notification to be sent
//     *          even if other notifications are suppressed.
//     */
//    public NotificationBuilder setFullScreenIntent(PendingIntent intent, boolean highPriority)
//    {
//        mFullScreenIntent = intent;
//        setFlag(Notification.FLAG_HIGH_PRIORITY, highPriority);
//        return this;
//    }

    /**
     * Set the text that is displayed in the status bar when the notification first
     * arrives.
     */
    public NotificationBuilder setTicker(CharSequence tickerText) {
        mTickerText = tickerText;
        return this;
    }

//    /**
//     * Set the text that is displayed in the status bar when the notification first
//     * arrives, and also a RemoteViews object that may be displayed instead on some
//     * devices.
//     */
//    public NotificationBuilder setTicker(CharSequence tickerText, RemoteViews views)
//    {
//        mTickerText = tickerText;
//        mTickerView = views;
//        return this;
//    }

//    /**
//     * Set the large icon that is shown in the ticker and notification.
//     */
//    public NotificationBuilder setLargeIcon(Bitmap icon)
//    {
//        mLargeIcon = icon;
//        return this;
//    }

    /**
     * Set the sound to play.  It will play on the default stream.
     */
    public NotificationBuilder setSound(Uri sound) {
        mSound = sound;
        mAudioStreamType = android.app.Notification.STREAM_DEFAULT;
        return this;
    }

    /**
     * Set the sound to play.  It will play on the stream you supply.
     *
     * @see android.app.Notification#STREAM_DEFAULT
     * @see android.media.AudioManager for the <code>STREAM_</code> constants.
     */
    public NotificationBuilder setSound(Uri sound, int streamType) {
        mSound = sound;
        mAudioStreamType = streamType;
        return this;
    }

    /**
     * Set the vibration pattern to use.
     *
     * @see android.os.Vibrator for a discussion of the <code>pattern</code>
     * parameter.
     */
    public NotificationBuilder setVibrate(long[] pattern) {
        mVibrate = pattern;
        return this;
    }

    /**
     * Set the argb value that you would like the LED on the device to blnk, as well as the
     * rate.  The rate is specified in terms of the number of milliseconds to be on
     * and then the number of milliseconds to be off.
     */
    public NotificationBuilder setLights(int argb, int onMs, int offMs) {
        mLedArgb = argb;
        mLedOnMs = onMs;
        mLedOffMs = offMs;
        return this;
    }

    /**
     * Set whether this is an ongoing notification.
     *
     * <p>Ongoing notifications differ from regular notifications in the following ways:
     * <ul>
     *   <li>Ongoing notifications are sorted above the regular notifications in the
     *   notification panel.</li>
     *   <li>Ongoing notifications do not have an 'X' close button, and are not affected
     *   by the "Clear all" button.
     * </ul>
     */
    public NotificationBuilder setOngoing(boolean ongoing)
    {
        setFlag(android.app.Notification.FLAG_ONGOING_EVENT, ongoing);
        return this;
    }

    /**
     * Set this flag if you would only like the sound, vibrate
     * and ticker to be played if the notification is not already showing.
     */
    public NotificationBuilder setOnlyAlertOnce(boolean onlyAlertOnce)
    {
        setFlag(android.app.Notification.FLAG_ONLY_ALERT_ONCE, onlyAlertOnce);
        return this;
    }

    /**
     * Setting this flag will make it so the notification is automatically
     * canceled when the user clicks it in the panel.  The PendingIntent
     * set with {@link #setDeleteIntent} will be broadcast when the notification
     * is canceled.
     */
    public NotificationBuilder setAutoCancel(boolean autoCancel)
    {
        setFlag(android.app.Notification.FLAG_AUTO_CANCEL, autoCancel);
        return this;
    }

    /**
     * Set the default notification options that will be used.
     * <p>
     * The value should be one or more of the following fields combined with
     * bitwise-or:
     * {@link android.app.Notification#DEFAULT_SOUND}, {@link android.app.Notification#DEFAULT_VIBRATE}, {@link android.app.Notification#DEFAULT_LIGHTS}.
     * <p>
     * For all default values, use {@link android.app.Notification#DEFAULT_ALL}.
     */
    public NotificationBuilder setDefaults(int defaults) {
        mDefaults = defaults;
        return this;
    }

    private void setFlag(int mask, boolean value)
    {
        if (value)
        {
            mFlags |= mask;
        }
        else
        {
            mFlags &= ~mask;
        }
    }

//    private RemoteViews makeRemoteViews(int resId)
//    {
//        RemoteViews contentView = new RemoteViews(mContext.getPackageName(), resId);
//        boolean hasLine3 = false;
//        if (mSmallIcon != 0)
//        {
//            contentView.setImageViewResource(R.id.icon, mSmallIcon);
//            contentView.setViewVisibility(R.id.icon, View.VISIBLE);
//        } else {
//            contentView.setViewVisibility(R.id.icon, View.GONE);
//        }
//        if (mContentTitle != null) {
//            contentView.setTextViewText(R.id.title, mContentTitle);
//        }
//        if (mContentText != null)
//        {
//            contentView.setTextViewText(R.id.text, mContentText);
//            hasLine3 = true;
//        }
//        if (mContentInfo != null)
//        {
//            contentView.setTextViewText(R.id.info, mContentInfo);
//            contentView.setViewVisibility(R.id.info, View.VISIBLE);
//            hasLine3 = true;
//        }
//        else if (mNumber > 0)
//        {
//            final int tooBig = mContext.getResources().getInteger(
//                    R.integer.status_bar_notification_info_maxnum);
//            if (mNumber > tooBig) {
//                contentView.setTextViewText(R.id.info, mContext.getResources().getString(
//                            R.string.status_bar_notification_info_overflow));
//            } else {
//                NumberFormat f = NumberFormat.getIntegerInstance();
//                contentView.setTextViewText(R.id.info, f.format(mNumber));
//            }
//            contentView.setViewVisibility(R.id.info, View.VISIBLE);
//            hasLine3 = true;
//        } else {
//            contentView.setViewVisibility(R.id.info, View.GONE);
//        }
//        if (mProgressMax != 0 || mProgressIndeterminate) {
//            contentView.setProgressBar(
//                    R.id.progress, mProgressMax, mProgress, mProgressIndeterminate);
//            contentView.setViewVisibility(R.id.progress, View.VISIBLE);
//        } else {
//            contentView.setViewVisibility(R.id.progress, View.GONE);
//        }
//        if (mWhen != 0) {
//            contentView.setLong(R.id.time, "setTime", mWhen);
//        }
//        contentView.setViewVisibility(R.id.line3, hasLine3 ? View.VISIBLE : View.GONE);
//        return contentView;
//    }

//    private RemoteViews makeContentView()
//    {
//        if (mContentView != null)
//        {
//            return mContentView;
//        }
//        else
//        {
//                return makeRemoteViews(mLargeIcon == null
//                        ? R.layout.status_bar_latest_event_content
//                    : R.layout.status_bar_latest_event_content_large_icon);
//        }
//    }

//    private RemoteViews makeTickerView()
//    {
//        if (mTickerView != null)
//        {
//            return mTickerView;
//        }
//        else
//        {
//            if (mContentView == null)
//            {
//                return makeRemoteViews(mLargeIcon == null
//                        ? R.layout.status_bar_latest_event_ticker
//                        : R.layout.status_bar_latest_event_ticker_large_icon);
//            }
//            else
//            {
//                return null;
//            }
//        }
//    }

    /**
     * Combine all of the options that have been set and return a new {@link android.app.Notification}
     * object.
     */
    public android.app.Notification getNotification()
    {
        android.app.Notification n = new android.app.Notification();
        n.when = mWhen;
        n.icon = mSmallIcon;
        n.iconLevel = mSmallIconLevel;
        n.number = mNumber;
        //n.contentView = makeContentView();
        n.deleteIntent = mDeleteIntent;
        //n.fullScreenIntent = mFullScreenIntent;
        n.tickerText = mTickerText;
        //n.tickerView = makeTickerView();
        //n.largeIcon = mLargeIcon;
        n.sound = mSound;
        n.audioStreamType = mAudioStreamType;
        n.vibrate = mVibrate;
        n.ledARGB = mLedArgb;
        n.ledOnMS = mLedOnMs;
        n.ledOffMS = mLedOffMs;
        n.defaults = mDefaults;
        n.flags = mFlags;
        if (mLedOnMs != 0 && mLedOffMs != 0)
        {
            n.flags |= android.app.Notification.FLAG_SHOW_LIGHTS;
        }
        if ((mDefaults & android.app.Notification.DEFAULT_LIGHTS) != 0)
        {
            n.flags |= android.app.Notification.FLAG_SHOW_LIGHTS;
        }
        n.setLatestEventInfo(this.mContext,mContentTitle,mContentText,mContentIntent);
        return n;
    }
}
