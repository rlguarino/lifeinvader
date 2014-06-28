package models;

import play.*;
import play.mvc.*;
import play.db.*;
import java.sql.*;
import play.Logger;


//Group size initialized 
//
public class Group {

	public Long groupId;
	public String name;
	public int groupsize;

	public static String GROUP = "group"; // Table Name is Group
	public static Long GROUP_ID = "groupid";
	public static String NAME = "name";
	
	/**
     * dquote
     * double quotes the given string in \", for use in sql querries.
     * @param str the string to quote
     * @return the quoted string
     */
    public static String dquote(String str){
        return "\"" + str + "\"";
    }

    /**
     * quote
     * single quotes the given string in \", for use in sql querries.
     * @param str the string to quote
     * @return the quoted string
     */
    public static String quote(String str){
        return "\'" + str + "\'";
    }

	/**
	*
	* Constructor
	*
	* @param id: 		The id of the group
	* @param name: 		The name of the group
	* @param groupsize: The groupsize 
	*
	*/
	public Group (Long groupid, String name, int groupsize) {
	this.groupId = groupId;
	this.name = name;
	this.groupsize = groupsize;
	} 

	/**
	* Persist
	* Saves the group to the database, this function will
	* create a new database connection
	*/
	public void Persist(){

		PreparedStatement = pstmt = null;
		Connection conn = null;
		try {
			conn = DB.getConnection();
			String sql = "UPDATE" + Group.dquote(Group.GROUP);
				sql += " set " + Group.NAME + " = ?, ";
				sql += "where " + Group.GROUP_ID + " = ?";
			Logger.debug("Generated update: [%s]", sql);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,this.name);
			pstmt.setString(2,this.groupId);
			pstmt.setString(3,this.groupsize);

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