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
import java.util.Date;


public class Userd{
    public Long id;
    public String name;
    public String dob;
    public String address;
    public String email;
    public String passwordHash;
    public Boolean isVisible;
 
    // Table column names
    public static String USER = "user"; // Table name
    public static String ID = "user_ID";
    public static String NAME = "name";
    public static String DOB = "dateOfBirth";
    public static String EMAIL = "email";
    public static String ADDRESS = "address";
    public static String PASSWD = "passwordHash";
    public static String ISVISIBLE = "isVisible";

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
     * Constructor
     *
     * @param id            The id of the user
     * @param name          The name of the user
     * @param dob           The date of birth of the user
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
     * Exception that the email is already in use.
     *
     */
    public static class EmailInUseException extends Exception{
        public EmailInUseException(String message){
            super(message);
        }
    }

    //TODO throw our own errors about duplicated emails, and such.
    /**
     * createUser
     * Returns a new user which is persisted in the database. This generates 
     *  a new id, and sets the isVisible field in the user.
     *
     * @param email         Email of the new user
     * @param fullname      Fullname of the new user
     * @param clearPasswd   Clear password for the new user
     * @param dob           The date of birth for the new user
     * @return              The new user if successful, null otherwise.
     */
    public static Userd createUser(String email, String fullname, String clearPasswd, Date dob) throws EmailInUseException, SQLException{
        Logger.debug(String.format("Attemping to create user with %s, %s, %s %s",
            email, fullname, clearPasswd, dob.toString()));

        Userd user = null;
        try{
            // Checking to see if the email already exists in the database.
            String sql = String.format("SELECT * from %s where email = '%s'", Userd.USER, email);
            Connection conn = DB.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()){
                Logger.debug(String.format("User already registered with email %s", email));
                throw new Userd.EmailInUseException(String.format("Email [%s] already in use", email));
            }
        }catch (SQLException e){
            Logger.debug("Error checking for existing emails");
            throw e;
        }
        return user;

    }
    /**
     * Persist
     * Saves the user to the database
     */
    public void Persist(){

        PreparedStatement pstmt = null;
        Connection conn = null;
        try{
            conn = DB.getConnection();
            String sql = "UPDATE" + Userd.dquote(Userd.USER);
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
     * @param email     The email address for the user
     * @return          The userd id successful null if not.
     */
    public static Userd findByEmail(String email) throws SQLException{
        Userd user = null;

        // SELECT * from Userd, where email = <email>
        String sql = String.format( "SELECT * from %s where %s = ?",
            Userd.dquote(USER), EMAIL, email);

        Connection conn = null;
        PreparedStatement pstmt = null; 
        try{
            conn = DB.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

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

            pstmt.close();
            conn.close();
            return user;
        } catch(SQLException e){
            Logger.debug("Error while finding user by email", e);
            if (pstmt != null){
                try{
                    pstmt.close();
                }catch (Exception x){
                    Logger.debug("Error while closing statment durring error.", x);
                }
            }
            if (conn != null){
                try{
                    conn.close();
                } catch (Exception x){
                    Logger.debug("Error while closing connection durring error", x);
                }
            }
            throw e;
        }
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