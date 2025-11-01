package com.ecom.commandesservice.controller;

import com.ecom.commandesservice.dto.CommandeDto;
import com.ecom.commandesservice.dto.CommandeDtoDetail;
import com.ecom.commandesservice.dto.CreateCommandeDto;
import com.ecom.commandesservice.entite.Commande;
import com.ecom.commandesservice.mapper.CommandeMapper;
import com.ecom.commandesservice.mapper.CommandeMapperDetail;
import com.ecom.commandesservice.service.CommandeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/commandes")
public class CommandeController {
    private final CommandeService commandeService;
    private final CommandeMapper CommandeMapper;
    private final CommandeMapperDetail CommandeMapperDetail;

    public CommandeController(CommandeService commandeService, CommandeMapper commandeMapper, CommandeMapperDetail commandeMapperDetail){
        this.commandeService = commandeService;
        CommandeMapper = commandeMapper;
        CommandeMapperDetail = commandeMapperDetail;
    }

    @GetMapping
    public ResponseEntity<List<CommandeDto>> getlistCommandes(){
        return ResponseEntity.ok(commandeService.listallCommande().stream().map(CommandeMapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeDtoDetail> getCommandeById(@PathVariable Long id){
        return ResponseEntity.ok(CommandeMapperDetail.toDto(commandeService.getCommandeById(id)));
    }

    @PostMapping("/new")
    public ResponseEntity<?> addCommande(@RequestBody CreateCommandeDto dtocommande){
        return commandeService.ajouterCommande(CommandeMapper.toEntity(dtocommande));
    }

}
