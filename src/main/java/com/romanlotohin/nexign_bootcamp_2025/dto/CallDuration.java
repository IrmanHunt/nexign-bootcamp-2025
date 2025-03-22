package com.romanlotohin.nexign_bootcamp_2025.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CallDuration {
    private String totalTime;

    public CallDuration(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }
}