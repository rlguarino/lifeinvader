package models;

import play.*;
import play.mvc.*;
import play.db.*;
import java.sql.*;
import play.Logger;


// Wall object initialized
public class Media {

	public Long media_ID;
	public String file_name;
	public String file_location;
	public String Display

	// Table column names
	public static String Media = "wall"; // Table Name is Wall
	public static Long WALLID = "wall_ID"; 
	public static Long USERID = "user_ID";
	public static Long GROUPID = "group_ID";
	
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
			String sql = "UPDATE" + Wall.Userd.dquote(Wall.WALL);
				sql += " set " + Wall.USERID " = ?, ";
				sql += Wall.GROUPID " = ?, ";
				sql += "where " + Wall.WALLID + " = ?";

			Logger.debug("Generated update: [%s]", sql);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, this.user_ID);
			pstmt.setString(2, this.group_ID);
			pstmt.setString(3, this.wall_ID);

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