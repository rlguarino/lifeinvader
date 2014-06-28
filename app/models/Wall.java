package models;

import play.db.jpa.JPA;

import play.Logger;


//Group size initialized 
//
public class Wall {

	public Long wallID;
	public Long userID;
	public Long groupID;


	public static Long wallID = "wall_ID";
	public static Long userID = "user_ID";
	public static Long groupID = "group_ID";
	
	/**
	*
	* Constructor
	*
	* @param wallID: 		The id of the wall
	* @param userID: 		The id of the user
	* @param groupID: 		The id of the group
	*
	*/
	public Wall (Long wallID, Long userID, Long groupID) {
	this.wallID = wallID;
	this.userID = userID;
	this.groupID = groupID;
	} 

	/**
	* Persist
	* Saves the group to the database, this function will
	* create a new database connection
	*/
	public void Persist(){

		//TODO
		return null;
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