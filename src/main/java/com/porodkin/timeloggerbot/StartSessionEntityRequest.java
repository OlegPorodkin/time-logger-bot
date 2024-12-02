package com.porodkin.timeloggerbot;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

public class StartSessionEntityRequest {
    private Long userId;
    private OffsetDateTime workSession;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public OffsetDateTime getWorkSession() {
        return workSession;
    }

    public void setWorkSession(OffsetDateTime workSessionTime) {
        this.workSession = workSessionTime;
    }
}
