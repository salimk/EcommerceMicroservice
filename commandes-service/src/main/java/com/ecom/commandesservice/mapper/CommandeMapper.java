package com.ecom.commandesservice.mapper;

import com.ecom.commandesservice.dto.CommandeDto;
import com.ecom.commandesservice.entite.Commande;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommandeMapper {
    Commande toEntity(CommandeDto commandeDto);

    CommandeDto toDto(Commande commande);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Commande partialUpdate(CommandeDto commandeDto, @MappingTarget Commande commande);
}