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
	public String type;
	public Timestamp time_stamp;
	public Long user_ID;
	public Long post_ID;
	public Long wall_ID;

	// Table column names
	public static String POST = "post"; // Table Name is post
	public static String CONTENT = "content"; 
	public static String TYPE = "type";
	public static String TIMESTAMP = "timestamp";
	public static String USER_ID = "userid";
	public static String POST_ID = "postid";
	public static String WALL_ID = "wallid";
	
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
	public Post (String content, String type, Timestamp time_stamp, Long user_ID, Long post_ID, Long wall_ID) {
		this.content = content;
		this.type = type;
		this.time_stamp = time_stamp;
		this.user_ID = user_ID;
		this.post_ID = post_ID;
		this.wall_ID = wall_ID;
	}

	public static String dquote(String str){
        return "\"" + str + "\"";
    }

	/**
	* Persist
	* Saves the post to the database, this function will
	* create a new database connection
	*/
	public void Persist(){

		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = DB.getConnection();
			String sql = "UPDATE " + Userd.dquote(Post.POST);
				sql += " set " + Post.CONTENT + " = ?, ";
				sql += Post.TYPE + " = ?, ";
				sql += Post.TIMESTAMP + " = ?, ";
				sql += Post.USER_ID + " = ?, ";
				sql += Post.WALL_ID + " = ?, ";
				sql += "where " + Post.POST_ID + " = ?";

			Logger.debug("Generated update: [%s]", sql);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, this.content);
			pstmt.setString(2, this.type);
			pstmt.setTimestamp(3, this.time_stamp);
			pstmt.setLong(4, this.user_ID);
			pstmt.setLong(5, this.wall_ID);
			pstmt.setLong(6, this.post_ID);

			pstmt.executeUpdate();

			pstmt.close();
			conn.close();
        } catch(SQLException e){
            // Attempt to close the connection
            Logger.debug("Error while persisting Post");
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
     public static Post FindByID(Long post_ID) throws SQLException{
     	Post post = null;
     	PreparedStatement findPostId = null;
     	Connection conn = null;

     	String sql = String.format("SELECT * from %s where postid = ?",
     		Post.dquote(POST), post_ID);


     	if(post_ID != null){
     		try{
     			conn = DB.getConnection();
     			findPostId = conn.prepareStatement(sql);
     			findPostId.setLong(1, post_ID);

     			ResultSet rs = findPostId.executeQuery();

     			if(rs.next()){
     				post = new Post(
     					rs.getString(Post.CONTENT),
     					rs.getString(Post.TYPE),
     					rs.getTimestamp(Post.TIMESTAMP),
     					rs.getLong(Post.USER_ID),
     					rs.getLong(Post.WALL_ID),
     					rs.getLong(Post.POST_ID));
     			}
     		
     		findPostId.close();
     		conn.close();
     		return post;
     		}catch (SQLException e){
     			Logger.debug("Error retrieving post.", e);
     		}
     	}
     	return post;
    }

    public static void deletePost(Long post_ID) throws SQLException{
    	Post post = null;

    	String deletePostStr = String.format("DELETE from %s where postid = ?",
    		Post.dquote(POST), post_ID);

    	Connection conn = null;
    	PreparedStatement deleteP = null;

    	if(post_ID != null){
    		try{
    			conn = DB.getConnection();
    			deleteP = conn.prepareStatement(deletePostStr);
    			ResultSet rs = deleteP.executeQuery();

    			if(rs.next()){
    				Logger.debug("Failed to delete the Post.");
    			}
    		deleteP.close();
    		conn.close();
    		}catch (SQLException e){
    			Logger.debug("Failed while trying to delete the post.");
    		}
    	}else{
    		Logger.debug("Post id is null.");
    	}
    }
}