/*
 * Created on 27.03.2005
 * Created by Tim Adler
 */
package org.selectbf.event;

public class ProcessingEndEvent
{
    private Exception ex;
    
    public ProcessingEndEvent()
    {
        //nothing to do, ending without Exception
    }
    
    public ProcessingEndEvent(Exception e)
    {
        ex = e;
    }
    
    public boolean isNormalEnd()
    {
        return ex == null ? true : false;
    }
    
    public Exception getException()
    {
        return ex;
    }
}
