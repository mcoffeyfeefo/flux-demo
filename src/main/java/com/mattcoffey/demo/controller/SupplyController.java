package com.mattcoffey.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mattcoffey.demo.service.TruckloadsService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class SupplyController {

  private final TruckloadsService service;

  @GetMapping("/supplyDepot/{name}/totalTruckLoads")
  public Mono<Integer> totalTruckLoads(@PathVariable String name) {
    return service.totalTruckloads(name);
  }
}
