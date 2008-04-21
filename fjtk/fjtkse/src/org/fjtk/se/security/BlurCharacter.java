package org.fjtk.se.security;

public class BlurCharacter extends BlurObject<Character>
{
    public BlurCharacter(Character x, Character y)
    {
        super(x, y);
    }

    @Override
    public Character getValue()
    {
        return (char) (x ^ y);
    }
}
