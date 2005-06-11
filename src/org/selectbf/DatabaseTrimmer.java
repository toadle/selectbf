
package org.selectbf;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Vector;


public class DatabaseTrimmer
{
	Connection dbConnection;
	
	int days;
	boolean removePlayers;
	
	int gamesDeleted = 0;
	int roundsDeleted = 0;
	int playersDeleted = 0;
	
	char lastIndicator = '-';
	
	
	public DatabaseTrimmer(SelectBfConfig CONFIG) throws SQLException
	{

			DriverManager.registerDriver (new org.gjt.mm.mysql.Driver());
			dbConnection = DriverManager.getConnection("jdbc:mysql://"+CONFIG.getDbMachine()+":"+CONFIG.getDbPort()+"/"+CONFIG.getDbName(),CONFIG.getDbUser(),CONFIG.getDbPassword());
			
			this.days = CONFIG.getTrimDays();
			this.removePlayers = !CONFIG.isTrimPlayers();
	}
	
	public void trim() throws SQLException
	{
		
		Calendar aLongTimeAgo = Calendar.getInstance();
		aLongTimeAgo.setTimeInMillis(System.currentTimeMillis());
		aLongTimeAgo.add(Calendar.DAY_OF_MONTH,(days*(-1)));
		
		Date d = new Date(aLongTimeAgo.getTimeInMillis());
		
		//first delete expired games
		PreparedStatement ps = dbConnection.prepareStatement("Select id from selectbf_games where starttime < ?");
		ps.setTimestamp(1, new Timestamp(d.getTime()));
		
		ResultSet rs = ps.executeQuery();	
		
		while(rs.next())
		{
			int game_id = rs.getInt("id");
			
			deleteGame(game_id);
			gamesDeleted++;
		}
		
		if(removePlayers)
		{
			ps = dbConnection.prepareStatement("Select distinct player_id from selectbf_playtimes where last_seen < ?");
			ps.setTimestamp(1, new Timestamp(d.getTime()));
			
			rs = ps.executeQuery();
			Vector vec = new Vector();
			while(rs.next())
			{
				int player_id = rs.getInt("player_id");
				
				
				System.out.println("Player "+player_id+" has been off too long and is no longer monitored");
				
				Statement s = dbConnection.createStatement();
				s.executeUpdate("DELETE FROM selectbf_chatlog where player_id = "+player_id);
				s.executeUpdate("DELETE FROM selectbf_players where id = "+player_id);
				s.executeUpdate("DELETE FROM selectbf_playtimes where player_id = "+player_id);
				s.executeUpdate("DELETE FROM selectbf_drives where player_id = "+player_id);
				s.executeUpdate("DELETE FROM selectbf_heals where player_id = "+player_id+" or healed_player_id = "+player_id);
				s.executeUpdate("DELETE FROM selectbf_kills_player where player_id = "+player_id+" or victim_id = "+player_id);
				s.executeUpdate("DELETE FROM selectbf_kills_weapon where player_id = "+player_id);
				s.executeUpdate("DELETE FROM selectbf_kits where player_id = "+player_id);
				s.executeUpdate("DELETE FROM selectbf_playerstats where player_id = "+player_id);
				s.executeUpdate("DELETE FROM selectbf_repairs where player_id = "+player_id);
				s.executeUpdate("DELETE FROM selectbf_selfkills where player_id = "+player_id);
				s.executeUpdate("DELETE FROM selectbf_tks where player_id = "+player_id+" or victim_id = "+player_id);
				s.close();
				playersDeleted++;
			}
		}
		
		//then delete expired playtimes
		ps = dbConnection.prepareStatement("DELETE FROM selectbf_playtimes where last_seen < ?");
		ps.setTimestamp(1, new Timestamp(d.getTime()));
		ps.execute();
		
		//now let MySQL optimize the tables
		Statement s = dbConnection.createStatement();
		s.executeUpdate("OPTIMIZE TABLE selectbf_players");
		s.executeUpdate("OPTIMIZE TABLE selectbf_playtimes");
		s.executeUpdate("OPTIMIZE TABLE selectbf_attacks");
		s.executeUpdate("OPTIMIZE TABLE selectbf_deaths");
		s.executeUpdate("OPTIMIZE TABLE selectbf_drives");
		s.executeUpdate("OPTIMIZE TABLE selectbf_heals");
		s.executeUpdate("OPTIMIZE TABLE selectbf_kills");
		s.executeUpdate("OPTIMIZE TABLE selectbf_kits");
		s.executeUpdate("OPTIMIZE TABLE selectbf_playerstats");
		s.executeUpdate("OPTIMIZE TABLE selectbf_repairs");
		s.executeUpdate("OPTIMIZE TABLE selectbf_selfkills");
		s.executeUpdate("OPTIMIZE TABLE selectbf_tks");
		s.close();
		
	}
	
	public void close() throws SQLException
	{
		dbConnection.close();
	}
	
	public String toString()
	{
		String str = "Result of Trimming Database to "+days+" Days:"
					+"\nGames removed: "+gamesDeleted
					+"\nRounds removed: "+roundsDeleted;
		if(removePlayers)
		{
			str = str+"\nPlayers removed: "+playersDeleted;
		}
		
		return str;
	}
	
	private void deleteGame(int id) throws SQLException
	{
		System.out.println("Game "+id+" is outdated and is being removed!");
		PreparedStatement ps = dbConnection.prepareStatement("Select id from selectbf_rounds where game_id = ?");
		ps.setInt(1,id);
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next())
		{
			int round_id = rs.getInt("id");
			deleteRound(round_id);
			roundsDeleted++;
		}
		
		ps.close();
		
		Statement s = dbConnection.createStatement();
		s.executeUpdate("DELETE FROM selectbf_games WHERE id="+id);
		s.close();
	}
	
	private void deleteRound(int id) throws SQLException
	{
		System.out.println("-> delete Round "+id+" along with all contained events");
		Statement s = dbConnection.createStatement();
		
		s.executeUpdate("DELETE FROM selectbf_chatlog WHERE round_id="+id);
		s.executeUpdate("DELETE FROM selectbf_playerstats WHERE round_id="+id);
		s.executeUpdate("DELETE FROM selectbf_rounds WHERE id="+id);
		s.close();
	}
}
