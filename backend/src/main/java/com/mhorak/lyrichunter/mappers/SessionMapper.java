package com.mhorak.lyrichunter.mappers;

import com.mhorak.lyrichunter.models.Session;
import com.mhorak.lyrichunter.models.dtos.SessionDto;
import org.springframework.stereotype.Component;

@Component
public class SessionMapper implements Mapper<Session, SessionDto>{
    @Override
    public SessionDto mapTo(Session session) {
        return SessionDto.builder()
                .sessionId(session.getSessionId())
                .guess(session.getGuess())
                .guessed(session.isGuessed())
                .artist(session.getSong().getArtist())
                .build();
    }

    @Override
    public Session mapFrom(SessionDto sessionDto) {
        return Session.builder()
                .sessionId(sessionDto.getSessionId())
                .guess(sessionDto.getGuess())
                .guessed(sessionDto.isGuessed())
                .build();
    }
}
