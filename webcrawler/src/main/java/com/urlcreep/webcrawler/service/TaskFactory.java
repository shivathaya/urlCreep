package com.urlcreep.webcrawler.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import com.urlcreep.webcrawler.observer.WebCrawlObserver;

public class TaskFactory {
	
	private final Set<String> visited;
	private final ExecutorService executor;
	private final int maxDepth;
	private final List<WebCrawlObserver> observers = new CopyOnWriteArrayList<>();
    private final AtomicInteger activeTaskCount;
    private final CountDownLatch latch;
	public TaskFactory(Set<String> visited, ExecutorService executor, int maxDepth, AtomicInteger activeTaskCount, CountDownLatch latch) {
		super();
		this.visited = visited;
		this.executor = executor;
		this.maxDepth = maxDepth;
		this.activeTaskCount = activeTaskCount;
		this.latch = latch;
	}
	
	public void addObserver(WebCrawlObserver observer) {
		observers.add(observer);
		
	}
	
	public void notifyAllObservers(String url, int depth, String title) {
		for(WebCrawlObserver o: observers)
			o.onWebUrlCrawled(url, depth, title);
	}
	
	public WebcrawlerTask createTask(String url, int depth) {
		return new WebcrawlerTask(url, depth, maxDepth, visited, executor, this, activeTaskCount, latch);
	}
}
