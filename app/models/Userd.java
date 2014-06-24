//User object
package models; 

import play.db.jpa.JPA;

import play.Logger;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.TypedQuery;
import javax.persistence.NonUniqueResultException;
import javax.persistence.NoResultException;

@Entity
public class Userd{
	@Id
	@GeneratedValue
	public Long id;

	public String name;
	public String dob;
	public String address;
	public String email;
	public String passwordHash;

	// TODO: Actually hash passwords
	// TODO: Throw exceptions
	public static Userd authenticate(String email, String clearPasswd){
		Logger.debug("Authenticating user with password: "+ clearPasswd+ " and email: "+ email);
		TypedQuery<Userd> query = JPA.em().createQuery("FROM Userd u WHERE u.email = :email ", Userd.class);
		Userd user = null;
		try{
			user = query.setParameter("email", email).getSingleResult();
		} catch(NonUniqueResultException e) {
			// There were more than one user's with the same email.
			Logger.info("There are more than one users");
			return null;
		} catch(NoResultException e){
			// There were no users in the databse with this email.
			Logger.info("There are no users in the database with this email.");
			return null;
		}
		if (user != null) {
			// We have a user, now check for the passwords to match.
			Logger.debug("We have a user, now check for the passwords to match");
			if (clearPasswd.equals(user.passwordHash)){
				return user;
			} 
		}
		Logger.info("The password did not match");
		return null;
	}

}