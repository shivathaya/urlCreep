package com.urlcreep.webcrawler.util;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlUtils {
	
	public static boolean isValidUrl(String url) {
		try {
			new URL(url);
			return url.startsWith("http");
			
		} catch (MalformedURLException e) {
			return false;
		}
	}
	
	public static boolean isSameDomain(String base, String other) {
		try {
			URL b = new URL(base);
			URL o = new URL(other);
			
			return b.getHost().equalsIgnoreCase(o.getHost());
		} catch (MalformedURLException e) {
			return false;
		}
	}

}
