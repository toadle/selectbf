/*
 * Created on 28.03.2005
 * Created by Tim Adler
 */
package org.selectbf.event;

public class DownloadEvent
{
    private String filename;
    private long size;
    private int number;
    private int total;

    /**
     * @param filename
     * @param size
     * @param number
     * @param total
     */
    public DownloadEvent(String filename, long size, int number, int total)
    {
        super();
        // TODO Auto-generated constructor stub
        this.filename = filename;
        this.size = size;
        this.number = number;
        this.total = total;
    }

    public String getFilename()
    {
        return filename;
    }

    public long getSize()
    {
        return size;
    }

    public int getNumber()
    {
        return number;
    }

    public int getTotal()
    {
        return total;
    }

}
