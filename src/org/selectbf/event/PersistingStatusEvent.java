/*
 * Created on 28.03.2005
 * Created by Tim Adler
 */
package org.selectbf.event;

public class PersistingStatusEvent
{
    public static final int STATUS_ROUNDINFO = 1;
    public static final int STATUS_SCORES = 2;
    public static final int STATUS_HOSPITAL = 3;
    public static final int STATUS_PITSTOP = 4;
    public static final int STATUS_PLAYERSTATS = 5;
    public static final int STATUS_HIGHWAY = 6;
    public static final int STATUS_KITS = 7;
    public static final int STATUS_CHAT = 8;

    private int round;
    private int status;

    public PersistingStatusEvent(int roundNumber, int status)
    {
        this.round = roundNumber;
        this.status = status;
    }

    public int getRound()
    {
        return round;
    }

    public int getStatus()
    {
        return status;
    }

}
