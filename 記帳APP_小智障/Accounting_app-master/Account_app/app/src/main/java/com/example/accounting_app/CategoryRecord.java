package com.example.accounting_app;

import java.util.HashMap;
import java.util.Map;

public class CategoryRecord {
    public String category;
    public Integer price;
    public String note;

    public Map<String, Boolean> stars = new HashMap<>();

    public CategoryRecord() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public CategoryRecord(String category, Integer price, String note) {
        this.category = category;
        this.price = price;
        this.note = note;
    }
    public String getCategory() { return category; }
    public Integer getPrice() { return price; }
    public String getNote() {
        return note;
    }
}
