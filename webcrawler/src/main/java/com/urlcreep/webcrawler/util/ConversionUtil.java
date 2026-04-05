package com.urlcreep.webcrawler.util;

import org.springframework.http.ResponseEntity;

public class ConversionUtil {
    public static ResponseEntity<String> success(String message) {
        return ResponseEntity.ok(message);
    }
}
