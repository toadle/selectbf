/*
 * Created on 27.03.2005
 * Created by Tim Adler
 */
package org.selectbf.event;

import java.io.File;

public class FileStatusEvent
{
    public static final int STATUS_DECOMPRESSING = 5;
    public static final int STATUS_CONSISTENCY_CHECKING = 4;
    public static final int STATUS_PARSING_XML = 1;
    public static final int STATUS_PROCESSING_DATA = 2;
    public static final int STATUS_PERSISTING = 3;

    private int status;
    private File file;

    public FileStatusEvent(File file, int status)
    {
        this.file = file;
        this.status = status;
    }

    public int getStatus()
    {
        return status;
    }

    public File getFile()
    {
        return file;
    }

}
