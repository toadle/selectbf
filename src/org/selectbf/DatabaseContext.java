
package org.selectbf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;



public class DatabaseContext
{
	Connection dbConnection;
	
	public static final int GAMES = 1;
	public static final int ROUNDS = 2;
	public static final int PLAYERS = 3;
	
	public DatabaseContext(SelectBfConfig CONFIG) throws SQLException
	{

			DriverManager.registerDriver (new com.mysql.jdbc.Driver());
			dbConnection = DriverManager.getConnection("jdbc:mysql://"+CONFIG.getDbMachine()+":"+CONFIG.getDbPort()+"/"+CONFIG.getDbName(),CONFIG.getDbUser(),CONFIG.getDbPassword());

	}
	
	public PreparedStatement prepareStatement(String sql) throws SQLException
	{
		return dbConnection.prepareStatement(sql);
	}
	
	private Statement createStatement() throws SQLException
	{
		return dbConnection.createStatement();
	}
	
	public Vector query(String sql) throws SQLException
	{
		Vector vec = new Vector();
		HashMap hash = null;
		boolean containsRows = false;
		
		//Query ausführen
		//out("Sending SQL to Database \""+sql+"\"");

		Statement stat = createStatement();
		ResultSet ergebnisse = stat.executeQuery(sql);
		ResultSetMetaData MetaData = ergebnisse.getMetaData();
		//out("Result---------------------------------------");
		while(ergebnisse.next())
		{
			containsRows = true;
			hash = new HashMap();
			for(int i = 1; i<=MetaData.getColumnCount(); i++)
			{
				//out(i+" "+MetaData.getColumnName(i)+" "+ergebnisse.getString(i));
				if(ergebnisse.getString(i)!=null)
				{
					hash.put(MetaData.getColumnName(i),ergebnisse.getString(i));
				}
				else
				{
					hash.put(MetaData.getColumnName(i),"");
				}
			}
			//out("=============================================");
			vec.add(hash);
		}
		//out("---------------------------------------------");
		
		if(containsRows)
		{
			return vec;
		}
		else
		{
			return null;
		}
	}
	
	public static String addSlashes(String input)
	{
		return input.replaceAll("'","\'");
	}
	
	public static String stripSlashes(String input)
	{
		return input.replaceAll("\'","'");
	}
	

	protected void close() throws SQLException
	{
		dbConnection.close();
	}
	
	public int getLatestId(int TABLE) throws SQLException, SelectBfException 
	{
		String sql = "select max(id) id from ";
		switch(TABLE)
		{
			case GAMES: sql+="selectbf_games";break;
			case ROUNDS: sql+="selectbf_rounds";break;
			case PLAYERS: sql+="selectbf_players";break;
		}
		
		Statement stat = createStatement();
		ResultSet res = stat.executeQuery(sql);
		if(res.next())
		{
			return res.getInt("id");
		}
		else
		{
			throw new SelectBfException("Couldn't find any Ids for the specified type.");	
		}
	}

}
