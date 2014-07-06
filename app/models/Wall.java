package models;

import play.*;
import play.mvc.*;
import play.db.*;
import java.sql.*;
import play.Logger;



public class Wall {

	public Long wall_ID;
	public Long user_ID;
	public Long group_ID;

	// Table column names
	public static String WALL = "wall"; // Table Name is Wall
	public static String WALLID = "wall_ID"; 
	public static String USERID = "user_ID";
	public static String GROUPID = "group_ID";
	
	/**
	*
	* Constructor
	*
	* @param wallID: 		The id of the wall
	* @param userID: 		The id of the user
	* @param groupID: 		The id of the group
	*
	*/
	public Wall (Long wall_ID, Long user_ID, Long group_ID) {
		this.wall_ID = wall_ID;
		this.user_ID = user_ID;
		this.group_ID = group_ID;
	} 

	/**
	* Persist
	* Saves the wall to the database, this function will
	* create a new database connection
	*/
	public void Persist(){

		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = DB.getConnection();
			String sql = "UPDATE " + Userd.dquote(Wall.WALL);
				sql += " set " + Wall.USERID + " = ?, ";
				sql += Wall.GROUPID + " = ?, ";
				sql += "where " + Wall.WALLID + " = ?";

			Logger.debug("Generated update: [%s]", sql);
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, this.user_ID);
			pstmt.setLong(2, this.group_ID);
			pstmt.setLong(3, this.wall_ID);

			pstmt.executeUpdate();

			pstmt.close();
			conn.close();
        } catch(SQLException e){
            // Attempt to close the connection
            Logger.debug("Error while persiting user");
            if (conn != null){
                try{
                conn.close();
                } catch (Exception x){
                    Logger.debug("Error while closing connection during exception", x);
                }
            }
        }
    }

	 /**
     * FindById
     * Searches the database for a Group object by the object id.
     *
     * @param connection    The jdbc connection
     * @param id            The id to select by
     */
     public static Group FindByID(){
     	//TODO
     	return null;
     }
}