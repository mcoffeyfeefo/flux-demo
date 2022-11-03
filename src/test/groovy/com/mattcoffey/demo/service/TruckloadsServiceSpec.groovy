package com.mattcoffey.demo.service

import com.mattcoffey.demo.client.ApiClientFactoryImpl
import com.mattcoffey.demo.client.ApiRestClient
import com.mattcoffey.demo.dto.Category
import com.mattcoffey.demo.dto.Supply
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import spock.lang.Specification

class TruckloadsServiceSpec extends Specification {
    static def SUPPLY_DEPOT_NAME = "Depot 1"

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

    def client = Mock(ApiRestClient)
    def clientFactory = Stub(ApiClientFactoryImpl)
    def tested = new TruckLoadsServiceImpl(clientFactory)
    def actual

    def setup() {
        clientFactory.createClient() >> client
        0 * _
    }

    def "should calculate the number of trucks needed to move the supplies in a depot"() {
        given: "the weightPerBox for categories are 15 for parachutes, 20 for rations, 15 for shells"
            client.getCategories() >> Flux.just(CATEGORY_ONE, CATEGORY_TWO, CATEGORY_THREE)
        and: "the supply depot has numberOfBoxes 0 parachutes, 300 rations, 20 shells"
            client.getSupplies(SUPPLY_DEPOT_NAME) >> Flux.just(SUPPLY_ONE, SUPPLY_TWO, SUPPLY_THREE)
        when: "the number of truckloads are calculated"
            actual = tested.totalTruckloads(SUPPLY_DEPOT_NAME).block()
        then: "the result is 13 truckloads"
             13 == actual
    }
}