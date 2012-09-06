/*
 * ProfileManager.java
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
package com.softenido.cafecore.profile;

import java.util.HashMap;

/**
 *
 * @author franci
 */
class ProfileManager
{
    private final Object lock = new Object();

    private static final ProfileManager real = new ProfileManager();
    private static final ProfileManager fake = new ProfileManager()
    {
        @Override
        Profiler demandLogger(String name)
        {
            return new NullProfiler(name);
        }
        
    };
    volatile boolean enabled=false;
    static ProfileManager getProfileManager(boolean active)
    {
        return active?real:fake;
    }
    
    final HashMap<String,Profiler> map = new HashMap<String,Profiler>();
    
    Profiler demandLogger(String name)
    {
        synchronized(lock)
        {
            Profiler profiler = map.get(name);
            if(profiler==null)
            {
                profiler = new NanoProfiler(name);
                map.put(name,profiler);
            }
            return profiler;
        }
    }
}
