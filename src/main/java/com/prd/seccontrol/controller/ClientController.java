package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.CreateClientRequest;
import com.prd.seccontrol.model.entity.Client;
import com.prd.seccontrol.repository.ClientRepository;
import com.prd.seccontrol.service.impl.ClientService;
import com.prd.seccontrol.service.impl.SearchService;
import com.prd.seccontrol.util.SEConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

  @Autowired
  private ClientRepository clientRepository;

  @Autowired
  private SearchService<Client> searchService;

  @Autowired
  private ClientService clientService;

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/client/all")
  public Page<Client> findAll(@RequestParam(required = false) String query, Pageable pageable) {
    return searchService.search(query, pageable, Client.class);
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/client/{id}")
  public Client findById(@PathVariable Long id) throws Exception {
    return clientRepository.findById(id)
        .orElseThrow(() -> new Exception("Client not found with id: " + id));
  }

  @PostMapping(SEConstants.SECURE_BASE_ENDPOINT + "/client")
  public Client createClient(@RequestBody CreateClientRequest request) {
    return clientService.createClient(request);
  }

  @PutMapping(SEConstants.SECURE_BASE_ENDPOINT + "/client/{id}")
  public Client updateClient(@RequestBody CreateClientRequest request, @PathVariable Long id) throws Exception {
    return clientService.updateClient(request, id);
  }
}