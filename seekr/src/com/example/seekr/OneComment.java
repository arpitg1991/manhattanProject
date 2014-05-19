package com.example.seekr;

public class OneComment {
	public boolean left;
	public String comment;
	public String userName = "Anonymous";
	public String userId = "debug";
	public String datetime;
	
	
	public OneComment(boolean left, String comment) {
		super();
		this.left = left;
		this.comment = comment;
	}
	
	public OneComment(boolean left, String comment, String userId) {
		super();
		this.left = left;
		this.comment = comment;
	}
	
	public OneComment(boolean left, String comment, String userId, String userName) {
		super();
		this.left = left;
		this.comment = comment;
		this.userName = userName;
	}

}