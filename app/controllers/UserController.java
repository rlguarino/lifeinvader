package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.Form;
import play.data.validation.Constraints;

import static play.data.Form.form;

// App imports
import views.html.*;
import models.User;

public class UserController extends Controller {

	public static Result getProfile(String email){

		if(email != null){
			User dbUser = User.findByEmail(email);
			return ok(index.render("This Usercontroller is working...."));
		}else{
			Logger.debug("Email is missing.");
			return badRequest();
		}
	}
}