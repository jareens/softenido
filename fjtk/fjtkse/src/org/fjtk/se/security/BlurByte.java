package org.fjtk.se.security;

public final class BlurByte extends BlurObject<Byte>
{
    public BlurByte(Byte x, Byte y)
    {
        super(x, y);
    }

    @Override
    public Byte getValue()
    {
        return (byte) (x ^ y);
    }
}
