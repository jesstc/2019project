package com.example.accounting_app;

import java.util.HashMap;
import java.util.Map;

public class accounting_month {
    public Integer m_budget;
    public Integer m_income;
    public Integer m_expend;

    public Map<String, Boolean> stars = new HashMap<>();

    public accounting_month() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public accounting_month(Integer m_budget, Integer m_income, Integer m_expend) {
        this.m_budget = m_budget;
        this.m_income = m_income;
        this.m_expend = m_expend;
    }

    public Integer getM_budget() { return m_budget; }
    public Integer getM_income() { return m_income; }
    public Integer getM_expend() { return m_expend; }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("m_budget", m_budget);
        result.put("m_income", m_income);
        result.put("m_expend", m_expend);
        return result;
    }
}
