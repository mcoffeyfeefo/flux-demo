package com.mattcoffey.demo.client

import com.mattcoffey.demo.dto.Category
import com.mattcoffey.demo.dto.Supply
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import spock.lang.Specification

import org.apache.commons.io.IOUtils
import org.springframework.web.reactive.function.client.WebClient

import static org.springframework.http.HttpHeaders.CONTENT_TYPE
import static org.springframework.http.MediaType.APPLICATION_JSON

class ApiRestClientSpec extends Specification {

    static def CATEGORY_ONE = Category.builder()
        .name("parachutes")
        .weightPerBox(15)
        .amountPerBox(2)
        .build()

    static def CATEGORY_TWO = Category.builder()
        .name("rations")
        .weightPerBox(20)
        .amountPerBox(40)
        .build()

    static def CATEGORY_THREE = Category.builder()
        .name("shells")
        .weightPerBox(30)
        .amountPerBox(6)
        .build()

    static def SUPPLY_ONE = Supply.builder()
        .name("parachutes")
        .numberOfBoxes(0)
        .build()

    static def SUPPLY_TWO = Supply.builder()
        .name("rations")
        .numberOfBoxes(300)
        .build()

    static def SUPPLY_THREE = Supply.builder()
        .name("shells")
        .numberOfBoxes(20)
        .build()

    def server = new MockWebServer()
    def client = WebClient.builder().baseUrl(server.url("").toString()).build()
    def tested = new ApiRestClient(client);
    def actual

    def "should retrieve and parse supply categories"() {
        given: "API responds with $responseFile"
            server.enqueue(new MockResponse()
                .setResponseCode(status)
                .setHeader(CONTENT_TYPE, APPLICATION_JSON)
                .setBody(readJsonFile(responseFile)));
        when: "the categories are retrieved"
            actual = tested.getCategories().collectList().block()
        then: "the result should be a Flux of category $expected"
            actual == expected
        where:
        responseFile           | status | expected
        "data/categories.json" | 200    | List.of(CATEGORY_ONE, CATEGORY_TWO, CATEGORY_THREE)
    }

    def "should retrieve and parse supplies"() {
        given: "API responds with $responseFile"
            server.enqueue(new MockResponse()
                .setResponseCode(status)
                .setHeader(CONTENT_TYPE, APPLICATION_JSON)
                .setBody(readJsonFile(responseFile)));
        when: "the supplies are retrieved"
            actual = tested.getSupplies("Depot 1").collectList().block()
        then: "the result should be a Flux of Supply $expected"
            actual == expected
        where:
        responseFile            | status | expected
        "data/supplyDepot.json" | 200    | List.of(SUPPLY_ONE, SUPPLY_TWO, SUPPLY_THREE)
    }

    String readJsonFile(String path) {
        BufferedReader rd = new BufferedReader(
            new InputStreamReader(Objects.requireNonNull(Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream(path))));
        StringBuilder sb = new StringBuilder();
        try {
            String ln;
            while (null != (ln = rd.readLine())) {
                sb.append(ln).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(
                "Cannot read resource '" + path + "'.");
        } finally {
            IOUtils.closeQuietly(rd);
        }
    }
}