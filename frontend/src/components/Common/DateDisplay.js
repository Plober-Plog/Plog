import React from 'react';
import PropTypes from 'prop-types';

const DateDisplay = ({ date }) => {
  return (
    <div>
      <h1>{date.toLocaleDateString()}</h1>
    </div>
  );
};

export default DateDisplay;