package com.example.flightSearch.controller;

import com.example.flightSearch.model.Flight;
import com.example.flightSearch.model.SearchRequest;
import com.example.flightSearch.service.FlightSearchService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

//@Controller
@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "http://localhost:3000")
public class FlightSearchController {

    private final FlightSearchService flightSearchService;

    public FlightSearchController(FlightSearchService flightSearchService) {
        this.flightSearchService = flightSearchService;
    }


    @GetMapping("/")
    public String showSearchForm(Model model) {
        model.addAttribute("searchRequest", new SearchRequest());
        model.addAttribute("flights", Collections.emptyList());
        return "index";
    }

    @PostMapping("/")
    public String searchFlights(SearchRequest searchRequest, Model model) {
        List<Flight> flights = flightSearchService.searchFlights(searchRequest);
        model.addAttribute("searchRequest", searchRequest);
        model.addAttribute("flights", flights);
        return "index";
    }

    @PostMapping("/search")
    public List<Flight> searchFlights(@RequestBody SearchRequest searchRequest) {
        return flightSearchService.searchFlights(searchRequest);
    }
}