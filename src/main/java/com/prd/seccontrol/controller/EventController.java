package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.EventAuditDto;
import com.prd.seccontrol.service.impl.EventService;
import com.prd.seccontrol.util.SEConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {

  @Autowired
  private EventService eventService;

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/events/all")
  public Page<EventAuditDto> findAllAudits(@RequestParam(required = false) String term, Pageable pageable) {
    return eventService.findAllAudits(term, pageable);
  }
}
