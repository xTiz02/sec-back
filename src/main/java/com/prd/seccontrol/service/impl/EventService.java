package com.prd.seccontrol.service.impl;

import com.prd.seccontrol.model.dto.EventAuditDto;
import com.prd.seccontrol.repository.EventRepository;
import com.prd.seccontrol.util.SEConstants;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EventService {

  @Autowired
  private EventRepository eventRepository;

  public Page<EventAuditDto> findAllAudits(String term, Pageable pageable) {
    return eventRepository.findAuditEvents(term,pageable).map(e->{
      for (List<Object> endpoint : SEConstants.endpointsMatcher) {

        String regex = (String) endpoint.get(0);
        String method = (String) endpoint.get(1);
        String description = (String) endpoint.get(2);
        Boolean showJson = (Boolean) endpoint.get(3);

        String fullRegex = ".*" + regex;

        if (e.getMethod().equalsIgnoreCase(method)
            && e.getUri().matches(fullRegex)) {

          e.setDescription(description);

          if (!showJson) {
            e.setJson(null);
          }

          break;
        }
      }

      return new EventAuditDto(e);
    });
  }
}
