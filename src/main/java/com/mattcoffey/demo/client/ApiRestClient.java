package com.mattcoffey.demo.client;

import java.util.Comparator;
import java.util.List;

import org.springframework.web.reactive.function.client.WebClient;

import com.mattcoffey.demo.dto.Category;
import com.mattcoffey.demo.dto.Categories;
import com.mattcoffey.demo.dto.Supply;
import com.mattcoffey.demo.dto.SupplyDepot;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class ApiRestClient implements ApiClient {

  private final WebClient rest;

  @Override
  public Flux<Category> getCategories() {
    return rest
        .get()
        .uri(getCategoriesUri())
        .retrieve()
        .bodyToFlux(Categories.class)
        .flatMapIterable(categories -> sortByName(categories));
  }

  private List<Category> sortByName(Categories categories) {
    List<Category> categoryList = categories.getCategories();
    categoryList.sort(Comparator.comparing(Category::getName));
    return categoryList;
  }

  private String getCategoriesUri() {
    return "/categories";
  }

  @Override
  public Flux<Supply> getSupplies(String supplyDepotName) {
    return rest
        .get()
        .uri(getSupplyDepotUri(supplyDepotName))
        .retrieve()
        .bodyToMono(SupplyDepot.class)
        .flatMapIterable(depot -> sortByName(depot));
  }

  private List<Supply> sortByName(SupplyDepot supplies) {
    List<Supply> supplyList = supplies.getSupplies();
    supplyList.sort(Comparator.comparing(Supply::getName));
    return supplyList;
  }

  private String getSupplyDepotUri(String supplyDepotName) {
    return "/supplyDepots/"
        + supplyDepotName;
  }
}
