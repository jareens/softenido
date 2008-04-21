/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fjtk.se.security;

import java.security.SecureRandom;

/**
 * 
 * @author franci
 */
public class BlurTool
{
    private static final SecureRandom random = new SecureRandom();;

    public static SecureRandom getRandom()
    {
        return random;
    }

    public static BlurBoolean buildBoolean(boolean val)
    {
        boolean X = getRandom().nextBoolean();
        boolean Y = X ^ val;
        return new BlurBoolean(X, Y);
    }

    public static BlurBoolean[] buildBoolean(boolean val[])
    {
        BlurBoolean items[] = new BlurBoolean[val.length];
        for (int i = 0; i < val.length; i++)
        {
            items[i] = buildBoolean(val[i]);
        }
        return items;
    }

    public static BlurByte buildByte(byte val)
    {
        byte X = (byte) getRandom().nextInt();
        byte Y = (byte) (X ^ val);
        return new BlurByte(X, Y);
    }

    public static BlurByte[] buildByte(byte val[])
    {
        BlurByte items[] = new BlurByte[val.length];
        for (int i = 0; i < val.length; i++)
        {
            items[i] = buildByte(val[i]);
        }
        return items;
    }

    public static BlurCharacter buildCharacter(char val)
    {
        char X = (char) getRandom().nextInt();
        char Y = (char) (X ^ val);
        return new BlurCharacter(X, Y);
    }

    public static BlurCharacter[] buildCharacter(char val[])
    {
        BlurCharacter items[] = new BlurCharacter[val.length];
        for (int i = 0; i < val.length; i++)
        {
            items[i] = buildCharacter(val[i]);
        }
        return items;
    }

    public static BlurInteger buildInteger(int val)
    {
        int x = getRandom().nextInt();
        int y = (x ^ val);
        return new BlurInteger(x, y);
    }

    public static BlurInteger[] buildInteger(int val[])
    {
        BlurInteger items[] = new BlurInteger[val.length];
        for (int i = 0; i < val.length; i++)
        {
            items[i] = buildInteger(val[i]);
        }
        return items;
    }

    public static BlurLong buildLong(long val)
    {
        long x = getRandom().nextLong();
        long y = (x ^ val);
        return new BlurLong(x, y);
    }

    public static BlurLong[] buildLong(long val[])
    {
        BlurLong items[] = new BlurLong[val.length];
        for (int i = 0; i < val.length; i++)
        {
            items[i] = buildLong(val[i]);
        }
        return items;
    }
}
