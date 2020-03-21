package com.example.accounting_app;

import java.util.HashMap;
import java.util.Map;

public class Accounting_c_icon {
    public String logo_url;
    public String name;


    public Map<String, Boolean> stars = new HashMap<>();


    public Accounting_c_icon() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Accounting_c_icon(String logo_url, String name) {
        this.logo_url = logo_url;
        this.name = name;
    }
    public String getLogo_url() { return logo_url; }
    public String getName() {
        return name;
    }

}
