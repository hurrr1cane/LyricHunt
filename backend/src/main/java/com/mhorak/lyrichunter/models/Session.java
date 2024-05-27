package com.mhorak.lyrichunter.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mhorak.lyrichunter.serializers.SessionSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonSerialize(using = SessionSerializer.class)
public class Session {
    @Id
    private UUID sessionId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Song song;

    @Column(length = 10000)
    private String guess;

    private boolean guessed;
}
