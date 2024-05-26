package com.mhorak.lyrichunter.models;

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
public class Session {
    @Id
    private UUID sessionId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Song song;

    @Column(length = 10000)
    private String guess;

    private boolean guessed;
}
