
package org.selectbf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


public class DatabaseCacher
{
	Connection dbConnection;
	
	public DatabaseCacher(SelectBfConfig CONFIG) throws SQLException
	{
			DriverManager.registerDriver (new org.gjt.mm.mysql.Driver());
			dbConnection = DriverManager.getConnection("jdbc:mysql://"+CONFIG.getDbMachine()+":"+CONFIG.getDbPort()+"/"+CONFIG.getDbName(),CONFIG.getDbUser(),CONFIG.getDbPassword());
	}
	
	public void close() throws SQLException
	{
		dbConnection.close();
	}

	public void cacheWeaponKills() throws SQLException
	{
		Date start = new Date();
		
		System.out.print("-> Caching 'Weapon Kills'");
		Statement s = dbConnection.createStatement();
		s.executeUpdate("TRUNCATE selectbf_cache_weaponkills");
		
		ResultSet res = s.executeQuery("select sum(times_used) count from selectbf_kills_weapon");
		res.next();
		int count = res.getInt("count");
		s.executeUpdate("INSERT INTO selectbf_cache_weaponkills (weapon,kills,percentage) select weapon, sum(times_used) count, (sum(times_used)*100/"+count+") from selectbf_kills_weapon group by weapon");
		
		Date ende = new Date();
		
		float f = ((float) (ende.getTime()-start.getTime()))/1000;
		
		System.out.println(" FINISHED ("+f+" sec)");
	}		

	public void cacheVehicleUsage() throws SQLException
	{
		Date start = new Date();
		
		System.out.print("-> Caching 'Vehicle Usage'");
		Statement s = dbConnection.createStatement();
		s.executeUpdate("TRUNCATE selectbf_cache_vehicletime");
		
        // SQL bug fixed by jrivett 2009-Feb-20
		// We want a total count of all drives, not just the number of records in the drives table. 
		// ResultSet res = s.executeQuery("select sum(drivetime) time,count(*) count from selectbf_drives");
		ResultSet res = s.executeQuery("select sum(drivetime) time, sum(times_used) count from selectbf_drives");
		res.next();
		// Bug fixed by jrivett 2009-Feb-25
		// Time is not an integer, it's a float.
		//int time = res.getInt("time");
		float time = res.getFloat("time");
		int count = res.getInt("count");
		// SQL bug fixed by jrivett 2009-Feb-25
		// As above, we want a total count of all drives for a vehicle, not the number of records.
		//s.executeUpdate("INSERT INTO selectbf_cache_vehicletime (vehicle,times_used,time,percentage_time,percentage_usage) select vehicle, count(*) count, sum(drivetime), (sum(drivetime)*100/"+time+"), (count(*)*100/"+count+") from selectbf_drives group by vehicle");
		s.executeUpdate("INSERT INTO selectbf_cache_vehicletime (vehicle,times_used,time,percentage_time,percentage_usage) select vehicle, sum(times_used) count, sum(drivetime), ((sum(drivetime)*100)/"+time+"), ((sum(times_used)*100)/"+count+") from selectbf_drives group by vehicle");
		
		Date ende = new Date();
		
		float f = ((float) (ende.getTime()-start.getTime()))/1000;
		
		System.out.println(" FINISHED ("+f+" sec)");
	}	
	
	public void cacheCharacterTypeUsage() throws SQLException
	{
		Date start = new Date();
		
		System.out.print("-> Caching 'Character-Type Usage'");
		Statement s = dbConnection.createStatement();
		s.executeUpdate("TRUNCATE selectbf_cache_chartypeusage");
		
        // SQL bug fixed by jrivett 2009-Feb-25
		// We want a total count of all character uses, not the number of records. 
		//ResultSet res = s.executeQuery("select count(*) count from selectbf_kits");
		ResultSet res = s.executeQuery("select sum(times_used) count from selectbf_kits");
		res.next();
		int count = res.getInt("count");
		// SQL bug fixed by jrivett 2009-Feb-25
		// As above, we want a total count of all character uses, not the number of records.
		//s.executeUpdate("INSERT INTO selectbf_cache_chartypeusage (kit,times_used,percentage) select k.kit, count(*) count,(count(*)*100.0/"+count+")  from selectbf_kits k group by k.kit");
		s.executeUpdate("INSERT INTO selectbf_cache_chartypeusage (kit,times_used,percentage) select k.kit, sum(times_used) count,(sum(times_used)*100.0/"+count+")  from selectbf_kits k group by k.kit");
		
		Date ende = new Date();
		
		float f = ((float) (ende.getTime()-start.getTime()))/1000;
		
		System.out.println(" FINISHED ("+f+" sec)");
	}
	
	public void cachePlayerRanking() throws SQLException
	{
		Date start = new Date();
		
		System.out.print("-> Caching 'Player Ranking'");		
		
		Statement s = dbConnection.createStatement();
		s.executeUpdate("TRUNCATE selectbf_cache_ranking");
		
		ResultSet rs = s.executeQuery("select p.name,ps.player_id id, sum(score) score ,sum(kills) kills ,sum(deaths) deaths ," +			
		    "sum(tks) tks ,sum(captures) captures,sum(defences) defences,sum(objectivetks) objectivetks ,sum(attacks) attacks ,sum(objectives) objectives,sum(heals) heals," +			"sum(selfheals) selfheals ,sum(repairs) repairs ,sum(otherrepairs) otherrepairs,count(*) rounds, " +			"sum(kills)/sum(deaths) kdrate, sum(captures) captures, sum(first) first, sum(second) second, sum(third) third " +			"from selectbf_playerstats ps, selectbf_players p where ps.player_id = p.id group by p.name,ps.player_id order by player_id ASC");
		
		Statement s2 = dbConnection.createStatement();
		ResultSet rs2 = s2.executeQuery("SELECT player_id,playtime time from selectbf_playtimes order by player_id ASC");
		rs2.next();
		
		int i = 1;
		while(rs.next())
		{
			int rank = i; 
			int player_id = rs.getInt("id"); 
			String playername = rs.getString("name");
			int	score = rs.getInt("score");
			int kills = rs.getInt("kills");
			int deaths = rs.getInt("deaths");
			int tks = rs.getInt("tks");
			int captures = rs.getInt("captures");
			int attacks = rs.getInt("attacks");
			int defences = rs.getInt("defences");
			int objectives = rs.getInt("objectives");
			int objectivetks = rs.getInt("objectivetks");
			int heals = rs.getInt("heals");
			int selfheals = rs.getInt("selfheals");
			int repairs = rs.getInt("repairs");
			int otherrepairs = rs.getInt("otherrepairs");
			int first = rs.getInt("first");
			int second = rs.getInt("second");
			int third = rs.getInt("third");
			int rounds_played = rs.getInt("rounds");
			double kdrate = rs.getDouble("kdrate");

			
			double score_per_minute = 0;
			
			PreparedStatement ps = dbConnection.prepareStatement("INSERT INTO selectbf_cache_ranking (rank, player_id, playername, score, kills, deaths, kdrate, score_per_minute, tks, captures, attacks, defences, objectives, objectivetks, heals, selfheals, repairs, otherrepairs, first, second, third, rounds_played) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			ps.setInt(1,rank);
			ps.setInt(2,player_id);
			ps.setString(3,playername);
			ps.setInt(4,score);
			ps.setInt(5,kills);
			ps.setInt(6,deaths);
			ps.setDouble(7,kdrate);
			ps.setDouble(8,score_per_minute);
			ps.setInt(9,tks);
			ps.setInt(10,captures);
			ps.setInt(11,attacks);
			ps.setInt(12,defences);
			ps.setInt(13,objectives);
			ps.setInt(14,objectivetks);
			ps.setInt(15,heals);
			ps.setInt(16,selfheals);
			ps.setInt(17,repairs);
			ps.setInt(18,otherrepairs);
			ps.setInt(19,first);
			ps.setInt(20,second);
			ps.setInt(21,third);
			ps.setInt(22,rounds_played);
			
			ps.execute();
					
			i++;	
		}	
		
		Date ende = new Date();
		float f = ((float) (ende.getTime()-start.getTime()))/1000;
		System.out.println(" FINISHED ("+f+" sec)");

	}
	
	public void cacheMapStats() throws SQLException
	{
		Date start = new Date();
		
		System.out.print("-> Caching 'Map Statistics'");	
		
		Statement s = dbConnection.createStatement();
		s.executeUpdate("TRUNCATE selectbf_cache_mapstats");
		
		ResultSet rs = s.executeQuery("SELECT DISTINCT map FROM selectbf_games order by map ASC");
		while(rs.next())
		{
			String map = rs.getString("map");
			
			//variables to collect for team1
			int wins_team1;
			int score_team1;
			int kills_team1;
			int deaths_team1;
			int attacks_team1;
			int captures_team1;
			float win_team1_tickets_team1;
			float win_team1_tickets_team2;
			
			//variable to collect for team1
			int wins_team2;
			int score_team2;
			int kills_team2;
			int deaths_team2;
			int attacks_team2;
			int captures_team2;
			float win_team2_tickets_team1;
			float win_team2_tickets_team2;
			
			PreparedStatement ps = dbConnection.prepareStatement("select count(*) wins, avg(end_tickets_team1) end_tickets_team1, avg(end_tickets_team2) end_tickets_team2 from selectbf_games g, selectbf_rounds r where g.id = r.game_id and map = ? and r.winning_team = 1 group by map, winning_team ");
			ps.setString(1,map);
			ResultSet rs2 = ps.executeQuery();
			

			if(rs2.next())
			{
				wins_team1 = rs2.getInt("wins");
				win_team1_tickets_team1 = rs2.getFloat("end_tickets_team1");
				win_team1_tickets_team2 = rs2.getFloat("end_tickets_team2");
			}
			else
			{
				wins_team1 = 0;
				win_team1_tickets_team1 = 0;
				win_team1_tickets_team2 = 0;
			}
			
			
			ps = dbConnection.prepareStatement("select count(*) wins, avg(end_tickets_team1) end_tickets_team1, avg(end_tickets_team2) end_tickets_team2 from selectbf_games g, selectbf_rounds r where g.id = r.game_id and map = ? and r.winning_team = 2 group by map, winning_team ");
			ps.setString(1,map);
			rs2 = ps.executeQuery();
			
			if(rs2.next())
			{
				wins_team2 = rs2.getInt("wins");
				win_team2_tickets_team1 = rs2.getFloat("end_tickets_team1");
				win_team2_tickets_team2 = rs2.getFloat("end_tickets_team2");
			}
			else
			{
				wins_team2 = 0;
				win_team2_tickets_team1 = 0;
				win_team2_tickets_team2 = 0;
			}			
			
			ps = dbConnection.prepareStatement("select sum(score) score, sum(deaths) deaths, sum(kills) kills, sum(attacks) attacks, sum(captures) captures from selectbf_playerstats p, selectbf_rounds r, selectbf_games g where p.round_id = r.id and r.game_id = g.id and p.team = 1 and g.map = ? group by p.team");
			ps.setString(1,map);
			rs2 = ps.executeQuery();
			if(rs2.next())
			{
				score_team1 = rs2.getInt("score");
				deaths_team1 = rs2.getInt("deaths");
				kills_team1 = rs2.getInt("kills");
				attacks_team1 = rs2.getInt("attacks");
				captures_team1 = rs2.getInt("captures");
			}
			else
			{
				score_team1 = 0;
				deaths_team1 = 0;
				kills_team1 = 0;
				attacks_team1 = 0;	
				captures_team1 = 0;			
			}
			
			ps = dbConnection.prepareStatement("select sum(score) score, sum(deaths) deaths, sum(kills) kills, sum(attacks) attacks, sum(captures) captures from selectbf_playerstats p, selectbf_rounds r, selectbf_games g where p.round_id = r.id and r.game_id = g.id and p.team = 2 and g.map = ? group by p.team");
			ps.setString(1,map);
			rs2 = ps.executeQuery();
			if(rs2.next())
			{
				score_team2 = rs2.getInt("score");
				deaths_team2 = rs2.getInt("deaths");
				kills_team2 = rs2.getInt("kills");
				attacks_team2 = rs2.getInt("attacks");
				captures_team2 = rs2.getInt("captures");
			}
			else
			{
				score_team2 = 0;
				deaths_team2 = 0;
				kills_team2 = 0;
				attacks_team2 = 0;
				captures_team2 = 0;				
			}			
			
			ps = dbConnection.prepareStatement("INSERT INTO selectbf_cache_mapstats (map, wins_team1, wins_team2, win_team1_tickets_team1, win_team1_tickets_team2, win_team2_tickets_team1, win_team2_tickets_team2, score_team1, score_team2, kills_team1, kills_team2, deaths_team1, deaths_team2, attacks_team1, attacks_team2, captures_team1, captures_team2) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			ps.setString(1,map);
			ps.setInt(2,wins_team1);
			ps.setInt(3,wins_team2);
			ps.setFloat(4,win_team1_tickets_team1);
			ps.setFloat(5,win_team1_tickets_team2);
			ps.setFloat(6,win_team2_tickets_team1);
			ps.setFloat(7,win_team2_tickets_team2);
			ps.setInt(8,score_team1);
			ps.setInt(9,score_team2);
			ps.setInt(10,kills_team1);
			ps.setInt(11,kills_team2);
			ps.setInt(12,deaths_team1);
			ps.setInt(13,deaths_team2);
			ps.setInt(14,attacks_team1);
			ps.setInt(15,attacks_team2);
			ps.setInt(16,captures_team1);
			ps.setInt(17,captures_team2);
			
			ps.execute();
			ps.close();			
		}
		
		Date ende = new Date();
		float f = ((float) (ende.getTime()-start.getTime()))/1000;
		System.out.println(" FINISHED ("+f+" sec)");
	}
}
