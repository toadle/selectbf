/*
 * Created on 02.04.2005
 * Created by Tim Adler
 */
package org.selectbf.config;

public class SelectBfConfigException extends Exception
{

    /**
     * 
     */
    public SelectBfConfigException()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0
     */
    public SelectBfConfigException(String arg0)
    {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    /**
     * Return true, if String is empty
     * 
     * @param string
     * @return
     */
    public static final boolean checkNullOrEmpty(String string)
    {
        if (string == null || string.trim().equals(""))
        {
            return true;
        }
        return false;
    }

    /**
     * Return true, if string is not a int
     * 
     * @param string
     * @return
     */
    public static final boolean checkInt(String string)
    {
        try
        {
            Integer.parseInt(string);
            return false;
        } catch (NumberFormatException ne)
        {
            return true;
        }
    }
    
    /**
     * Return true if the given value is not a valid boolean-value
     * 
     * @param string
     * @return
     */
    public static final boolean checkBoolean(String string)
    {
        return !Boolean.parseBoolean(string);
    }
}
