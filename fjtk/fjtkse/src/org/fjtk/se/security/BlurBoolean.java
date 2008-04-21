package org.fjtk.se.security;

public final class BlurBoolean extends BlurObject<Boolean>
{
    public BlurBoolean(Boolean x, Boolean y)
    {
        super(x, y);
    }

    /**
     * 
     * @return
     */
    @Override
    public Boolean getValue()
    {
        return x ^ y;
    }
}
