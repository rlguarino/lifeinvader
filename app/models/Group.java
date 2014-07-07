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
	public static String GROUPSIZE = "groupSize";
	
	/**
     * dquote
     * double quotes the given string in \", for use in sql queries.
     * @param str the string to quote
     * @return the quoted string
     */
    public static String dquote(String str){
        return "\"" + str + "\"";
    }

    /**
     * quote
     * single quotes the given string in \", for use in sql queries.
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

    public static Group createGroup(Long group_Id, String name, int groupsize) throws SQLException{
    	Group group = null;
    	PreparedStatement insertGroup = null;
    	PreparedStatement checkGroup = null;
    	Connection conn = null;

    	try{
    		conn = DB.getConnection();

    		String existingGroup = String.format("SELECT * from %s where group_id = ?", Group.dquote(Group.GROUP));
    		String insertGroupStr = String.format("INSERT into %s (%s, %s, %s) VALUES ( ?, ?, ?, ?)",
                Group.dquote(Group.GROUP),
                Group.GROUPID,
                Group.NAME,
                Group.GROUPSIZE);
            insertGroup = conn.prepareStatement(insertGroupStr);
            checkGroup = conn.prepareStatement(existingGroup);

            insertGroup.setLong(1, group_Id);
            insertGroup.setString(2, name);
            insertGroup.setInt(3, groupsize);

            checkGroup.setLong(1, group_Id);
            ResultSet rs = checkGroup.executeQuery();
            if(rs.next()){
            	Logger.debug("Already a group with this ID");
            }

            Boolean results = insertGroup.execute();
            if(!results){
            	Logger.debug("Failed to execute create group.");
            }
    	}catch (SQLException e){
    		Logger.debug("Error creating group", e);
    	}

    	checkGroup.close();
    	insertGroup.close();
    	conn.close();
    	return group;
    }

	 /**
     * FindById
     * Searches the database for a Group object by the object id.
     *
     * @param connection    The jdbc connection
     * @param id            The id to select by
     */
     public static Group FindByID(Long group_Id) throws SQLException{
     	//TODO
     	Group group = null;
     	Connection conn = null;
     	PreparedStatement findGroup = null;

     	if(group_Id != null){
     		try{
     			conn = DB.getConnection();
    			
    			String findGroupID = String.format("SELECT * from %s where group_id = ?", Group.dquote(Group.GROUP));
     			findGroup = conn.prepareStatement(findGroupID);
     			findGroup.setLong(1, group_Id);

     			ResultSet rs = findGroup.executeQuery();
     			if (rs.next()){
     				group = new Group(
     				rs.getLong(Group.GROUPID),
     				rs.getString(Group.NAME),
     				rs.getInt(Group.GROUPSIZE));
     			}

     			findGroup.close();
     			conn.close();
     			return group;

     		}catch(SQLException e){
     			Logger.debug("Error retrieving group.", e);
     		}
     	}
     	return group;
    }

    public void deleteGroup(Long group_ID) throws SQLException{
        Connection conn = null;
        PreparedStatement deleteByID = null;

        String deleteGroupStr = String.format("DELETE from %s where group_id = ?",
            Group.dquote(GROUP), group_ID);

        try {
            conn = DB.getConnection();
            deleteByID = conn.prepareStatement(deleteGroupStr);
            ResultSet rs = deleteByID.executeQuery();

            if(rs.next()){
                Logger.debug("Failed to delete Group.")
            }

            deleteByID.close();
            conn.close();
        } catch(SQLException e){
            Logger.debug("Failed while trying to delete group.");
        }
    }
}