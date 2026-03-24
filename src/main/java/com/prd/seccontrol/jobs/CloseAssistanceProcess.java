package com.prd.seccontrol.jobs;

import com.prd.seccontrol.repository.DateGuardUnityAssignmentRepository;
import com.prd.seccontrol.repository.GuardAssignmentRepository;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CloseAssistanceProcess {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CloseAssistanceProcess.class);

  @Autowired
  private DateGuardUnityAssignmentRepository dateGuardUnityAssignmentRepository;

  @Autowired
  private GuardAssignmentRepository guardAssignmentRepository;

  @Scheduled(fixedDelay = 20 * 60 * 1000) // 20 min después de que terminó la ejecución anterior
  public void closeAssistanceProcess() {
    logger.info("Iniciando proceso programado CERRADO AUTOMATICO... : " + new Date());
    try {
      //
    } catch (Exception e) {
      logger.error("Error en proceso programado", e);
    }
    logger.info("Proceso programado finalizado CERRADO AUTOMATICO... : " + new Date());
  }
}
