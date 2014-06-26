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

public class UserController extends Controller {

	public static void setSetting(Userd user){

		if(user == null){
			return null;
		}
	}
}