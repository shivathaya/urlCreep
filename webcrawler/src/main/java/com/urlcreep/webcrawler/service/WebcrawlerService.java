package com.urlcreep.webcrawler.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.urlcreep.webcrawler.model.WebcrawledPage;
import com.urlcreep.webcrawler.observer.WebCrawlObserver;

@Service
public class WebcrawlerService implements WebCrawlObserver {

	private final Set<String> visited = Collections.newSetFromMap(new ConcurrentHashMap<>());
	private final List<WebcrawledPage> results = Collections.synchronizedList(new ArrayList<>());
	private final ExecutorService executor = Executors.newFixedThreadPool(25);
	private TaskFactory taskFactory;

	public List<WebcrawledPage> startCrawl(String url, int depth) {

		visited.clear();
		results.clear();
		AtomicInteger activeTaskCount = new AtomicInteger(1); // root task
		CountDownLatch latch = new CountDownLatch(1); // for waiting until all tasks done
		taskFactory = new TaskFactory(visited, executor, depth, activeTaskCount, latch);
		taskFactory.addObserver(this);

		executor.submit(taskFactory.createTask(url, 0));

		// shutdown

		try {
			latch.await(1, TimeUnit.MINUTES); // Wait for crawl to finish
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		executor.shutdown();

		try {
			executor.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO: handle exception
		}

		return results;
	}

	@Override
	public void onWebUrlCrawled(String url, int depth, String title) {
		results.add(new WebcrawledPage(url, depth, title));

	}

}
