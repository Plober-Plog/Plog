import React from 'react';
import PropTypes from 'prop-types';

const DateDisplay = ({ date, setDate }) => {
  const handleDateChange = (event) => {
    setDate(event.target.value);
  };

  return (
    <div>
      <input type="date" value={date} onChange={handleDateChange} />
    </div>
  );
};

DateDisplay.propTypes = {
  date: PropTypes.string.isRequired,
  setDate: PropTypes.func.isRequired,
};

export default DateDisplay;