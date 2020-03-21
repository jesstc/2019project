package com.example.accounting_app;

import java.util.HashMap;
import java.util.Map;

public class User {
    public String role;

    public Map<String, Boolean> stars = new HashMap<>();

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String role) {
        this.role = role;
    }

    public String getRole() { return role; }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("role", role);
        return result;
    }
}
