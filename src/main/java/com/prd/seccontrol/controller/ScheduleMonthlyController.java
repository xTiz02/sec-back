package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.GenerateMonthScheduleRequest;
import com.prd.seccontrol.model.entity.ScheduleMonthly;
import com.prd.seccontrol.repository.ScheduleMonthlyRepository;
import com.prd.seccontrol.service.inter.SearchService;
import com.prd.seccontrol.util.SEConstants;
import java.time.Month;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScheduleMonthlyController {

  @Autowired
  private SearchService<ScheduleMonthly> searchService;

  @Autowired
  private ScheduleMonthlyRepository scheduleMonthlyRepository;

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/schedule-monthly/all")
  public Page<ScheduleMonthly> findAll(@RequestParam(required = false) String query, Pageable pageable) {
    return searchService.search(query, pageable, ScheduleMonthly.class);
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/schedule-monthly")
  public Page<ScheduleMonthly> findAllMonthly(@RequestParam(required = false) String query, Pageable pageable) {
    return scheduleMonthlyRepository.findAll(pageable);
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/schedule-monthly/{id}")
  public ScheduleMonthly findById(@PathVariable Long id) {
    return scheduleMonthlyRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("ScheduleMonthly not found with id: " + id));
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/schedule-monthly/by-period")
  public ScheduleMonthly findByMonth(@RequestParam Month month, @RequestParam Integer year) {
    ScheduleMonthly scheduleMonthly = scheduleMonthlyRepository.findByMonthAndYear(month, year)
        .orElseThrow(() -> new RuntimeException("ScheduleMonthly not found for month: " + month + " and year: " + year));
    return scheduleMonthly;
  }

  @PostMapping(SEConstants.SECURE_BASE_ENDPOINT + "/schedule-monthly/generate-month")
  public ScheduleMonthly save(@RequestBody GenerateMonthScheduleRequest request) {
    ScheduleMonthly scheduleMonthly = scheduleMonthlyRepository.findByMonthAndYear(request.month(), request.year()).orElse(null);
    if (scheduleMonthly != null) {
      throw new RuntimeException("Schedule for month " + request.month() + " and year " + request.year() + " already exists.");
    }
    scheduleMonthly = new ScheduleMonthly();
    scheduleMonthly.setMonth(request.month());
    scheduleMonthly.setYear(request.year());
    scheduleMonthly.setDescription(request.scheduleDescription());
    scheduleMonthly.setName(request.scheduleName());

    return scheduleMonthlyRepository.save(scheduleMonthly);
  }
}
