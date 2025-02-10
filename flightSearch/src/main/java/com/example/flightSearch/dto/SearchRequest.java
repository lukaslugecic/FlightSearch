package com.example.flightSearch.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class SearchRequest {
    private String originLocationCode;
    private String destinationLocationCode;
    private String departureDate;
    private String returnDate;
    private Integer adults;
    private String currencyCode;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SearchRequest that = (SearchRequest) o;
        return Objects.equals(originLocationCode, that.originLocationCode)
                && Objects.equals(destinationLocationCode, that.destinationLocationCode)
                && Objects.equals(departureDate, that.departureDate)
                && Objects.equals(returnDate, that.returnDate)
                && Objects.equals(adults, that.adults)
                && Objects.equals(currencyCode, that.currencyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originLocationCode, destinationLocationCode, departureDate, returnDate, adults, currencyCode);
    }
}
