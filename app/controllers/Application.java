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

public class Application extends Controller {
    
    
	/**
	 * Display the index page.
	 * User must be authenticated to view login page.
	 * 
	 */
    @Security.Authenticated(Secured.class)
    public static Result index() {
        return ok(index.render("Your new application is ready."));
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
    public static Result authenticate(){
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()){
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("id", loginForm.get().email);
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
