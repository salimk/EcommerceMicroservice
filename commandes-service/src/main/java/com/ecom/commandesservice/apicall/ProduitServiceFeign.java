package com.ecom.commandesservice.apicall;

import com.ecom.commandesservice.apicall.dtoResponse.ProduitResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "catalogue-service")
public interface ProduitServiceFeign {

    @GetMapping("/produits/{id}")
    public ResponseEntity<ProduitResponse> getProduitbyId(@PathVariable("id") Long id);

}
