package com.example.flightSearch.service;

import com.example.flightSearch.model.Flight;
import com.example.flightSearch.model.SearchRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlightSearchService {

    private final AmadeusApiService amadeusApiService;

    public FlightSearchService(AmadeusApiService amadeusApiService) {
        this.amadeusApiService = amadeusApiService;
    }

    public List<Flight> searchFlights(SearchRequest searchRequest) {
        Mono<String> flightOffersMono = amadeusApiService.getFlightOffers(
                searchRequest.getOriginLocationCode(),
                searchRequest.getDestinationLocationCode(),
                searchRequest.getDepartureDate(),
                searchRequest.getReturnDate(),
                searchRequest.getAdults(),
                searchRequest.getCurrencyCode()
        ).map(bytes -> new String(bytes, StandardCharsets.UTF_8));
        String jsonResponse = flightOffersMono.block();
        return parseFlights(jsonResponse);
    }

    private List<Flight> parseFlights(String jsonResponse) {
        List<Flight> flightOffers = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);

            if (root.has("data") && root.get("data").isArray()) {
                if(root.get("data").isEmpty()) {
                    System.out.println("No flights found");
                    return flightOffers;
                }

                for (JsonNode flightOfferNode : root.get("data")) {
                    Flight flightOffer = new Flight();

                    // Extract itineraries (outbound and return)
                    if (flightOfferNode.has("itineraries") && flightOfferNode.get("itineraries").isArray()) {
                        JsonNode outboundItinerary = flightOfferNode.get("itineraries").get(0);
                        if (outboundItinerary.has("segments") && outboundItinerary.get("segments").isArray()) {
                            JsonNode firstSegment = outboundItinerary.get("segments").get(0);
                            JsonNode lastSegment = outboundItinerary.get("segments").get(outboundItinerary.get("segments").size() - 1);

                            // Origin and Destination
                            flightOffer.setOriginLocationCode(firstSegment.get("departure").get("iataCode").asText());
                            flightOffer.setDestinationLocationCode(lastSegment.get("arrival").get("iataCode").asText());

                            // Departure Date
                            flightOffer.setDepartureDate(firstSegment.get("departure").get("at").asText());

                            // Number of Stops (Outbound)
                            flightOffer.setStopsOutbound(outboundItinerary.get("segments").size() - 1);
                        }

                        // If there is a return itinerary
                        if (flightOfferNode.get("itineraries").size() > 1) {
                            JsonNode returnItinerary = flightOfferNode.get("itineraries").get(1);
                            if (returnItinerary.has("segments") && returnItinerary.get("segments").isArray()) {
                                JsonNode firstReturnSegment = returnItinerary.get("segments").get(0);
                                flightOffer.setReturnDate(firstReturnSegment.get("departure").get("at").asText());

                                // Number of Stops (Return)
                                flightOffer.setStopsReturn(returnItinerary.get("segments").size() - 1);
                            }
                        }
                    }

                    // Currency
                    if (flightOfferNode.has("price") && flightOfferNode.get("price").has("currency")) {
                        flightOffer.setCurrencyCode(flightOfferNode.get("price").get("currency").asText());
                    }

                    // Total Price
                    if (flightOfferNode.has("price") && flightOfferNode.get("price").has("total")) {
                        flightOffer.setTotalPrice(flightOfferNode.get("price").get("total").asDouble());
                    }

                    // Number of Passengers (Based on TravelerPricings array)
                    if (flightOfferNode.has("travelerPricings") && flightOfferNode.get("travelerPricings").isArray()) {
                        flightOffer.setNumberOfPassengers(flightOfferNode.get("travelerPricings").size());
                    }

                    flightOffers.add(flightOffer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flightOffers;
    }

}

