package com.example.flightSearch.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Flight {
    private String originLocationCode;
    private String destinationLocationCode;
    private String departureDate;
    private String returnDate;
    private Integer stopsOutbound;
    private Integer stopsReturn;
    private Integer numberOfPassengers;
    private String currencyCode;
    private Double totalPrice;
}
