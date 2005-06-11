/*
 * Created on 08.04.2005
 * Created by Tim Adler
 */
package org.selectbf.gui.common;

public class UserMessage
{
    private String text;
    private String message;
    private int icon;

    /**
     * @param text
     * @param message
     * @param icon
     */
    public UserMessage(String text, String message, int icon)
    {
        super();
        // TODO Auto-generated constructor stub
        this.text = text;
        this.message = message;
        this.icon = icon;
    }

    public int getIcon()
    {
        return icon;
    }

    public String getMessage()
    {
        return message;
    }

    public String getText()
    {
        return text;
    }

}
