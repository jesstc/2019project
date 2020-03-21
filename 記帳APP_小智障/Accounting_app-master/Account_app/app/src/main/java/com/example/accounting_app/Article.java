package com.example.accounting_app;

import java.util.HashMap;
import java.util.Map;

public class Article {
    public String author;
    public String preface;
    public String publisher;
    public String title;

    public Map<String, Boolean> stars = new HashMap<>();

    public Article() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Article(String author, String preface, String publisher, String title) {
        this.author = author;
        this.preface = preface;
        this.publisher = publisher;
        this.title = title;
    }

    public String getAuthor() { return author; }
    public String getPreface() { return preface; }
    public String getPublisher() { return publisher; }
    public String getTitle() { return title; }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("author", author);
        result.put("preface", preface);
        result.put("publisher", publisher);
        result.put("title", title);
        return result;
    }
}