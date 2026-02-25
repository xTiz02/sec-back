package com.prd.seccontrol.service.impl;

import com.prd.seccontrol.model.dto.CreateTurnTemplateRequest;
import com.prd.seccontrol.model.entity.TurnTemplate;
import com.prd.seccontrol.repository.TurnTemplateRepository;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TurnTemplateService {

  @Autowired
  private TurnTemplateRepository turnTemplateRepository;

  public TurnTemplate createTurnTemplate(CreateTurnTemplateRequest request) throws Exception {
    // Preferimos aceptar horas en formato "HH:mm" (por ejemplo "08:30") desde el front
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    LocalTime from;
    LocalTime to;
    try {
      if (request.timeFrom() == null || request.timeTo() == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "timeFrom and timeTo are required");
      }
      from = LocalTime.parse(request.timeFrom(), formatter);
      to = LocalTime.parse(request.timeTo(), formatter);
    } catch (DateTimeParseException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid time format. Expected HH:mm", e);
    }

    TurnTemplate turnTemplate = new TurnTemplate();
    turnTemplate.setName(request.name());
    turnTemplate.setNumGuards(request.numGuards());
    turnTemplate.setTimeFrom(from);
    turnTemplate.setTimeTo(to);
    turnTemplate.setTurnType(request.turnType());

    return turnTemplateRepository.save(turnTemplate);
  }

  public TurnTemplate updateTurnTemplate(Long id, CreateTurnTemplateRequest request) throws Exception {
    TurnTemplate turnTemplate = turnTemplateRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TurnTemplate not found with id: " + id));

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    if (request.name() != null) {
      turnTemplate.setName(request.name());
    }
    if (request.numGuards() != null) {
      turnTemplate.setNumGuards(request.numGuards());
    }
    if (request.timeFrom() != null) {
      try {
        LocalTime from = LocalTime.parse(request.timeFrom(), formatter);
        turnTemplate.setTimeFrom(from);
      } catch (DateTimeParseException e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid timeFrom format. Expected HH:mm", e);
      }
    }
    if (request.timeTo() != null) {
      try {
        LocalTime to = LocalTime.parse(request.timeTo(), formatter);
        turnTemplate.setTimeTo(to);
      } catch (DateTimeParseException e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid timeTo format. Expected HH:mm", e);
      }
    }
    if (request.turnType() != null) {
      turnTemplate.setTurnType(request.turnType());
    }

    return turnTemplateRepository.save(turnTemplate);
  }
}
