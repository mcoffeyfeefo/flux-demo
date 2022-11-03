package com.mattcoffey.demo.client;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ApiClientFactoryImpl implements ApiClientFactory {

  private final WebClientFactory webClientFactory;
  public ApiClient createClient() {
    return new ApiRestClient(webClientFactory.createClient());
  }
}
