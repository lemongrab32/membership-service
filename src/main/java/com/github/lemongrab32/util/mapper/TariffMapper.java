package com.github.lemongrab32.util.mapper;

import com.github.lemongrab32.controller.dto.TariffDto;
import com.github.lemongrab32.model.Tariff;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TariffMapper {

	Tariff toTariff(TariffDto tariffDto);

	TariffDto toDto(Tariff tariff);

}
