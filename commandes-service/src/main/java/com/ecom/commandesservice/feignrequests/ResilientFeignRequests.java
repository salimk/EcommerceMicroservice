package com.ecom.commandesservice.feignrequests;

import feign.FeignException;
import feign.Response;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
public class ResilientFeignRequests {
    private final ClientFeignRequest clientFeignRequest;
    private final ProduitFeignRequest produitFeignRequest;

    public ResilientFeignRequests(ClientFeignRequest clientFeignRequest, ProduitFeignRequest produitFeignRequest) {
        this.clientFeignRequest = clientFeignRequest;
        this.produitFeignRequest = produitFeignRequest;
    }

    @CircuitBreaker(name = "clients-service", fallbackMethod = "getClientByIdFallback")
    @Retry(name = "clients-service")
    public ResponseEntity<ClientDto> getClientById(Long id){
        try{
            return clientFeignRequest.getClientById(id);
        } catch (FeignException feignex){
            throw feignex;
        }
    }

    @CircuitBreaker(name = "catalogue-service", fallbackMethod = "getProduitByIdFallback")
    @Retry(name = "catalogue-service")
    public ResponseEntity<ProduitDto> getProduitById(Long id){
        try{
            return produitFeignRequest.getProduitById(id);
        } catch (FeignException feignex){
            throw feignex;
        }
    }

    private ResponseEntity<ClientDto> getClientByIdFallback(Long id, Exception ex){
        throw new RuntimeException("client-service n'est pas disponible");
    }

    private ResponseEntity<ProduitDto> getProduitByIdFallback(Long id, Exception ex){
        throw new RuntimeException("catalogue-service n'est pas disponible");
    }


}
