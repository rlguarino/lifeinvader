package models;

import play.*;
import play.mvc.*;
import play.db.*;
import java.sql.*;
import play.Logger;
import java.util.Calendar;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.util.Date;


public class Post{

	public String content;
	public Type type;
	public Timestamp time_stamp;
	public Long user_ID;
	public Long post_ID;

	// Table column names
	public static String POST = "post"; // Table Name is Wall
	public static String CONTENT = "Content"; 
	public static Type TYPE = "type";
	public static TimeStamp TIMESTAMP = "time_stamp";
	public static Long USER_ID = "user_ID";
	public static Long POST_ID = "post_ID";
	
	/**
	*
	* Constructor
	*
	* @param content: 		The content of the post
	* @param type: 			The type of post with text or urls
	* @param time_stamp:	The timestamp of the post
	* @param userID: 		The id of the user who made the post
	* @param postID: 		The id of the post
	*
	*/
	public Post (String content, Type type, Timestamp time_stamp, Long user_ID, Long post_ID) {
		this.content = content;
		this.type = type;
		this.time_stamp = time_stamp;
		this.user_ID = user_ID;
		this.post_ID = post_ID;
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
			String sql = "UPDATE" + Post.Userd.dquote(Post.POST);
				sql += " set " + Post.CONTENT " = ?, ";
				sql += Post.TYPE " = ?, ";
				sql += Post.TIMESTAMP " = ?, ";
				sql += Post.USER_ID " = ?, ";
				sql += "where " + Post.POSTID + " = ?";

			Logger.debug("Generated update: [%s]", sql);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, this.content);
			pstmt.setString(2, this.type);
			pstmt.setString(3, this.time_stamp);
			pstmt.setString(4, this.user_ID);

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