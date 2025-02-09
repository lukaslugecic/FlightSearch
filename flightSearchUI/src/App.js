import React, { useState } from 'react';
import FlightSearch from './components/FlightSearch';
import SearchResults from './components/SearchResults';
import './styles.css';

function App() {
  const [flights, setFlights] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSearch = async (searchParams) => {
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
    } catch (error) {
      console.error('Error searching flights:', error);
      setError('Failed to fetch flight data. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="App">
      <h1>Flight Search</h1>
      <FlightSearch onSearch={handleSearch} />
      {isLoading && <p>Loading...</p>}
      {error && <p className="error">{error}</p>}
      {!isLoading && !error && <SearchResults flights={flights} />}
    </div>
  );
}

export default App;
