package org.selectbf;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;
import org.selectbf.event.PersistingStatusEvent;
import org.selectbf.event.PersistingStatusListener;
import org.selectbf.event.ProgressListener;

public class GameContext extends SelectBfClassBase
{
    private ProgressListener persistingProgressListener;
    private PersistingStatusListener persistingStatusListener;

    private Date starttime;
    private Vector rounds;
    private ServerInfoManagementBase simb;
    private PlayerManagementBase pmb;
    private SelectBfConfig CONFIG;

    int cancelRoundsCount = 0;
    private int persistedRound = 0;

    public GameContext(Date d, Element root, SelectBfConfig CONFIG, ProgressListener eventProcessProgressListener) throws SelectBfException
    {
        super(root.getNamespace());

        starttime = d;
        this.CONFIG = CONFIG;

        pmb = new PlayerManagementBase(CONFIG.isLogBots(), CONFIG.isLanMode(), this, NAMESPACE);

        rounds = new Vector();

        List roundXML = root.getChildren("round", NAMESPACE);

        int i = 0;
        for (Iterator it = roundXML.iterator(); it.hasNext();)
        {
            try
            {
                Element e = (Element) it.next();

                RoundContext rc = new RoundContext(NAMESPACE, e, this, pmb, eventProcessProgressListener);
                rounds.add(rc);

                if (i == 0)
                {
                    simb = new ServerInfoManagementBase(e.getChild("server", NAMESPACE), NAMESPACE);
                }
                i++;
            } catch (CancelProcessException ce)
            {
                cancelRoundsCount++;
            }
        }

        if (cancelRoundsCount == roundXML.size())
        {
            throw new CancelProcessException("The whole Game was canceled because no Round contained information worth noting.");
        }
    }

    public Date calcTimeFromDiffString(String sec)
    {
        double d = Double.parseDouble(sec);

        Calendar c = Calendar.getInstance();
        c.setTime(starttime);

        c.add(Calendar.SECOND, ((int) d));
        return new Date(c.getTimeInMillis());
    }

    public Date getStarttime()
    {
        return starttime;
    }

    public ServerInfoManagementBase getSimb()
    {
        return simb;
    }

    public void persist(DatabaseContext dc) throws SQLException, SelectBfException
    {
        // first persist game-infos
        PreparedStatement ps = dc
                .prepareStatement("INSERT INTO selectbf_games (servername, modid, mapid, map, game_mode, gametime, maxplayers, scorelimit, spawntime, soldierff, vehicleff, tkpunish, deathcamtype, starttime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setString(1, simb.getServer_name());
        ps.setString(2, simb.getModid());
        ps.setString(3, simb.getMapid());
        ps.setString(4, simb.getMap());
        ps.setString(5, simb.getGame_mode());
        ps.setInt(6, simb.getGametime());
        ps.setInt(7, simb.getMaxplayers());
        ps.setInt(8, simb.getScorelimit());
        ps.setInt(9, simb.getSpawntime());
        ps.setInt(10, simb.getSoldierff());
        ps.setInt(11, simb.getVehicleff());
        ps.setInt(12, simb.getTkpunish());
        ps.setInt(13, simb.getDeathcamtype());
        ps.setTimestamp(14, new Timestamp(getStarttime().getTime()));

        // ps.setString(14,dc.toAddableDateString(getStarttime()));
        ps.execute();

        // then get this games Database Id
        int gameId = dc.getLatestId(DatabaseContext.GAMES);

        Date gameendtime = ((RoundContext) rounds.elementAt(rounds.size() - 1)).getEndtime();
        pmb.closeAllSlots(gameendtime);
        pmb.persist(dc);

        // now persist the rounds
        for (int i = 0; i < rounds.size(); i++)
        {
            try
            {
                RoundContext rc = (RoundContext) rounds.elementAt(i);
                rc.addPersistingProgressListener(persistingProgressListener);

                persistedRound = i+1;
                
                if (!(CONFIG.isSkipEmptyRounds() && rc.isEmpty()))
                {
                    rc.persist(dc, gameId);
                }
            } catch (SelectBfException se)
            {
                if (se.getType() == SelectBfException.ROUND_NOT_STARTED || se.getType() == SelectBfException.ROUND_NOT_ENDED)
                {
                    // that means skip this round
                    SelectBfExceptionCounter.registerSelectBfException(se);
                } else
                {
                    throw se;
                }
            }
        }
    }

    public Vector getRounds()
    {
        return rounds;
    }

    public boolean isEmpty()
    {
        boolean empty = true;
        for (Iterator i = rounds.iterator(); i.hasNext() && empty;)
        {
            RoundContext rc = (RoundContext) i.next();
            empty = rc.isEmpty();
        }
        return empty;
    }

    public void addPersistingProgressListener(ProgressListener listener)
    {
        this.persistingProgressListener = listener;
    }

    public void addPersistingStatusListener(PersistingStatusListener listener)
    {
        this.persistingStatusListener = listener;
    }
    
    public void firePersistingStatusEvent(int status)
    {
        if(persistingStatusListener != null)
        {
            persistingStatusListener.persistingStatusChanged(new PersistingStatusEvent(persistedRound,status));
        }
    }
}
