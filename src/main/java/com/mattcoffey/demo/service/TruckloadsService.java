package com.mattcoffey.demo.service;

import reactor.core.publisher.Mono;

public interface TruckloadsService {
  Mono<Integer> totalTruckloads(String supplyDepotName);
}
