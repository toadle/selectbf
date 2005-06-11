/*
 * Created on 27.03.2005
 * Created by Tim Adler
 */
package org.selectbf.event;

public interface FileEventListener
{
    public void startFileProcessing(FileStartEvent event);
    public void fileProcessingStatusChanged(FileStatusEvent event);
    public void endFileProcessing(FileEndEvent event);
}
