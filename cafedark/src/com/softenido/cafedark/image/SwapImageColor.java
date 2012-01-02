/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softenido.cafedark.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;



abstract class SwapColor
{
    final double ratio = 0.53; 
    final String rgb;
    public SwapColor(String rgb)
    {
        this.rgb = rgb;
    }
    
    int swap(int c)
    {
        Color color = new Color(c);
        color = swap(color);
        return color.getRGB();
    }
    abstract Color swap(Color c);
    String getRGB()
    {
        return rgb;
    }
    static final SwapColor[] OPS=
    {
        new SwapColorRGB(),
        new SwapColorGBR(),
        new SwapColorBRG(),
        new SwapColorRG(),
        new SwapColorRB(),
        new SwapColorGB(),
        new SwapColorR(),
        new SwapColorG(),
        new SwapColorB()
    };
            
    static SwapColor build(int op)
    {
        if(op>=0&&op<OPS.length)
        {
            return OPS[op];
        }
        return null;
    }
}

class SwapColorRGB extends SwapColor
{

    public SwapColorRGB()
    {
        super("RGB");
    }
    
    @Override
    int swap(int c)
    {
        return c;
    }
    @Override
    Color swap(Color c)
    {
        return c;
    }
}

    
class SwapColorGBR extends SwapColor
{

    public SwapColorGBR()
    {
        super("GBR");
    }
    @Override
    Color swap(Color c)
    {
        return new Color(c.getGreen(),c.getBlue(),c.getRed(),c.getAlpha());
    }
}
    
    
class SwapColorBRG extends SwapColor
{

    public SwapColorBRG()
    {
        super("BRG");
    }
    @Override
    Color swap(Color c)
    {
        return new Color(c.getBlue(),c.getRed(),c.getGreen(),c.getAlpha());
    }
}
    
    
class SwapColorRG extends SwapColor
{
    public SwapColorRG()
    {
        super("RG");
    }
    @Override
    Color swap(Color c)
    {
        return new Color(c.getGreen(),c.getRed(),c.getBlue(),c.getAlpha());
    }
}
    

class SwapColorRB extends SwapColor
{
    public SwapColorRB()
    {
        super("RB");
    }
    
    @Override
    Color swap(Color c)
    {
        return new Color(c.getBlue(),c.getGreen(),c.getRed(),c.getAlpha());
    }
}
class SwapColorGB extends SwapColor
{
    public SwapColorGB()
    {
        super("GB");
    }
    
    @Override
    Color swap(Color c)
    {
        return new Color(c.getRed(),c.getBlue(),c.getGreen(),c.getAlpha());
    }
}

class SwapColorR extends SwapColor
{
    public SwapColorR()
    {
        super("R");
    }
    
    @Override
    Color swap(Color c)
    {
        int r = (int) Math.min(255,c.getRed());
        int g = (int) (c.getGreen()*ratio);
        int b = (int) (c.getBlue()*ratio);
        return new Color(r,g,b,c.getAlpha());
    }
}
class SwapColorG extends SwapColor
{
    public SwapColorG()
    {
        super("G");
    }
    
    @Override
    Color swap(Color c)
    {
        int r = (int) (c.getRed()*ratio);
        int g = (int) Math.min(255,c.getGreen());
        int b = (int) (c.getBlue()*ratio);
        return new Color(r,g,b,c.getAlpha());
    }
}
class SwapColorB extends SwapColor
{
    public SwapColorB()
    {
        super("B");
    }
    
    @Override
    Color swap(Color c)
    {
        int r = (int) (c.getRed()*ratio);
        int g = (int) (c.getGreen()*ratio);
        int b = (int) Math.min(255,c.getBlue());
        return new Color(r,g,b,c.getAlpha());
    }
}

/**
 *
 * @author franci
 */
public class SwapImageColor implements Runnable
{
    //no operation
    static public final int RGB=0;
    //rotate
    static public final int GBR=1;
    static public final int BRG=2;
    //swap
    static public final int RG=3;
    static public final int RB=4;
    static public final int GB=5;
    //
    static public final int R=6;
    static public final int G=7;
    static public final int B=8;
    
    static public final int MIN=0;
    static public final int MAX=8;
    
    
    final BufferedImage src;
    final BufferedImage dst;
    final int op;
    final AtomicBoolean done= new AtomicBoolean(false);
    final SwapColor swap;

    public SwapImageColor(BufferedImage src, BufferedImage dst, int op)
    {
        this.src = src;
        this.dst = dst;
        this.op  = op;
        this.swap= SwapColor.build(op);
    }

    public void run()
    {
        done.set(false);
        int w = src.getWidth();
        int h = src.getHeight();
        int t = src.getType();
        
        for(int y=0; y<h;y++)
            for(int x=0; x<w;x++)
                dst.setRGB(x, y, swap.swap(dst.getRGB(x,y)));
    
        done.set(true);
    }

    public String getRGB()
    {
        return swap.getRGB();
    }
}
