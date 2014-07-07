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
	public static String USERID = "userID";
	public static String GROUPID = "groupID";
	
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

	public static String dquote(String str){
        return "\"" + str + "\"";
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
			String sql = "UPDATE " + Wall.dquote(Wall.WALL);
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
            Logger.debug("Error while persiting wall");
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
     * Searches the database for a wall object by the object id.
     *
     * @param connection    The jdbc connection
     * @param id            The id to select by
     */
     public static Wall FindByID(Long wall_ID){

     	Wall wall = null;
     	Connection conn = null;
     	PreparedStatement findWall = null;

     	if(wall_ID != null){
     		try{
     			conn = DB.getConnection();
    			
    			String findWallID = String.format("SELECT * from %s where wall_ID = ?", Wall.dquote(Wall.WALL));
     			findWall = conn.prepareStatement(findWallID);
     			findWall.setLong(1, wall_ID);

     			ResultSet rs = findWall.executeQuery();
     			if (rs.next()){
     				wall = new Wall(	
     				rs.getLong(Wall.USERID),
     				rs.getLong(Wall.GROUPID),
     				rs.getLong(Wall.WALLID));
     			}

     			findWall.close();
     			conn.close();
     			return wall;

     		}catch(SQLException e){
     			Logger.debug("Error retrieving wall.", e);
     		}
     	}else{
     		Logger.debug("Wall id is null.");
     	}
     	return wall;
     }

     public void deleteWall(Long wall_ID) throws SQLException{
        Connection conn = null;
        PreparedStatement deleteByID = null;

        String deleteWallStr = String.format("DELETE from %s where wall_ID = ?",
            Wall.dquote(WALL), wall_ID);

        try {
            conn = DB.getConnection();
            deleteByID = conn.prepareStatement(deleteWallStr);
            ResultSet rs = deleteByID.executeQuery();

            if(rs.next()){
                Logger.debug("Failed to delete Wall.");
            }

            deleteByID.close();
            conn.close();
        } catch(SQLException e){
            Logger.debug("Failed while trying to delete wall.");
        }
    }
}