package com.ecom.clientsservice.service;

import com.ecom.clientsservice.entite.Client;
import com.ecom.clientsservice.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client getClientById(Long id){
        return clientRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
    public List<Client> getAllClient(){
        return clientRepository.findAll();
    }

    public void saveClient(Client client){
        clientRepository.save(client);
    }
}
