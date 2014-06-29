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
import views.html.profile.*; 
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
    public static Result settings(){
        Long user_id = new Long(session("id"));
        User user = null;
        try{
            user = User.findById(user_id);
        } catch (User.UserDNEException e) {
            Logger.debug("Error:", e);
        }
        
        if (user == null){
            return notFound();
        }

        Form<SettingsForm> form = form(SettingsForm.class);
        SettingsForm sf = new SettingsForm();
        sf.email = user.email;
        sf.fullname = user.name;
        sf.address = user.address;
        sf.dateOfBirth = new Date(user.dob);

        // Fill in the usersettings form.
        return ok(settings.render(form.fill(sf)));
        //return new Todo();
    }

    /**
     *  save
     *  Saves the request from editing the users settings page.
     *  @param the requested settings page user's id.
     */
    @Security.Authenticated(Secured.class)
    public static Result save(){
        return new Todo();
    }

}