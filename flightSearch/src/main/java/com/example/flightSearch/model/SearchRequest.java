package com.example.flightSearch.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequest {
    private String originLocationCode;
    private String destinationLocationCode;
    private String departureDate;
    private String returnDate;
    private Integer adults;
    private String currencyCode;
}
