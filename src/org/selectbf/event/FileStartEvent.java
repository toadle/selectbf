/*
 * Created on 27.03.2005
 * Created by Tim Adler
 */
package org.selectbf.event;

import java.io.File;

public class FileStartEvent
{
    private File file;
    private int fileNumber;
    private int totalFiles;

    /**
     * @param file
     * @param fileNumber
     * @param totalFiles
     */
    public FileStartEvent(File file, int fileNumber, int totalFiles)
    {
        super();
        // TODO Auto-generated constructor stub
        this.file = file;
        this.fileNumber = fileNumber;
        this.totalFiles = totalFiles;
    }

    public File getFile()
    {
        return file;
    }

    public int getFileNumber()
    {
        return fileNumber;
    }

    public int getTotalFiles()
    {
        return totalFiles;
    }

}
