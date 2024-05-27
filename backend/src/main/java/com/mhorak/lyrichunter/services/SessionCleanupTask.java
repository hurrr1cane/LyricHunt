package com.mhorak.lyrichunter.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SessionCleanupTask {

    private final SessionService sessionService;

    public SessionCleanupTask(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Scheduled(fixedRate = 3600000) // Run every hour
    public void cleanUpOldSessions() {
        sessionService.deleteOldSessions();
    }
}
