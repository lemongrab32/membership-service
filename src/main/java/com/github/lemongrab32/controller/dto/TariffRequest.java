package com.github.lemongrab32.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.lemongrab32.model.ClientCategory;
import com.github.lemongrab32.model.ClientType;

@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public record TariffRequest(String name, Double basePrice, ClientCategory category, ClientType type) {}
