package com.urlcreep.webcrawler.service;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.urlcreep.webcrawler.util.UrlUtils;

public class WebcrawlerTask implements Runnable {

	private final String url;
	private int depth;
	private final int maxDepth;
	private final Set<String> visited;
	private final ExecutorService executor;
	private final TaskFactory taskFactory;
	private final AtomicInteger activeTaskCount;
	private final CountDownLatch latch;

	public WebcrawlerTask(String url, int depth, int maxDepth, Set<String> visited, ExecutorService executor,
			TaskFactory taskFactory, AtomicInteger activeTaskCount, CountDownLatch latch) {
		super();
		this.url = url;
		this.depth = depth;
		this.maxDepth = maxDepth;
		this.visited = visited;
		this.executor = executor;
		this.taskFactory = taskFactory;
		this.activeTaskCount = activeTaskCount;
		this.latch = latch;
	}

	@Override
	public void run() {
		if (depth > maxDepth || !visited.add(url))
			return;
		try {
			Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(10000).get();
			String title = doc.title();
			taskFactory.notifyAllObservers(url, depth, title);
			Elements links = doc.select("a[href]");

			if (depth < maxDepth) {
				for (Element link : links) {

					String absUrl = link.absUrl("href");

					if (UrlUtils.isValidUrl(absUrl) && UrlUtils.isSameDomain(absUrl, url)) {
						activeTaskCount.incrementAndGet();
						executor.submit(taskFactory.createTask(absUrl, depth + 1));
					}
				}
			}

		} catch (Exception e) {
			System.out.println("Failed: " + url + "--" + e.getMessage());
		} finally {
			if (activeTaskCount.decrementAndGet() == 0) {
				latch.countDown();
			}

		}
	}

}
