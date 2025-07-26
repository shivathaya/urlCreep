package com.urlcreep.webcrawler.model;

public class WebcrawledPage {
	private String url;
	private int depth;
	private String title;
	public WebcrawledPage(String url, int depth, String title) {
		super();
		this.url = url;
		this.depth = depth;
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	

}
