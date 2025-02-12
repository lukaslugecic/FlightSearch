import React, { useState, useEffect } from 'react';
import FlightSearch from './components/FlightSearch';
import SearchResults from './components/SearchResults';
import './styles.css';

function App() {
  const [flights, setFlights] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [showSearchForm, setShowSearchForm] = useState(true);
  const [page, setPage] = useState(0);
  const [searchParams, setSearchParams] = useState({
    originLocationCode: '',
    destinationLocationCode: '',
    departureDate: '',
    returnDate: '',
    adults: 1,
    currencyCode: 'EUR'
  });

  const pageSize = 10;
  const totalPages = Math.ceil(flights.length / pageSize);
  const pagedFlights = flights.slice(page * pageSize, (page + 1) * pageSize);

  const handleSearch = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await fetch('http://localhost:8080/api/flights/search', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(searchParams),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      setFlights(data);
      setShowSearchForm(false);
    } catch (error) {
      console.error('Error searching flights:', error);
      setError('Failed to fetch flight data. Please try again.');
    } finally {
      setIsLoading(false);
      setShowSearchForm(false);
    }
  };

  const toggleSearchForm = () => {
    setShowSearchForm(!showSearchForm);
  };

  const handlePageChange = (newPage) => {
    setPage(newPage);
  };

  useEffect(() => {
    if (!showSearchForm && flights.length === 0) {
      handleSearch();
    }
  }, [showSearchForm, searchParams]);

  return (
    <div className="App">
      <div className="header">
        <h1>Flight Search</h1>
        <button onClick={toggleSearchForm}>Search Flights</button>
      </div>
      {showSearchForm && <FlightSearch onSearch={handleSearch} searchParams={searchParams} setSearchParams={setSearchParams} />}
      {isLoading && <p>Loading...</p>}
      {error && <p className="error">{error}</p>}
      {!isLoading && !error && <SearchResults flights={pagedFlights} page={page} totalPages={totalPages} handlePageChange={handlePageChange}/>}
    </div>
  );
}

export default App;
