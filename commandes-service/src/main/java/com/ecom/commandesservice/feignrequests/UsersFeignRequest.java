package com.ecom.commandesservice.feignrequests;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "clients-service")
public interface UsersFeignRequest {
    @GetMapping("/clients/{id}")
    ResponseEntity<ClientDto> getClientById(@PathVariable Long id);

}
