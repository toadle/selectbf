/*
 * Created on 28.03.2005
 * Created by Tim Adler
 */
package org.selectbf.event;

public interface DownloadListener
{
    public void beginDownload(DownloadEvent event);
    public void endDownload(DownloadEvent event);    
}
