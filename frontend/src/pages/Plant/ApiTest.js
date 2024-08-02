import React, { useEffect, useState } from "react";

const ApiTest = () => {

  const URI = `https://i11b308.p.ssafy.io/api/user/plant-type/2`;

  const [plantTypeData, setPlantTypeData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error ,setError] = useState(null);

  useEffect(() => {
    const fetchPlantTypeData = async () => {
      try {
        const response = await fetch(URI);
        if (!response.ok) {
          throw new Error("Network Response was not OK")
        }
        const data = await response.json();
        setPlantTypeData(data);
      } catch (error) {
        setError(error.message);
      } finally {
        setLoading(false);
      }
    };

    fetchPlantTypeData();
  }, [URI]);

  if (loading) {
    return <p>Loading...</p>
  }

  if (error) {
    return <div>error: {error}</div>
  }

  return (
    <div>
      <h1>API Test</h1>
      {plantTypeData ? (
        <p>{plantTypeData}</p>
      ) : (
        <p>뭔가 잘못됨</p>
      )}
    </div>
  )
}

export default ApiTest;