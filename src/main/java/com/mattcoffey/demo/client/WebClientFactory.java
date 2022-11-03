package com.mattcoffey.demo.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
@RequiredArgsConstructor
@Component
public class WebClientFactory {

  @Value("${client.baseUrl}")
  private String baseUrl;

  public WebClient createClient() {
    return WebClient.builder()
        .baseUrl(baseUrl)
        .defaultHeader(CONTENT_TYPE, APPLICATION_JSON.toString())
        .build();
  }
}
