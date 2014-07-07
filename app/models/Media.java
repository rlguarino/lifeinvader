package models;

import play.*;
import play.mvc.*;
import play.db.*;
import java.sql.*;
import play.Logger;


/**
* Media is the superclass
* Subclasses are Song, Photo, and Video
*/
public class Media {

	public Long media_ID;
	public String file_name;
	public String file_location;
	public String display_name;

}
