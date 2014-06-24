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

	@Transactional
	@Security.Authenticated(Secured.class)
    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
  

    public static class Register {
    	/**
		* Register Class taken from https://github.com/yesnault/PlayStartApp/
	 	*/
    	@Constraints.Required
    	public String email;

    	@Constraints.Required
    	public String fullname;

    	@Constraints.Required
    	public String inputPassword;

    	/**
         * Validate the authentication.
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

    		return null;
    	}

    	private boolean isBlank(String input) {
            return input == null || input.isEmpty() || input.trim().isEmpty();
        }
    }

    public static class Login {

    	@Constraints.Required
    	public String email;
    	@Constraints.Required
    	public String password;


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

    public static Result login(){
    	return ok(login.render(form(Login.class)));
    }

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
}
