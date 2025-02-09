import React from 'react';

const SearchResults = ({ flights }) => {
  if (flights.length === 0) {
    return <p>No flights found. Try a different search.</p>;
  }

  return (
    <div className="search-results">
      <h2>Flight Search Results</h2>
      <table>
        <thead>
          <tr>
            <th>Origin</th>
            <th>Destination</th>
            <th>Departure</th>
            <th>Return</th>
            <th>Stops (Outbound)</th>
            <th>Stops (Return)</th>
            <th>Passengers</th>
            <th>Currency</th>
            <th>Price</th>
          </tr>
        </thead>
        <tbody>
          {flights.map((flight, index) => (
            <tr key={index}>
              <td>{flight.originLocationCode}</td>
              <td>{flight.destinationLocationCode}</td>
              <td>{flight.departureDate}</td>
              <td>{flight.returnDate}</td>
              <td>{flight.stopsOutbound}</td>
              <td>{flight.stopsReturn}</td>
              <td>{flight.numberOfPassengers}</td>
              <td>{flight.currencyCode}</td>
              <td>{flight.totalPrice}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default SearchResults;
