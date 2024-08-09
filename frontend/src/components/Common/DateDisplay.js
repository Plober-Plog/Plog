import React from 'react';
import PropTypes from 'prop-types';
import './DateDisplay.css';

const DateDisplay = ({ date, setDate }) => {
  const handleDateChange = (event) => {
    setDate(event.target.value);
  };

  return (
    <div className="date-display-container">
      <input type="date" value={date} onChange={handleDateChange} className="date-display-input" />
    </div>
  );
};

DateDisplay.propTypes = {
  date: PropTypes.string.isRequired,
  setDate: PropTypes.func.isRequired,
};

export default DateDisplay;
