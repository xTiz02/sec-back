package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.GuardShiftDetailDto;
import com.prd.seccontrol.model.dto.LiveAssistanceEventDto;
import com.prd.seccontrol.model.dto.UnitMonitoringStatusDto;
import com.prd.seccontrol.service.impl.MonitoringService;
import com.prd.seccontrol.util.SEConstants;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitoringController {

  @Autowired
  private MonitoringService monitoringService;


  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/monitoring/unit-status")
  public List<UnitMonitoringStatusDto> getUnitMonitoringStatus(@RequestParam String date) {

    return monitoringService.getUnitMonitoringStatus(LocalDate.parse(date));
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/monitoring/unit-shifts")
  public List<GuardShiftDetailDto> getUnitShiftDetails(@RequestParam Long contractUnityId,
      @RequestParam String date) {
    return monitoringService.getUnitShiftDetails(contractUnityId, LocalDate.parse(date));
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/monitoring/live-assists")
  public List<LiveAssistanceEventDto> getLiveAssists(
      @RequestParam String date) {
    return monitoringService.getLiveAssists(LocalDate.parse(date));
  }

}
