import React from 'react';
import PropTypes from 'prop-types';

const DateDisplay = ({ date }) => {
  return (
    <div>
      <h1>{ date }</h1>
    </div>
  );
};

// dateDisplay.propTypes = {
//   date: PropTypes.string.isRequired,
// };


export default DateDisplay;