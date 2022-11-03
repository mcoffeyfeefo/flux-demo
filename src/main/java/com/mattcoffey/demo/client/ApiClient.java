package com.mattcoffey.demo.client;

import com.mattcoffey.demo.dto.Category;
import com.mattcoffey.demo.dto.Supply;
import reactor.core.publisher.Flux;

public interface ApiClient {

  Flux<Category> getCategories();
  Flux<Supply> getSupplies(String categoryName);
}
