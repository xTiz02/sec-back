package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.entity.View;
import com.prd.seccontrol.repository.ViewRepository;
import com.prd.seccontrol.util.SEConstants;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ViewController {

  @Autowired
  private ViewRepository viewRepository;

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/views/all")
  public List<View> findAll() {
    return viewRepository.findAll();
  }
}
