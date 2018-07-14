package models.client_models;

import java.sql.*;

public class SQLManager {
	private static SQLManager sqlManager;
	private static final String DATA_BASE_NAME = "file.db";
	private static final String TABLE_TYPE = "filetype";
	private static final String FILE_EXE = "extension";
	private static final String FILE_TYPE = "type";
	
	private Connection connection;

	private SQLManager() {
		createDataBase();
	}
	
	/**
	 * get method for sqlManager variable
	 * @return an instance of the SQLManager class (singleton)
	 */
	public static SQLManager getSqlManager() {
		if(sqlManager == null) {
			sqlManager = new SQLManager();
		}
		return sqlManager;
	}
	
	private void createDataBase() {
		try {
	         Class.forName("org.sqlite.JDBC");
	         this.connection = DriverManager.getConnection("jdbc:sqlite:"+DATA_BASE_NAME);
	         
	         //--------------------------------------------------------------------
	         //Create Type Table
	         Statement statement = this.connection.createStatement();
	         
	         String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_TYPE + 
	                        "(" + FILE_EXE +" TEXT PRIMARY KEY NOT NULL, " + 
	                        FILE_TYPE + " TEXT NOT NULL)"; 
	         	         
	         statement.executeUpdate(sql);
	         
	      } catch ( Exception e ) {
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	         System.exit(0);
	      }
	}

	/**
	 * get method for the connection variable 
	 * @return the connection to the data base 
	 */
	public Connection getConnection() {
		return connection;
	}

	public void insertType(String extension, String type) {
		try {
			Statement statement = this.connection.createStatement();
			
	        String sql = "INSERT INTO " + TABLE_TYPE + " VALUES ('" + extension 
	        		+"', '" + type + "')"; 
	        
	        statement.execute(sql);
	        
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getType(String extension) {
		try {
			Statement statement = this.connection.createStatement();
	        
	        String sql = "SELECT " + FILE_TYPE + " FROM " + TABLE_TYPE +
	        				" WHERE " + FILE_EXE + " = '" + extension + "'"; 
	        
	        ResultSet resultSet = statement.executeQuery(sql);
	        
	        if (resultSet.next()) {
	     			        
		        return resultSet.getString(FILE_TYPE);
	        }

		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
}
