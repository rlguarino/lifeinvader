package controllers;

import play.*;
import play.mvc.*;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.data.*;
import play.data.Form;
import play.data.validation.Constraints;

import static play.data.Form.form;


// App imports
import views.html.*;
import models.Userd;

public class Application extends Controller {


	/**
	 * Display the index page.
	 * User must be authenticated to view login page.
	 * 
	 */
    @Transactional
    @Security.Authenticated(Secured.class)
    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
  

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
            if( Userd.authenticate(email, password) == null){
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
    @Transactional
    public static Result authenticate(){
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()){
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("email", loginForm.get().email);
            return redirect(routes.Application.index());
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
