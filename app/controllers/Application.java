package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.Form;
import play.data.validation.Constraints;
import java.sql.*;

import static play.data.Form.form;

// App imports
import views.html.*;
import models.User;

public class Application extends Controller {
    
    
	/**
	 * Display the index page.
	 * User must be authenticated to view login page.
	 * 
	 */
    public static Result index() {
        //Check for a authenticated user, redirect to home if there is one.
        return ok(index.render());
    }

    @Security.Authenticated(Secured.class)
    public static Result home() {
        return ok(home.render());
    }
    
    /**
     * Login form class
     */
    public static class Login {

        @Constraints.Required
        public String email;
        @Constraints.Required
        public String password;

        /**
         * Validate the authentication.
         *
         * @return null if validation ok, string with details otherwise
         */
        public String validate(){
            if (isBlank(email)){
                return "Email is required";
            }
            if (isBlank(password)){
                return "Password is required";
            }
            //TODO: Catch exceptions here.
            if( User.authenticate(email, password) == null){
                return "Invalid user or password";
            }
            return null;

        }

        private boolean isBlank(String input) {
            return input == null || input.isEmpty() || input.trim().isEmpty();
        }

    }
    
    /**
     * Display login page.
     */
    public static Result login(){
        return ok(login.render(form(Login.class)));
    }

    /**
     * Handle login form submission.
     */
    public static Result authenticate(){
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()){
            return badRequest(login.render(loginForm));
        } else {
            try{
                User user = User.findByEmail(loginForm.get().email);
                session().clear();
                session("id", user.id.toString());
            } catch (User.UserDNEException e){
                session().clear();
                Logger.debug("Error while getting user for authentication", e);
            } catch (Exception e){
                session().clear();
                Logger.debug("Error while getting user for authentication", e);
            }
            
            return redirect(routes.Application.home());
        }
    }

    /**
     * Handle logout GET request.
     *
     * @return redirects the user to the index page.
     */
    public static Result logout(){
    	session().clear();
    	flash("success", "You've been logged out");
        return redirect(
            routes.Application.index()
        );
    }
}
