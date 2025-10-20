package com.ecom.clientsservice;

import com.ecom.clientsservice.entite.Client;
import com.ecom.clientsservice.repository.ClientRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ClientsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientsServiceApplication.class, args);
    }
    @Bean
    ApplicationRunner init(ClientRepository clientService) {
        return args -> {
            clientService.save(new Client(null, "Salim S", "salim@email.com"));
            clientService.save(new Client(null, "Amine A", "amine@email.com"));
        };
    }
}
