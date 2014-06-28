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