/*
 * Created on 27.03.2005
 * Created by Tim Adler
 */
package org.selectbf.event;

import java.io.File;

public class FileEndEvent extends FileStartEvent
{
    private Exception exception;
    
    public FileEndEvent(File file, int fileNumber, int totalFiles)
    {
        super(file, fileNumber, totalFiles);
    }
    
    public FileEndEvent(File file, int fileNumber, int totalFiles, Exception exception)
    {
        super(file, fileNumber, totalFiles);
        this.exception = exception;
    }

    public boolean isNormalEnd()
    {
        return exception == null ? true : false;
    }
    
    public Exception getException()
    {
        return exception;
    }
}
