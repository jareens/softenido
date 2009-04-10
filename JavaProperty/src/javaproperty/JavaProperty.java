/*
 *  JavaProperty.java
 *
 *  Copyright (C) 2009  Francisco Gómez Carrasco
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  Report bugs or new features to: flikxxi@gmail.com
 *
 */
package javaproperty;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author franci
 */
public class JavaProperty
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        if (args.length > 0)
        {
            System.out.println("--System.getProperties--");
            for (String item : args)
            {
                System.out.println(item + "=" + System.getProperty(item.toString()));
            }
            System.out.println("--System.getenv--");
            for (String item : args)
            {
                System.out.println(item + "=" + System.getenv(item.toString()));
            }
        }
        else
        {
            System.out.println("--System.getProperties--");
            Properties prop = System.getProperties();
            Set<Object> set = prop.keySet();
            for (Object item : set)
            {
                String key = item.toString();
                System.out.println(key + "=" + prop.getProperty(key.toString()));
            }
            System.out.println("--System.getenv--");
            Map<String, String> env = System.getenv();
            for(String item : env.values())
            {
                String key = item.toString();
                System.out.println(key + "=" + env.get(key.toString()));
            }

        }
    }
}
