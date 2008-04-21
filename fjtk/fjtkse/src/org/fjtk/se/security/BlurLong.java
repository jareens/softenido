package org.fjtk.se.security;

public class BlurLong extends BlurObject<Long>
{
    public BlurLong(Long x, Long y)
    {
        super(x, y);
    }

    @Override
    public Long getValue()
    {
        return x ^ y;
    }
}
