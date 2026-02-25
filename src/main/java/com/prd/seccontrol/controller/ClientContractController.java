package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.ClientContractDto;
import com.prd.seccontrol.model.dto.CreateClientContractRequest;
import com.prd.seccontrol.model.entity.Client;
import com.prd.seccontrol.model.entity.ClientContract;
import com.prd.seccontrol.repository.ClientContractRepository;
import com.prd.seccontrol.repository.ClientRepository;
import com.prd.seccontrol.service.impl.SearchService;
import com.prd.seccontrol.util.SEConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientContractController {

  @Autowired
  private SearchService<ClientContract> searchService;

  @Autowired
  private ClientRepository clientRepository;

  @Autowired
  private ClientContractRepository clientContractRepository;

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/client-contract/all")
  public Page<ClientContractDto> findAll(@RequestParam(required = false) String query, Pageable pageable) {
    return searchService.search(query, pageable, ClientContract.class).map(ClientContractDto::new);
  }

  @PostMapping(SEConstants.SECURE_BASE_ENDPOINT + "/client-contract")
  public ClientContractDto createClientContract(@RequestBody CreateClientContractRequest request) {
    ClientContract clientContract = new ClientContract();
    Client client = clientRepository.findById(request.clientId())
        .orElseThrow(() -> new RuntimeException("Client not found with id: " + request.clientId()));
    clientContract.setClientId(client.getId());
    clientContract.setName(request.name());
    clientContract.setDescription(request.description());
    clientContract.setActive(request.active());
    clientContract = clientContractRepository.save(clientContract);
    return new ClientContractDto(clientContract);
  }

  @PutMapping(SEConstants.SECURE_BASE_ENDPOINT + "/client-contract/{id}")
  public ClientContractDto updateClientContract(@RequestBody CreateClientContractRequest request, @PathVariable Long id) throws Exception {
    ClientContract clientContract = clientContractRepository.findById(id)
        .orElseThrow(() -> new Exception("ContractUnity not found with id: " + id));

    Client client = clientRepository.findById(request.clientId())
        .orElseThrow(() -> new RuntimeException("Client not found with id: " + request.clientId()));
    clientContract.setClientId(client.getId() != null ? client.getId() : clientContract.getClientId());
    clientContract.setName(request.name() != null ? request.name() : clientContract.getName());
    clientContract.setDescription(request.description() != null ? request.description() : clientContract.getDescription());
    clientContract.setActive(request.active() != null ? request.active() : clientContract.getActive());
    clientContract = clientContractRepository.save(clientContract);
    return new ClientContractDto(clientContract);
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/client-contract/{id}")
  public ClientContractDto findById(@PathVariable Long id) throws Exception {
    ClientContract contractUnity = clientContractRepository.findById(id)
        .orElseThrow(() -> new Exception("ContractUnity not found with id: " + id));
    return new ClientContractDto(contractUnity);
  }

  @DeleteMapping(SEConstants.SECURE_BASE_ENDPOINT + "/client-contract/{id}")
  public Long deleteClientContract(@PathVariable Long id) throws Exception {
    ClientContract contractUnity = clientContractRepository.findById(id)
        .orElseThrow(() -> new Exception("ContractUnity not found with id: " + id));
    clientContractRepository.delete(contractUnity);
    return id;
  }


}
