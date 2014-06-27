//User object
package models; 

import play.*;
import play.mvc.*;
import play.db.*;
import java.sql.*;
import play.Logger;
import java.util.Calendar;
import java.sql.Timestamp;
import java.sql.ResultSet;



public class Userd{
    public Long id;
    public String name;
    public String dob;
    public String address;
    public String email;
    public String passwordHash;
    public Boolean isVisible;
 
    // Table column names
    public static String USER = "userd"; // Table name
    public static String ID = "user_ID";
    public static String NAME = "name";
    public static String DOB = "dateOfBirth";
    public static String EMAIL = "email";
    public static String ADDRESS = "address";
    public static String PASSWD = "passwordHash";
    public static String ISVISIBLE = "isVisible";

    /**
     * Constructor
     *
     * @param id:           The id of the user
     * @param name:         The name of the user
     * @param dob:          The date of birth of the user
     * @param address       The address of the user
     * @param passwordHash  The hash of the users password
     * 
     */
    public Userd(Long id, String name, String dob, String address, String email, String passwordHash, Boolean isVisible){
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.address = address;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isVisible = isVisible;
    }

    /**
     * Persist
     * Saves the user to the database, this function will create a new database
     *  connection
     */
    public void Persist(){

        PreparedStatement pstmt = null;

        try{
            Connection conn = DB.getConnection();
            String sql = "UPDATE" + Userd.USER;
                sql += " set " + Userd.NAME + " = ?, ";
                sql += Userd.DOB + " = ?, ";
                sql += Userd.ADDRESS + " = ?, ";
                sql += Userd.EMAIL + " = ?, ";
                sql += Userd.PASSWD + " = ?, ";
                sql += Userd.ISVISIBLE + " = ? ";
                sql += "where " + Userd.ID + " = ?";

            Logger.debug("Generated update: [%s]", sql);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, this.name);
            pstmt.setString(2, this.dob);
            pstmt.setString(3, this.address);
            pstmt.setString(4, this.email);
            pstmt.setString(5, this.passwordHash);
            pstmt.setBoolean(6, this.isVisible );
            pstmt.setLong(7, this.id);

            pstmt.executeUpdate();


        } catch(SQLException e){
            Logger.debug("Error in persisting User");
        }
    }

    /**
     * FindById
     * Searches the database for a User object by the object id.
     *
     * @param connection    The jdbc connection
     * @param id            The id to select by
     */
    public static Userd FindById(){
        //TODO
        return null;
    }

    /**
     * FindByEmail
     * Searches the database for a User object using the email.
     *
     * @param email
     */
    public static Userd findByEmail(String email) throws SQLException{
        Userd user = null;

        // SELECT * from Userd, where email = <email>
        String sql = String.format( "SELECT * from %s where %s = %s",
            USER, EMAIL, email);

        Logger.debug(String.format("Selecting with %s", sql));
        Connection conn = DB.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        Logger.debug("Executed query");


        // Load the first user, there should only be one.
        if (rs.next()){
            user = new Userd(
            rs.getLong(Userd.ID),
            rs.getString(Userd.NAME),
            rs.getString(Userd.DOB),
            rs.getString(Userd.ADDRESS),
            rs.getString(Userd.EMAIL),
            rs.getString(Userd.PASSWD),
            rs.getBoolean(Userd.ISVISIBLE));
        }

        // Make sure the statement is closed
        if (stmt != null ){
            stmt.close();
        }
        Logger.debug("Returning user");
        return user;
    }


    /**
     * authenticate
     * returns a Userd if the email, and clearPassword are correct,
     *      otherwise returns null
     *
     * @param email         The Users email
     * @param clearPasswd   The users password
     * @param conn          The jdbc connection
     */
    public static Userd authenticate(String email, String clearPasswd){
        // TODO: Actually hash passwords
        // TODO: Throw exceptions
        Userd user = null;
        try{
            user = findByEmail(email);
        } catch (SQLException e){
            return null;
        }

        if (user != null) {
            // We have a user, now check for the passwords to match.
            Logger.debug("We have a user, now check for the passwords to match");
            if (clearPasswd.equals(user.passwordHash)){
                return user;
            } 
        }
        Logger.info("The password did not match");
        return null;
    }

    /**
    * Returns a user's settings.
    */
    public static Userd returnSettings(Userd user){
        Logger.debug("Returning " + user + "'s settings");
        //TypedQuery<Userd> query = JPA.em().find
        return user;
    }

    /**
    * Creates a new user in the database.
    */
    public static void createUser(Userd user){
        Logger.debug("Saving User "+ user + " to database.");
    //place holder
    }

    /**
    * Updates a user's information in the database.
    */
    public static void updateUser(Userd user){
        Logger.debug("Updating User " + user + " in database.");
    }

    /**
    * Hides a user's information in the database.
    */
    public static void deleteUser(Userd user){
        Logger.debug("Deleting User " + user + " in database.");
    }
}