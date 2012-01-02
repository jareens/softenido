/*
 * Battery.java
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

/**
 * Created by IntelliJ IDEA.
 * User: franci
 * Date: 26/11/11
 * Time: 11:50
 * To change this template use File | Settings | File Templates.
 */
public class Battery
{
    final int plugged;
    final int status;
    final boolean present;
    final int health;
    final int level;
    final int scale;
    final String technology;
    final int temperature;
    final int voltage;

    Battery(int plugged, int status, boolean present, int health, int level, int scale, String technology, int temperature, int voltage)
    {
        this.plugged = plugged;
        this.status = status;
        this.present = present;
        this.health = health;
        this.level = level;
        this.scale = scale;
        this.technology = technology;
        this.temperature = temperature;
        this.voltage = voltage;
    }
    public int getPlugged()
    {
        return plugged;
    }

    public int getStatus()
    {
        return status;
    }

    public boolean isPresent()
    {
        return present;
    }

    public int getHealth()
    {
        return health;
    }

    public int getLevel()
    {
        return level;
    }

    public int getScale()
    {
        return scale;
    }

    public String getTechnology()
    {
        return technology;
    }

    public int getTemperature()
    {
        return temperature;
    }

    public int getVoltage()
    {
        return voltage;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Battery battery = (Battery) o;

        if(health != battery.health) return false;
        if(level != battery.level) return false;
        if(plugged != battery.plugged) return false;
        if(present != battery.present) return false;
        if(scale != battery.scale) return false;
        if(status != battery.status) return false;
        if(temperature != battery.temperature) return false;
        if(voltage != battery.voltage) return false;
        if(technology != battery.technology)
        {
            //if one of the technology variables is null and the other is not,
            //objects are different from each other
            if(technology==null) return false;
            if(battery.technology==null) return false;

            return technology.equals(battery.technology);
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = plugged;
        result = 31 * result + status;
        result = 31 * result + (present ? 1 : 0);
        result = 31 * result + health;
        result = 31 * result + level;
        result = 31 * result + scale;
        result = 31 * result + (technology != null ? technology.hashCode() : 0);
        result = 31 * result + temperature;
        result = 31 * result + voltage;
        return result;
    }

    @Override
    public String toString()
    {
        return "Battery{" +
                "plugged=" + plugged +
                ", status=" + status +
                ", present=" + present +
                ", health=" + health +
                ", level=" + level +
                ", scale=" + scale +
                ", technology='" + technology + '\'' +
                ", temperature=" + temperature +
                ", voltage=" + voltage +
                '}';
    }
}
