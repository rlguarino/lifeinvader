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



public class Request {

	public Long requestID;
	public Long senderID;
	public long receiverID;


	// Table column names
	public static String REQUEST = "request"; // Table Name is request
	public static String REQUEST_ID = "request_ID";
	public static String SENDER_ID = "sender_ID";
	public static String RECEIVER_ID = "receiver_ID";
	
	/**
	*
	* Constructor
	*
	* @param requestID 		The id of the request
	* @param senderID 		The id of the sender
	* @param receiverID 	The id of the receiver
	*
	*/
	public Request (Long requestID, Long senderID, Long receiverID) {
		this.requestID = requestID;
		this.senderID = senderID;
		this.receiverID = receiverID;
	}

	/**
	* Persist
	* Saves the photo to the database, this function will
	* create a new database connection
	*/
	public void Persist(){

		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = DB.getConnection();
			String sql = "UPDATE " + Userd.dquote(Request.REQUEST);
				sql += " set " + Request.SENDER_ID + " = ?, ";
				sql += Request.RECEIVER_ID + " = ?, ";
				sql += "where " + Request.REQUEST_ID + " = ?";

			Logger.debug("Generated update: [%s]", sql);
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, this.senderID);
			pstmt.setLong(2, this.receiverID);
			pstmt.setLong(3, this.requestID);


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
}
