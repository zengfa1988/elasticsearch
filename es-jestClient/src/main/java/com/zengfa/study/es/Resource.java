package com.zengfa.study.es;

import java.util.Date;

public class Resource {

	private String title;
	
	private String author;
	
	private String content;
	
	private Date publishDate;

	public Resource() {}
	
	public Resource(String title, String author, String content, Date publishDate) {
		super();
		this.title = title;
		this.author = author;
		this.content = content;
		this.publishDate = publishDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	
}
