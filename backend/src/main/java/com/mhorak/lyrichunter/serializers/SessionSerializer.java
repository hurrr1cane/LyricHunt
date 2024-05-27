package com.mhorak.lyrichunter.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mhorak.lyrichunter.models.Session;
import com.mhorak.lyrichunter.models.Song;

import java.io.IOException;

public class SessionSerializer extends JsonSerializer<Session> {

    @Override
    public void serialize(Session session, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("sessionId", session.getSessionId().toString());
        gen.writeStringField("guess", session.getGuess());
        gen.writeBooleanField("guessed", session.isGuessed());

        Song song = session.getSong();
        if (song != null) {
            gen.writeObjectFieldStart("song");
            gen.writeStringField("artist", song.getArtist());
            gen.writeStringField("title", song.getTitle());
            gen.writeStringField("lyrics", song.getLyrics());
            gen.writeEndObject();
        }

        gen.writeEndObject();
    }
}
