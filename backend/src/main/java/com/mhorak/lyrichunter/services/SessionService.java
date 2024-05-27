package com.mhorak.lyrichunter.services;

import com.mhorak.lyrichunter.repositories.SessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void deleteOldSessions() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(1);
        sessionRepository.deleteOldSessions(cutoff);
    }
}
