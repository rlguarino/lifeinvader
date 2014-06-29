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


public class User{
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
    public User(Long id, String name, String dob, String address, String email, String passwordHash, Boolean isVisible){
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

    public static class UserDNEException extends Exception{
        public UserDNEException(String message){
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
    public static User createUser(String email, String fullname, String address, String clearPasswd, Date dob) throws EmailInUseException, SQLException{
        Logger.debug(String.format("Attemping to create user with %s, %s, %s %s",
            email, fullname, clearPasswd, dob.toString()));

        User user = null;
        Connection conn = null;
        PreparedStatement checkEmail = null;
        PreparedStatement insertUser = null;
        try{
            // Get connection
            conn = DB.getConnection();
            //Create email statement.
            String searchEmail = String.format("SELECT * from %s where email = ?", User.dquote(User.USER));
            checkEmail = conn.prepareStatement(searchEmail);
            //Create insert statement.
            String insertUserStr = String.format("INSERT into %s (%s, %s, %s, %s, %s) VALUES ( ?, ?, ?, ?, ?)",
                User.dquote(User.USER),
                User.NAME,
                User.DOB,
                User.ADDRESS,
                User.EMAIL,
                User.PASSWD);
            insertUser = conn.prepareStatement(insertUserStr);


            checkEmail.setString(1, email);
            ResultSet rs = checkEmail.executeQuery();
            if (rs.next()){
                Logger.debug(String.format("User already registered with email %s", email));
                throw new User.EmailInUseException(email);
            }

            insertUser.setString(1, fullname);
            insertUser.setString(2, dob.toString());
            insertUser.setString(3, address);
            insertUser.setString(4, email);
            insertUser.setString(5, clearPasswd);
            Boolean res = insertUser.execute();
            if( !res){
                //The statement failed
                Logger.debug("Falied to execute statment.");
            }

        }catch (SQLException e){
            Logger.debug("Error creating new users", e);
            if (insertUser != null){
                try{
                    insertUser.close();
                } catch (Exception x){Logger.debug("Caught error while handling another error.", x);}
            }
            if (checkEmail != null){
                try{
                    checkEmail.close();
                } catch (Exception x){Logger.debug("Caught error while handling another error.", x);}
            }
            if (conn != null){
                try{
                    conn.close();
                } catch (Exception x){Logger.debug("Caught error while handling another error.", x);}
            }
            throw e;
        }

        // Close connections
        insertUser.close();
        checkEmail.close();
        conn.close();
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
            String sql = "UPDATE " + User.dquote(User.USER);
                sql += " set " + User.NAME + " = ?, ";
                sql += User.DOB + " = ?, ";
                sql += User.ADDRESS + " = ?, ";
                sql += User.EMAIL + " = ?, ";
                sql += User.PASSWD + " = ?, ";
                sql += User.ISVISIBLE + " = ? ";
                sql += "where " + User.ID + " = ?";

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
    public static User findById(Long id) throws UserDNEException, UserDNEException{
        Logger.debug(String.format("Attempting to locate user with id"));
        User user = null;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = DB.getConnection();
            //Select * from user where user_id = <id>;
            String select = String.format("SELECT * FROM %s WHERE %s = ? ", User.dquote(User.USER), User.ID);
            pstmt = conn.prepareStatement(select);
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            // There should only be one.
            if (rs.next()){
                user = new User(
                    rs.getLong(User.ID),
                    rs.getString(User.NAME),
                    rs.getString(User.DOB),
                    rs.getString(User.ADDRESS),
                    rs.getString(User.EMAIL),
                    rs.getString(User.PASSWD),
                    rs.getBoolean(User.ISVISIBLE));
            }else{
                // No user was found
                throw new UserDNEException(id.toString());
            }
            // Close connections
            pstmt.close();
            conn.close();
            return user;
        } catch (SQLException e ){
            Logger.debug("Error while selecting a user by id", e);
            if (pstmt != null) {
                try{
                    pstmt.close();
                } catch (Exception x){
                    Logger.debug("Error while erroring", x);
                }
            }
            if (conn != null){
                try{
                    conn.close();
                } catch (Exception x){
                    Logger.debug("Error while erroring", x);
                }
            }
            return null;
        } 
    }

    /**
     * FindByEmail
     * Searches the database for a User object using the email.
     *
     * @param email     The email address for the user
     * @return          The User id successful null if not.
     */
    public static User findByEmail(String email) throws SQLException, UserDNEException{
        User user = null;

        // SELECT * from User, where email = <email>
        String sql = String.format( "SELECT * from %s where %s = ?",
            User.dquote(USER), EMAIL, email);

        Connection conn = null;
        PreparedStatement pstmt = null; 
        try{
            conn = DB.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            // Load the first user, there should only be one.
            if (rs.next()){
                user = new User(
                    rs.getLong(User.ID),
                    rs.getString(User.NAME),
                    rs.getString(User.DOB),
                    rs.getString(User.ADDRESS),
                    rs.getString(User.EMAIL),
                    rs.getString(User.PASSWD),
                    rs.getBoolean(User.ISVISIBLE));
            }else{
                // No user was found
                throw new UserDNEException(email);
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
     * returns a User if the email, and clearPassword are correct,
     *      otherwise returns null
     *
     * @param email         The Users email
     * @param clearPasswd   The users password
     * @param conn          The jdbc connection
     */
    public static User authenticate(String email, String clearPasswd){
        // TODO: Actually hash passwords
        // TODO: Throw exceptions
        User user = null;
        try{
            user = findByEmail(email);
        } catch (SQLException e){
            return null;
        } catch (UserDNEException e){
            Logger.debug("User did not exist");
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
    public static User returnSettings(User user){
        Logger.debug("Returning " + user + "'s settings");
        //TypedQuery<User> query = JPA.em().find
        return user;
    }

    /**
    * Updates a user's information in the database.
    */
    public static void updateUser(User user){
        Logger.debug("Updating User " + user + " in database.");
    }

    /**
    * Hides a user's information in the database.
    */
    public static void deleteUser(User user){
        Logger.debug("Deleting User " + user + " in database.");
    }
}