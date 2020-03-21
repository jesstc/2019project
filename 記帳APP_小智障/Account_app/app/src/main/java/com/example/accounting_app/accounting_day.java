package com.example.accounting_app;

import java.util.HashMap;
import java.util.Map;

public class accounting_day {
    public Integer d_income;
    public Integer d_expend;

    public Map<String, Boolean> stars = new HashMap<>();

    public accounting_day() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public accounting_day(Integer d_income, Integer d_expend) {
        this.d_income = d_income;
        this.d_expend = d_expend;
    }

    public Integer getD_income() { return d_income; }
    public Integer getD_expend() { return d_expend; }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("d_income", d_income);
        result.put("d_expend", d_expend);
        return result;
    }
}
