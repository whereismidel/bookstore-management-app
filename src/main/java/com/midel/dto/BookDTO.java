package com.midel.dto;

import lombok.Builder;

@Builder
public record BookDTO(
        String id,
        String title,
        String author,
        String isbn,
        int quantity
) { }
