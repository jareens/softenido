/*
 * AndroidVersion.java
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

package com.softenido.droidcore.os;

import android.os.Build;

/**
 * Created by IntelliJ IDEA.
 * User: franci
 * Date: 17/12/11
 * Time: 15:52
 * To change this template use File | Settings | File Templates.
 */
public class AndroidVersion
{
    static public final AndroidVersion os = new AndroidVersion();

    public final int SDK;
    public final String RELEASE;
    public final String NAME;

    public AndroidVersion()
    {
        this.SDK = Build.VERSION.SDK_INT;
        this.RELEASE = Build.VERSION.RELEASE;
        this.NAME = versionName(Build.VERSION.SDK_INT);
    }

    public String versionName(int sdk)
    {
        switch (sdk)
        {
            case 1:
                return "base";
            case 2:
                return "base 1.1";
            case 3:
                return "cupcake";
            case 4:
                return "donut";
            case 5:
                return "eclair";
            case 6:
                return "eclair 0.1";
            case 7:
                return "eclair mr1";
            case 8:
                return "froyo";
            case 9:
                return "gingerbread";
            case 10:
                return "gingerbread mr1";
            case 11:
                return "honeycomb";
            case 12:
                return "honeycomb_mr1";
            case 13:
                return "honeycomb_mr2";
            case 14:
                return "ice cream sandwich";
            case 15:
                return "ice cream sandwich mr1";
            case 10000:
                return "cur development";
            default:
                return "unknown";
        }
    }

}
