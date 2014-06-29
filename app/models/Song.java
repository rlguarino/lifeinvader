package models;

import play.*;
import play.mvc.*;
import play.db.*;
import java.sql.*;
import play.Logger;



public class Song extends Media {

	public String duration;
	public String genre;
	public String artist;

	// Table column names
	public static String SONG = "song"; // Table Name is Song
	public static Long MEDIA_ID = "media_ID";
	public static String FILE_NAME = "file_name";
	public static String FILE_LOCATION = "file_location";
	public static String DISPLAY_NAME = "display_name";
	public static String DURATION = "duration"; 
	public static String GENRE = "genre";
	public static String ARTIST = "artist";
	
	/**
	*
	* Constructor
	*
	* @param media_ID 		The id of the song file
	* @param file_name 		The name of the song file
	* @param file_location 	The location of the song file
	* @param display_name 	The display name of the song file
	* @param duration 		The duration of the song
	*
	*/
	public Song (Long media_ID, String file_name, String file_location, \
		String display_name, String duration, String genre, String artist) {
		this.media_ID = media_ID;
		this.file_name = file_name;
		this.file_location = file_location;
		this.display_name = display_name;
		this.duration = duration;
		this.genre = genre;
		this.artist = artist;
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
			String sql = "UPDATE" + Song.Userd.dquote(Song.SONG);
				sql += " set " + Song.FILE_NAME " = ?, ";
				sql += Song.FILE_LOCATION " = ?, ";
				sql += Song.DISPLAY_NAME " = ?, ";
				sql += Song.DURATION " = ?, ";
				sql += Song.GENRE " = ?, ";
				sql += Song.ARTIST " = ?, ";
				sql += "where " + Song.MEDIA_ID + " = ?";

			Logger.debug("Generated update: [%s]", sql);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, this.file_name);
			pstmt.setString(2, this.file_location);
			pstmt.setString(3, this.display_name);
			pstmt.setString(4, this.duration);
			pstmt.setString(5, this.genre);
			pstmt.setString(6, this.artist);

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