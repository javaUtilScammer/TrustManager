import java.util.Date.*; 
import java.sql.*; 

public class Comment
{
	private int comment_id; 
	private String message; 
	private Timestamp created_at; 
	private Account commenter; 
	private Evaluation eval; 


	public Comment(int comment_id, String message, Timestamp created_at, Account commenter, Evaluation eval)
	{
		this.comment_id = comment_id; 
		this.message = message;
		this.created_at = created_at; 
		this.commenter = commenter;
		this.eval = eval; 
	}

	public int getId()
	{
		return comment_id; 
	}

	public String getMessage()
	{
		return message;
	}

	public Timestamp getCreatedAt()
	{
		return created_at; 
	}

	public Account getCommenter()
	{
		return commenter; 
	}

	public Evaluation getEval()
	{
		return eval; 
	}

}