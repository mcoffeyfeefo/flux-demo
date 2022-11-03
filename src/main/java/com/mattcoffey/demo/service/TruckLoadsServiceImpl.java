package com.mattcoffey.demo.service;

import org.springframework.stereotype.Service;

import com.mattcoffey.demo.client.ApiClient;
import com.mattcoffey.demo.client.ApiClientFactory;
import com.mattcoffey.demo.dto.Category;
import com.mattcoffey.demo.dto.Supply;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class TruckLoadsServiceImpl implements TruckloadsService {

  // Truck capacity in kg
  private static int TRUCK_CAPACITY = 500;

  private final ApiClientFactory clientFactory;

  @Override
  public Mono<Integer> totalTruckloads(String supplyDepotName) {
      ApiClient client = clientFactory.createClient();
      Flux<Category> categoryFlux = client.getCategories();
      Flux<Supply> supplyFlux = client.getSupplies(supplyDepotName);
      return totalWeight(categoryFlux, supplyFlux)
          .map(totalWeight -> totalWeight / TRUCK_CAPACITY);
  }

  public Mono<Integer> totalWeight(Flux<Category> categoryFlux, Flux<Supply> supplyFlux) {
    return Flux
        .zip(categoryFlux, supplyFlux)
        .map(categorySupply ->
            categorySupply.getT1().getWeightPerBox() * categorySupply.getT2().getNumberOfBoxes())
        .reduce((a, b) -> a + b);
  }
}