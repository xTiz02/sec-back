package com.prd.seccontrol.service.impl;

import com.prd.seccontrol.model.dto.CreateClientRequest;
import com.prd.seccontrol.model.entity.Client;
import com.prd.seccontrol.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

  @Autowired
  private ClientRepository clientRepository;

  public Client createClient(CreateClientRequest request) {
    Client client = new Client();
    client.setName(request.name());
    client.setCode(request.code());
    client.setDirection(request.direction());
    client.setDescription(request.description());
    client.setActive(request.active());

    return clientRepository.save(client);
  }

  public Client updateClient(CreateClientRequest request, Long id) throws Exception {
    Client client = clientRepository.findById(id)
        .orElseThrow(() -> new Exception("Client not found with id: " + id));
    client.setName(request.name() != null ? request.name() : client.getName());
    client.setCode(request.code() != null ? request.code() : client.getCode());
    client.setDirection(request.direction() != null ? request.direction() : client.getDirection());
    client.setDescription(request.description() != null ? request.description() : client.getDescription());
    client.setActive(request.active() != null ? request.active() : client.isActive());

    return clientRepository.save(client);
  }
}
