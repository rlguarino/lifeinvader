package controllers;

import play.*;
import play.mvc.*;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.data.*;
import play.data.Form;
import play.data.validation.Constraints;
import java.util.Date;

import static play.data.Form.form;

// App imports
import views.html.*;
import models.User;

public class UserController extends Controller {

    /**
     * Settings Form
     */
    public static class SettingsForm{

        @Constraints.Required
        public String email;

        @Constraints.Required
        public String fullname;

        @Constraints.Required
        public String address;

        // Passwords are not required.

        public String currentPasswd;

        public String inputPassword;

        public String confirmPasswd;

        @Constraints.Required
        public Date dateOfBirth;


        /**
         * Constructor
         * @param email     Default email
         * @param fullname  Defualt fullname
         * @param address   Default address
         * @param dateOfBirth   Default date of birth
         */
        public SettingsForm(String email, String fullname, String address, Date dateOfBirth){
            super();
            this.email = fullname;
            this.fullname = fullname;
            this.address = address;
            this.dateOfBirth = dateOfBirth;
            this.inputPassword = "";
            this.confirmPasswd = ""; 
        }

        /**
         * Validate the settings form.
         *
         * @return null if validation ok, string with details otherwise
         */
        public String validate(){
            if (isBlank(email)){
                return "Email is required";
            }
            if (isBlank(fullname)){
                return "Full name is required";
            }
            if (isBlank(address)){
                return "Address is required";
            }
            if (dateOfBirth == null){
                return "Date of birth is required";
            }
            
            return null;
        }

        private boolean isBlank(String input) {
            return input == null || input.isEmpty() || input.trim().isEmpty();
        }
    } 


    /**
     *  settings
     *  Displays the user settings page.
     *  @param  The requested settings page user's id.
     */
    @Security.Authenticated(Secured.class)
    public static Result settings(Long user_id){
        // Check to make sure the logged in user is the owner of the requested page.
        if (user_id != session().get("id")){
            //TODO Redirect to the users profile page.
            return redirect(routes.Application.index())
        }
        try{
            User user = User.findById(user_id);
        }
        // Check for the existance of the user.

        // Fill in the usersettings form.
        return new Todo();
    }

    /**
     *  save
     *  Saves the request from editing the users settings page.
     *  @param the requested settings page user's id.
     */
    @Security.Authenticated(Secured.class)
    public static Result save(Long user_id){
        return new Todo();
    }

	public static void setSetting(User user){

		if(user == null){
			return;
		}
	}
}