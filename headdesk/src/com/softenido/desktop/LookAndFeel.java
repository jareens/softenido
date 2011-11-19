/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.softenido.desktop;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author franci
 */
public class LookAndFeel 
{
    public static void setSystemLAF()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(LookAndFeel.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            Logger.getLogger(LookAndFeel.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            Logger.getLogger(LookAndFeel.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (UnsupportedLookAndFeelException ex)
        {
            Logger.getLogger(LookAndFeel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
