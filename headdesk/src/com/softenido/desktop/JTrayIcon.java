/*
 *  JTrayIcon.java
 *
 *  Copyright (C) 2010 Francisco Gómez Carrasco
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
package com.softenido.desktop;

import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 *
 * @author franci
 */
public class JTrayIcon extends TrayIcon
{

    final JPopupMenu popup;

    public JTrayIcon(Image image, String tooltip, JPopupMenu popup)
    {
        super(image, tooltip, null);
        this.popup = popup;
        if(popup!=null)
        {
            addPopup(popup);
        }
    }

    public JTrayIcon(Image image, String tooltip, PopupMenu popup)
    {
        super(image, tooltip, popup);
        this.popup = null;
    }

    public JTrayIcon(Image image, String tooltip)
    {
        super(image, tooltip);
        this.popup = null;
    }

    public JTrayIcon(Image image)
    {
        super(image);
        this.popup = null;
    }

    private void addPopup(final JPopupMenu popup)
    {
        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (popup.isPopupTrigger(e))
                {
                    popup.setLocation(e.getX(), e.getY());
                    popup.setInvoker(popup);
                    popup.setVisible(true);
                }
            }
        });
        popup.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e)
            {
                System.out.println("popupMenuWillBecomeVisible");
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
            {
                System.out.println("popupMenuWillBecomeInvisible");
                //popup.setInvoker(null);
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e)
            {
                System.out.println("popupMenuCanceled");
            }
        });
    }  
}
