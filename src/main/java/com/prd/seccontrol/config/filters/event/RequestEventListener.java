package com.prd.seccontrol.config.filters.event;

import com.prd.seccontrol.model.configdto.RequestCompletedEvent;
import com.prd.seccontrol.model.entity.Event;
import com.prd.seccontrol.model.entity.User;
import com.prd.seccontrol.repository.EventRepository;
import com.prd.seccontrol.repository.UserRepository;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class RequestEventListener {
  private static final Logger logger = LoggerFactory.getLogger(RequestEventListener.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EventRepository eventRepository;

  @Async("eventExecutor") // thread pool separado, no bloquea la request
  @EventListener
  public void handleRequestCompleted(RequestCompletedEvent evt) {
    try {
      String username = resolveUsername(evt.getAuthentication());

      String json = evt.getJson();
      Event event = new Event();
      event.setDuration(evt.getDuration());
      event.setJson(json != null && json.length() > 60_000
          ? json.substring(0, 60_000) : json);
      event.setParameters(evt.getParameters());
      event.setStartTime(new Date(evt.getStartTime()));
      event.setUrl(truncate(evt.getUrl(), 100));
      event.setMethod(evt.getMethod());
      event.setUri(truncate(evt.getUri(), 100));
      event.setResponseStatus(evt.getResponseStatus());

      if (!"anonymousUser".equals(username)) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) event.setUserId(user.getId());
      }

      eventRepository.save(event);

    } catch (Exception e) {
      logger.error("Failed to save request event for uri={}", evt.getUri(), e);
    }
  }

  private String resolveUsername(Authentication auth) {
    return (auth != null && auth.isAuthenticated()) ? auth.getName() : "anonymousUser";
  }

  private String truncate(String s, int length) {
    return s == null ? null : s.substring(0, Math.min(s.length(), length));
  }
}
