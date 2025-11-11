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
    private final CommandeMapper commandeMapper;
    private final CommandeMapperDetail commandeMapperDetail;

    public CommandeController(CommandeService commandeService, CommandeMapper commandeMapper, CommandeMapperDetail commandeMapperDetail){
        this.commandeService = commandeService;
        this.commandeMapper = commandeMapper;
        this.commandeMapperDetail = commandeMapperDetail;
    }


@PostMapping("/new")
public ResponseEntity<?> createCommande(@RequestBody CreateCommandeDto createCommandeDto,
                                        @RequestHeader(name="idempotencyKey") String idempotency ){
        return commandeService.createCommande(commandeMapper.toEntity(createCommandeDto),idempotency);
}

    @GetMapping
    public ResponseEntity<List<CommandeDto>> getlistCommandes(){
        return ResponseEntity.ok(commandeService.listallCommande().stream().map(commandeMapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeDtoDetail> getCommandeById(@PathVariable Long id){
        return ResponseEntity.ok(commandeMapperDetail.toDto(commandeService.getCommandeById(id)));
    }


}
