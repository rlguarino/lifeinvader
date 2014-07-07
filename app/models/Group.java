package models;

import play.*;
import play.mvc.*;
import play.db.*;
import java.sql.*;
import play.Logger;



public class Group {

	public Long group_Id;
	public String name;
	public int groupsize;

	public static String GROUP = "group"; // Table Name is Group
	public static String GROUPID = "group_id";
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
	* @param groupsize: The size of the group
	*
	*/
	public Group (Long group_Id, String name, int groupsize) {
		this.group_Id = group_Id;
		this.name = name;
		this.groupsize = groupsize;
	} 

	/**
	* Persist
	* Saves the group to the database, this function will
	* create a new database connection
	*/
	public void Persist(){

		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = DB.getConnection();
			String sql = "UPDATE " + Group.dquote(Group.GROUP);
				sql += " set " + Group.NAME + " = ?, ";
				sql += "where " + Group.GROUPID + " = ?";
			Logger.debug("Generated update: [%s]", sql);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,this.name);
			pstmt.setLong(2,this.group_Id);

			pstmt.executeUpdate();

			pstmt.close();
			conn.close();
		} catch(SQLException e){
            // Attempt to close the connection
            Logger.debug("Error while persiting group");
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