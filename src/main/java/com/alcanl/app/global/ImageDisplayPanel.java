package com.alcanl.app.global;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageDisplayPanel extends JPanel
{
    private static final Object BACKGROUND_LOCK = new Object();
    private BufferedImage background;
    public ImageDisplayPanel () throws HeadlessException
    {
        this.setDoubleBuffered(true);
    }
    public void setBackground (Image newBackground)
    {
        synchronized (BACKGROUND_LOCK)
        {
            if (background == null)
                background = new BufferedImage(newBackground.getWidth(null), newBackground.getHeight(null), BufferedImage.TYPE_INT_RGB);

            else if (background.getWidth() != newBackground.getWidth(null) || background.getHeight() != newBackground.getHeight(null))
            {
                background.flush();
                background = new BufferedImage(newBackground.getWidth(null), newBackground.getHeight(null), BufferedImage.TYPE_INT_RGB);
            }
            var graphics = background.createGraphics();
            graphics.drawImage(newBackground, 0, 0, null);
        }
        repaint();
    }
    @Override
    public void paint (Graphics g)
    {
        super.paint(g);
        synchronized (BACKGROUND_LOCK)
        {
            if (background != null)
            {
                g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
            }
        }
    }
}