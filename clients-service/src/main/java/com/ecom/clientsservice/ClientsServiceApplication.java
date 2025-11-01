package com.ecom.clientsservice;

import com.ecom.clientsservice.dto.ClientDto;
import com.ecom.clientsservice.entite.Client;
import com.ecom.clientsservice.service.ClientService;
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
    ApplicationRunner init(ClientService clientService) {
        return args -> {
            clientService.saveClient(new Client(null, "amine@email.com", "Amin jamali"));
            clientService.saveClient(new Client(null, "Jad@email.com", "Jad Fahri"));
            clientService.saveClient(new Client(null, "khalil@email.com", "khalil Salim"));
        };
    }

}
