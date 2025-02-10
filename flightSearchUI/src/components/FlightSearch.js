import React from 'react';

const FlightSearch = ({ onSearch, searchParams, setSearchParams }) => {
  
  const handleChange = (e) => {
    const { name, value } = e.target;
    setSearchParams(prevParams => ({
      ...prevParams,
      [name]: value
      
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSearch(searchParams);
  };

  return (
    <form onSubmit={handleSubmit} className="flight-search-form">
      <div>
        <label htmlFor="originLocationCode">Origin Airport:</label>
        <input
          type="text"
          id="originLocationCode"
          name="originLocationCode"
          value={searchParams.originLocationCode}
          onChange={handleChange}
          required
        />
      </div>
      <div>
        <label htmlFor="destinationLocationCode">Destination Airport:</label>
        <input
          type="text"
          id="destinationLocationCode"
          name="destinationLocationCode"
          value={searchParams.destinationLocationCode}
          onChange={handleChange}
          required
        />
      </div>
      <div>
        <label htmlFor="departureDate">Departure Date:</label>
        <input
          type="date"
          id="departureDate"
          name="departureDate"
          value={searchParams.departureDate}
          onChange={handleChange}
          required
        />
      </div>
      <div>
        <label htmlFor="returnDate">Return Date:</label>
        <input
          type="date"
          id="returnDate"
          name="returnDate"
          value={searchParams.returnDate}
          onChange={handleChange}
        />
      </div>
      <div>
        <label htmlFor="adults">Number of Passengers:</label>
        <input
          type="number"
          id="adults"
          name="adults"
          value={searchParams.adults}
          onChange={handleChange}
          min="1"
          required
        />
      </div>
      <div>
        <label htmlFor="currencyCode">Currency Code:</label>
        <input
          type="text"
          id="currencyCode"
          name="currencyCode"
          value={searchParams.currencyCode}
          onChange={handleChange}
        />
      </div>
      <button type="submit">Search Flights</button>
    </form>
  );
};

export default FlightSearch;
