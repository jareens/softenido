package org.fjtk.se.security;

public class BlurInteger extends BlurObject<Integer>
{
    public BlurInteger(Integer x, Integer y)
    {
        super(x, y);
    }

    @Override
    public Integer getValue()
    {
        return x ^ y;
    }
}
