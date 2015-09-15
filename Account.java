import java.util.Date.*;
import java.sql.Timestamp; 

public class Account
{
	private int account_id;
	private String username, password, e_mail_address; 
	private Timestamp created_at, last_updated_at, birthdate; 
	private double trust_rating, trust_validity, last_lat, last_lng;

	public Account(int account_id, String username, String password, String e_mail_address, Timestamp created_at, Timestamp last_updated_at, Timestamp birthdate, double trust_rating, double trust_validity, double last_lat, double last_lng)
	{
		this.account_id = account_id;
		this.username = username; 
		this.password = password;
		this.e_mail_address = e_mail_address; 
		this.created_at = created_at;
		this.last_updated_at = last_updated_at; 
		this.birthdate = birthdate;
		this.trust_rating = trust_rating;
		this.trust_validity = trust_validity;
		this.last_lat = last_lng;
		this.last_lng = last_lng; 
	}

	public int getId()
	{
		return account_id;
	}

	public String getUsername()
	{
		return username;
	}

	public String getEmail()
	{
		return e_mail_address; 
	}

	public Timestamp getCreatedAt()
	{
		return created_at; 
	}

	public Timestamp getUpdatedAt()
	{
		return last_updated_at; 
	}

	public Timestamp getBirthdate()
	{
		return birthdate; 
	}

	public double getTrustRating()
	{
		return trust_rating;
	}

	public double getTrustValidity()
	{
		return trust_validity; 
	}

	public double getLastLat()
	{
		return last_lat;
	}

	public double getLastLng()
	{
		return last_lng; 
	}
}