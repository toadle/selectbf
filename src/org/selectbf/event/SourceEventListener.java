/*
 * Created on 27.03.2005
 * Created by Tim Adler
 */
package org.selectbf.event;

public interface SourceEventListener
{
    public void beginSourceProcessing(SourceEvent se);
    public void endSourceProcessing(SourceEvent se);    
}
