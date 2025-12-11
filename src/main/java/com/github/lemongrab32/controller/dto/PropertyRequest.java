package com.github.lemongrab32.controller.dto;

import jakarta.validation.constraints.NotNull;

public record PropertyRequest(@NotNull String name, @NotNull Object value) {
}
