import React, { useState } from 'react';

const DiaryWeather = ({ weather, temperature, humidity, content }) => {
  return (
    <div>
      <div>
        <span>날씨: {weather}</span>
      </div>
      <div>
        <span>온도: {temperature} °C</span>
      </div>
      <div>
        <span>습도: {humidity}</span>
      </div>
      <div>
        <p>{content}</p>
      </div>
    </div>
  );
};

export default DiaryWeather;