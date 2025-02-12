package com.example.flightSearch.controller;

import com.example.flightSearch.dto.Flight;
import com.example.flightSearch.dto.SearchRequest;
import com.example.flightSearch.service.FlightSearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightSearchController {

    private final FlightSearchService flightSearchService;

    public FlightSearchController(FlightSearchService flightSearchService) {
        this.flightSearchService = flightSearchService;
    }

    @PostMapping("/search")
    public List<Flight> searchFlights(@RequestBody SearchRequest searchRequest) {
        return flightSearchService.searchFlights(searchRequest);
    }
}