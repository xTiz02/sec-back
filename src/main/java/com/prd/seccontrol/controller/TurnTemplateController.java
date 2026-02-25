package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.CreateTurnTemplateRequest;
import com.prd.seccontrol.model.entity.TurnTemplate;
import com.prd.seccontrol.repository.TurnTemplateRepository;
import com.prd.seccontrol.service.impl.SearchService;
import com.prd.seccontrol.service.impl.TurnTemplateService;
import com.prd.seccontrol.util.SEConstants;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TurnTemplateController {
  @Autowired
  private TurnTemplateRepository turnTemplateRepository;

  @Autowired
  private SearchService<TurnTemplate> searchService;

  @Autowired
  private TurnTemplateService turnTemplateService;

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/turn-template/all")
  public Page<TurnTemplate> findAll(@RequestParam(required = false) String query, Pageable pageable) {
    return searchService.search(query, pageable, TurnTemplate.class);
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/turn-template/list-all")
  public List<TurnTemplate> listAll() {
    return turnTemplateRepository.findAll();
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/turn-template/{id}")
  public TurnTemplate findById(@PathVariable Long id) {
    return turnTemplateRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TurnTemplate not found with id: " + id));
  }

  @PostMapping(SEConstants.SECURE_BASE_ENDPOINT + "/turn-template")
  public TurnTemplate createTurnTemplate(@RequestBody CreateTurnTemplateRequest request) throws Exception {
    return turnTemplateService.createTurnTemplate(request);
  }

  @PutMapping(SEConstants.SECURE_BASE_ENDPOINT + "/turn-template/{id}")
  public TurnTemplate updateTurnTemplate(@RequestBody CreateTurnTemplateRequest request, @PathVariable Long id) throws Exception {
    return turnTemplateService.updateTurnTemplate(id, request);
  }

  @DeleteMapping(SEConstants.SECURE_BASE_ENDPOINT + "/turn-template/{id}")
  public Long deleteTurnTemplate(@PathVariable Long id) {
    TurnTemplate turnTemplate = turnTemplateRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TurnTemplate not found with id: " + id));
    turnTemplateRepository.delete(turnTemplate);
    return id;
  }

}
