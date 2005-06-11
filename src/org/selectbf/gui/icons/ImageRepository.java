package org.selectbf.gui.icons;

/*
 /*
 * Some code copied from the Azureus project.
 * Thx for being open source guys :)!
 *
 */
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.selectbf.SelectBf;

public class ImageRepository
{
    private static Logger log = Logger.getLogger(ImageRepository.class);

    private static final HashMap images;

    static
    {
        images = new HashMap();
    }

    public static void loadImages(Display display)
    {
        Properties p = new Properties();
        try
        {
            p.load(ImageRepository.class.getResourceAsStream("imageRegistry.properties"));
        }
        catch (IOException ioex)
        {
            log.warn("Couldn't load load the ImageRegistry!");
        }

        String prefix = p.getProperty("prefix");

        for (Iterator i = p.keySet().iterator(); i.hasNext();)
        {
            Object key = i.next();
            if (!key.equals("prefix"))
            {
                Object value = p.get(key);
                loadImage(display, prefix + ((String) value), (String) key);
            }
        }
    }

    public static Image loadImage(Display display, String res, String name)
    {
        return loadImage(display, res, name, 255);
    }

    public static Image loadImage(Display display, String res, String name, int alpha)
    {
        return loadImage(ImageRepository.class.getClassLoader(), display, res, name, alpha);
    }

    public static Image loadImage(ClassLoader loader, Display display, String res, String name, int alpha)
    {
        Image im = getImage(name);
        if (null == im)
        {
            InputStream is = loader.getResourceAsStream(res);
            if (null != is)
            {
                if (alpha == 255)
                {
                    im = new Image(display, is);
                } else
                {
                    ImageData icone = new ImageData(is);
                    icone.alpha = alpha;
                    im = new Image(display, icone);
                }
                images.put(name, im);
            } else
            {
                log.warn("ImageRepository:loadImage:: Resource not found: " + res);
            }
        }
        return im;
    }

    public static void unLoadImages()
    {
        Iterator iter;
        iter = images.values().iterator();
        while (iter.hasNext())
        {
            Image im = (Image) iter.next();
            im.dispose();
        }
    }

    public static Image getImage(String name)
    {
        return (Image) images.get(name);
    }
}