package controllers;

import java.util.Date;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.Form;
import play.data.validation.Constraints;
import java.sql.SQLException;

import static play.data.Form.form;

// App imports
import views.html.*;
import models.User;


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
        public String address;

        @Constraints.Required
        public String inputPassword;

        @Constraints.Required
        public String confirmPasswd;

        @Constraints.Required
        public Date dateOfBirth;

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
            if (!inputPassword.equals(confirmPasswd)){
                return "Passwords must match";
            }
            if (isBlank(address)){
                return "Address is required";
            }
            if (dateOfBirth == null){
                return "Date of birth is required";
            }
            
            try{
                User user = User.createUser(email, fullname, address, inputPassword, dateOfBirth);
            } catch (User.EmailInUseException e){
                return String.format( "Email[%s] already in use, Forgot password? Good Luck!", e.getMessage());
            } catch (SQLException x){
                return "Unknown error, contact administration.";
            }

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
        return ok(register.render(form(Register.class)));
    }


    /**
     * Register a new account.
     *
     */
    public static Result register(){
        Logger.debug(String.format("Registering a new user"));
        Form<Register> registerForm = form(Register.class).bindFromRequest();
        if (registerForm.hasErrors()){
            return badRequest(register.render(registerForm));
        }else{
            Logger.debug(String.format("Email: %s Name: %s Passwd %s, CPasswd %s Birthdate %s",
                registerForm.get().email,
                registerForm.get().fullname,
                registerForm.get().inputPassword,
                registerForm.get().confirmPasswd,
                registerForm.get().dateOfBirth.toString()));
            return redirect(routes.Application.index());
        }
    }

}