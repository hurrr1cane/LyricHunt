package com.mhorak.lyrichunter.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionDto {
    private UUID sessionId;
    private String guess;
    private boolean guessed;
}
