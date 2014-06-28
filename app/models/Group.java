package models;

import play.db.jpa.JPA;

import play.Logger;


//Group size initialized 
//
public class Group {

	public Long id;

	public String name;
	public int groupsize;

	public static String ID = "user_id";
	public static String name = "name";
	public static int = 0;
	
	/**
	*
	* Constructor
	*
	* @param id: 		The id of the group
	* @param name: 		The name of the group
	* @param groupsize: The groupsize 
	*
	*/
	public Group (Long id, String name, int groupsize) {
	this.id = id;
	this.name = name;
	this.groupsize = groupsize;
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