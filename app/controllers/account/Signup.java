package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.Form;
import play.data.validation.Constraints;

import static play.data.Form.form;

// App imports
import views.html.*;
import models.Userd;


public class Signup extends Controller {

	/**
	 * Registration form
	 */
    public static class Register {

        @Constraints.Required
        public String email;

        @Constraints.Required
        public String fullname;

        @Constraints.Required
        public String inputPassword;

        /**
         * Validate the registration form.
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
            if (isBlank(inputPassword)){
                return "Password is required";
            }

            // TODO Actually add the new user to the database
            // Remember to check for duplicate email

            return null;
        }

        private boolean isBlank(String input) {
            return input == null || input.isEmpty() || input.trim().isEmpty();
        }
    }

	/**
     * Display registration page.
     */
    public static Result registration(){
        return new Todo();
    }


    /**
     * Register a new account.
     *
     */
    public static Result register(){
    	Logger.debug(String.format("Registering a new user"));
    	return new Todo();
    }

}