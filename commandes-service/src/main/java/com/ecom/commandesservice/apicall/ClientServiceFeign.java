package com.ecom.commandesservice.apicall;


import com.ecom.commandesservice.apicall.dtoResponse.ClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "clients-service")
public interface ClientServiceFeign {
    @GetMapping("/clients/{id}")
    ResponseEntity<ClientResponse> getProduitbyId(@PathVariable("id") Long id);
}
