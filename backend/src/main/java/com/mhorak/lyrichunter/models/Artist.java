package com.mhorak.lyrichunter.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Artist {
    private Long id;
    private String name;
    private Iterable<Long> songs;
}
