package com.example.seekr;

public class OneComment {
	public boolean left;
	public String comment;
	public String userName = "Anonymous";
	public String datetime;
	
	public OneComment(boolean left, String comment) {
		super();
		this.left = left;
		this.comment = comment;
	}
	
	public OneComment(boolean left, String comment, String userName) {
		super();
		this.left = left;
		this.comment = comment;
		this.userName = userName;
	}

}