import React from 'react';
import PropTypes from 'prop-types';
<<<<<<< HEAD
=======
import './DateDisplay.css';
>>>>>>> master

const DateDisplay = ({ date, setDate }) => {
  const handleDateChange = (event) => {
    setDate(event.target.value);
  };

  return (
<<<<<<< HEAD
    <div>
      <input type="date" value={date} onChange={handleDateChange} />
=======
    <div className="date-display-container">
      <input type="date" value={date} onChange={handleDateChange} className="date-display-input" />
>>>>>>> master
    </div>
  );
};

DateDisplay.propTypes = {
  date: PropTypes.string.isRequired,
  setDate: PropTypes.func.isRequired,
};

<<<<<<< HEAD
export default DateDisplay;
=======
export default DateDisplay;
>>>>>>> master
