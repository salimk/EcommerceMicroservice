package com.ecom.commandesservice.feignrequests;

import org.apache.coyote.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "catalogue-service",contextId="produitFeignRequest")
public interface ProduitFeignRequest {
    @GetMapping("/produits/{id}")
    ResponseEntity<ProduitDto> getProduitById(@PathVariable Long id);
}
