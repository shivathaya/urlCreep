package com.urlcreep.webcrawler.observer;

public interface WebCrawlObserver {
	void onWebUrlCrawled(String url, int depth, String title);

}
