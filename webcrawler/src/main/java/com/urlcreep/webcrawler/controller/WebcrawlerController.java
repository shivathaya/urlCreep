package com.urlcreep.webcrawler.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.urlcreep.webcrawler.model.WebcrawledPage;
import com.urlcreep.webcrawler.service.WebcrawlerService;

@RestController
@RequestMapping("/urlCreep")
public class WebcrawlerController {
	
	private final WebcrawlerService webcrawlerService;
	
	public WebcrawlerController(WebcrawlerService webcrawlerService) {
		this.webcrawlerService = webcrawlerService;
	}
	
	@GetMapping("/webCrawl")
	public List<WebcrawledPage> crawl(@RequestParam String url, @RequestParam int depth)
	{
		return webcrawlerService.startCrawl(url, depth);
		
	}
}
