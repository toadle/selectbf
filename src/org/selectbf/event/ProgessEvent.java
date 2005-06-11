/*
 * Created on 27.03.2005
 * Created by Tim Adler
 */
package org.selectbf.event;

public class ProgessEvent
{
    private int number;
    private int total;

    /**
     * @param number
     * @param total
     */
    public ProgessEvent(int number, int total)
    {
        super();
        // TODO Auto-generated constructor stub
        this.number = number;
        this.total = total;
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
