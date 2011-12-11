/*
 * WifixTest.java
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

package com.softenido.wifix;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.softenido.wifix.WifixTest \
 * com.softenido.wifix.tests/android.test.InstrumentationTestRunner
 */
public class WifixTest extends ActivityUnitTestCase<Wifix>
{
    public WifixTest()
    {
        super(Wifix.class);
    }

    @Override
    public void setUp() throws Exception
    {
        super.setUp();

    }

    public void testPreconditions() throws Exception
    {
        startActivity(new Intent(getInstrumentation().getTargetContext(),Wifix.class),null,null);
        Activity activity = getActivity();

        assertNotNull(activity.findViewById(R.id.bReassign));
        assertNotNull(activity.findViewById(R.id.bReconnect));
        assertNotNull(activity.findViewById(R.id.cbKeepLock));
        assertNotNull(activity.findViewById(R.id.time_spinner));
        assertNotNull(activity.findViewById(R.id.battery_spinner));
        assertNotNull(activity.findViewById(R.id.bAbout));
        assertNotNull(activity.findViewById(R.id.bHide));
    }
}
