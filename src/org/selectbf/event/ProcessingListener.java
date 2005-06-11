/*
 * Created on 27.03.2005
 * Created by Tim Adler
 */
package org.selectbf.event;

public interface ProcessingListener
{
    public void startProcessing();
    public void endProcessing(ProcessingEndEvent event);
}
