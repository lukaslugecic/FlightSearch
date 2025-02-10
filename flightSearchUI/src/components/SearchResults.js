import React from 'react';

const SearchResults = ({ flights, page, totalPages, handlePageChange }) => {
  if (flights.length === 0) {
    return <p>No flights found. Try a different search.</p>;
  }

  const renderPagination = () => {
    const pageNumbers = [];
    for (let i = 0; i < totalPages; i++) {
      pageNumbers.push(
        <button key={i} onClick={() => handlePageChange(i)} disabled={page === i}>
          {i + 1}
        </button>
      );
    }
    return pageNumbers;
  };

  const formatDate = (dateString) => {
    if(!dateString) return '';
    
    const date = new Date(dateString);
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return date.toLocaleDateString(undefined, options);
  };

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
              <td>{formatDate(flight.departureDate)}</td>
              <td>{formatDate(flight.returnDate)}</td>
              <td>{flight.stopsOutbound}</td>
              <td>{flight.stopsReturn}</td>
              <td>{flight.numberOfPassengers}</td>
              <td>{flight.currencyCode}</td>
              <td>{flight.totalPrice}</td>
            </tr>
          ))}
        </tbody>
      </table>
      <div className="pagination">
        {renderPagination()}
      </div>
    </div>
  );
};

export default SearchResults;
